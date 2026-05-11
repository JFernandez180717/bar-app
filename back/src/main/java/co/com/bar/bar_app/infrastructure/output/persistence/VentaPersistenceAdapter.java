package co.com.bar.bar_app.infrastructure.output.persistence;

import co.com.bar.bar_app.application.ports.out.VentaOutPort;
import co.com.bar.bar_app.domain.model.*;
import co.com.bar.bar_app.domain.exception.PrecioManipuladoException;
import co.com.bar.bar_app.domain.exception.StockInsuficienteException;
import co.com.bar.bar_app.infrastructure.mapper.VentaMapper;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.*;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class VentaPersistenceAdapter implements VentaOutPort {
    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final ProductoRepository productoRepository;
    private final MesaRepository mesaRepository;
    private final TipoPagoRepository tipoPagoRepository;
    private final VentaMapper ventaMapper;

    public VentaPersistenceAdapter(
            VentaRepository ventaRepository,
            VentaDetalleRepository ventaDetalleRepository,
            ProductoRepository productoRepository,
            MesaRepository mesaRepository,
            TipoPagoRepository tipoPagoRepository,
            VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.ventaDetalleRepository = ventaDetalleRepository;
        this.productoRepository = productoRepository;
        this.mesaRepository = mesaRepository;
        this.tipoPagoRepository = tipoPagoRepository;
        this.ventaMapper = ventaMapper;
    }

    @Override
    @Transactional
    public Venta create(Venta venta, String username) {
        // 1. Validar y preparar datos de la venta
        VentaEntity ventaEntity = new VentaEntity();
        ventaEntity.setFecha(LocalDateTime.now());
        ventaEntity.setUsuario(username);
        ventaEntity.setFechaCreacion(LocalDateTime.now());
        ventaEntity.setUsuarioCrea(username);
        ventaEntity.setEstado(true);
        ventaEntity.setTotalDescuento(venta.getTotalDescuento());

        // Obtener referencias de entidades relacionadas
        TipoPagoEntity tipoPagoEntity = tipoPagoRepository.findById(venta.getTipoPago().getId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de pago no encontrado"));
        MesaEntity mesaEntity = mesaRepository.findById(venta.getMesa().getId())
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

        ventaEntity.setTipoPago(tipoPagoEntity);
        ventaEntity.setMesa(mesaEntity);

        // 2. Procesar cada detalle y descontar stock con pessimistic lock
        double totalVenta = 0;
        List<VentaDetalleEntity> detallesEntity = new ArrayList<>();

        for (VentaDetalle detalle : venta.getDetalles()) {
            UUID productoId = detalle.getProducto().getId();

            // Obtener producto con lock para evitar race conditions
            ProductoEntity productoEntity = productoRepository.findByIdWithLock(productoId);

            // Validar que el precio unitario coincida con el del producto (detección de manipulación)
            if (detalle.getPrecioUnitario() != productoEntity.getPrecio()) {
                throw new PrecioManipuladoException(
                        "Precio unitario manipulado para el producto: " + productoEntity.getNombre() +
                        ". Esperado: " + productoEntity.getPrecio() + ", Recibido: " + detalle.getPrecioUnitario());
            }

            // Validar stock disponible
            if (productoEntity.getStock() < detalle.getCantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para el producto: " + productoEntity.getNombre() +
                        ". Disponible: " + productoEntity.getStock() + ", Solicitado: " + detalle.getCantidad());
            }

            // Descontar stock
            productoEntity.setStock(productoEntity.getStock() - detalle.getCantidad());
            productoEntity.setFechaActualizacion(LocalDateTime.now());
            productoEntity.setUsuarioModifica(username);
            productoRepository.save(productoEntity);

            // Crear entidad de detalle
            VentaDetalleEntity detalleEntity = new VentaDetalleEntity();
            detalleEntity.setIdProducto(productoEntity);
            detalleEntity.setCantidad(detalle.getCantidad());
            detalleEntity.setPrecioUnitario(detalle.getPrecioUnitario());
            detalleEntity.setDescuento(detalle.getDescuento());
            double totalDetalle = (detalle.getPrecioUnitario() * detalle.getCantidad()) - detalle.getDescuento();
            detalleEntity.setTotal(totalDetalle);

            detallesEntity.add(detalleEntity);
            totalVenta += totalDetalle;
        }

        // Aplicar descuento total
        totalVenta -= venta.getTotalDescuento();

        ventaEntity.setTotal(totalVenta);
        ventaEntity.setDetalles(detallesEntity);

        // 3. Persistir la venta
        VentaEntity savedVenta = this.ventaRepository.save(ventaEntity);

        // 4. Retornar el dominio mapeado
        return this.ventaMapper.entityToDomain(savedVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Venta> findAll(PaginationRequest pagination) {
        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size());
        Page<VentaEntity> page = this.ventaRepository.findAll(pageRequest);

        List<Venta> ventas = page.getContent().stream()
                .map(ventaMapper::entityToDomain)
                .toList();

        return new PageResponse<>(
                ventas,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Venta findById(int codigo) {
        VentaEntity ventaEntity = this.ventaRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No existe la venta con el id: " + codigo));
        return this.ventaMapper.entityToDomain(ventaEntity);
    }
}
