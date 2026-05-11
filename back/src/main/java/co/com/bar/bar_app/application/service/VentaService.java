package co.com.bar.bar_app.application.service;

import co.com.bar.bar_app.application.dto.CrearVentaDto;
import co.com.bar.bar_app.application.dto.DetalleVentaItem;
import co.com.bar.bar_app.application.dto.VentaApplicationDto;
import co.com.bar.bar_app.application.dto.VentaCreadaDto;
import co.com.bar.bar_app.application.mapper.VentaApplicationMapper;
import co.com.bar.bar_app.application.ports.in.VentaInPort;
import co.com.bar.bar_app.application.ports.out.MesaOutPort;
import co.com.bar.bar_app.application.ports.out.ProductoOutPort;
import co.com.bar.bar_app.application.ports.out.TipoPagoOutPort;
import co.com.bar.bar_app.application.ports.out.VentaOutPort;
import co.com.bar.bar_app.domain.exception.VentaNotValidException;
import co.com.bar.bar_app.domain.model.Mesa;
import co.com.bar.bar_app.domain.model.Producto;
import co.com.bar.bar_app.domain.model.TipoPago;
import co.com.bar.bar_app.domain.model.Venta;
import co.com.bar.bar_app.domain.model.VentaDetalle;
import co.com.bar.bar_app.domain.model.PageResponse;
import co.com.bar.bar_app.domain.model.PaginationRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService implements VentaInPort {
    private final VentaOutPort ventaOutPort;
    private final VentaApplicationMapper ventaApplicationMapper;
    private final MesaOutPort mesaOutPort;
    private final TipoPagoOutPort tipoPagoOutPort;
    private final ProductoOutPort productoOutPort;

    public VentaService(VentaOutPort ventaOutPort, VentaApplicationMapper ventaApplicationMapper,
                     MesaOutPort mesaOutPort, TipoPagoOutPort tipoPagoOutPort,
                     ProductoOutPort productoOutPort) {
        this.ventaOutPort = ventaOutPort;
        this.ventaApplicationMapper = ventaApplicationMapper;
        this.mesaOutPort = mesaOutPort;
        this.tipoPagoOutPort = tipoPagoOutPort;
        this.productoOutPort = productoOutPort;
    }

    @Override
    public VentaCreadaDto crearVenta(CrearVentaDto dto, String username) {
        // 1. Validar mesa existe y está activa
        List<Mesa> mesas = this.mesaOutPort.findAll();
        Mesa mesa = mesas.stream()
                .filter(m -> m.getId() == dto.mesaId())
                .filter(Mesa::isActivo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La mesa no existe o no está activa"));

        // 2. Validar tipoPago existe
        TipoPago tipoPago = this.tipoPagoOutPort.findById(dto.tipoPagoId());
        if (tipoPago == null) {
            throw new IllegalArgumentException("El tipo de pago no existe");
        }

        // 3. Construir objetos Producto y VentaDetalle para cada item
        List<VentaDetalle> detalles = new ArrayList<>();
        List<String> erroresValidacion = new ArrayList<>();

        for (DetalleVentaItem item : dto.items()) {
            // Validar producto existe y está activo
            Producto producto = this.productoOutPort.findById(item.productoId());
            if (producto == null) {
                erroresValidacion.add("El producto con ID " + item.productoId() + " no existe");
                continue;
            }
            if (!producto.isActivo()) {
                erroresValidacion.add("El producto con ID " + item.productoId() + " no está activo");
                continue;
            }

            // Validar precioUnitario contra precio del producto (manipulación)
            if (item.precioUnitario() != producto.getPrecio()) {
                erroresValidacion.add("El precio unitario del producto " + producto.getNombre() +
                        " no coincide. Esperado: " + producto.getPrecio() + ", Recibido: " + item.precioUnitario());
                continue;
            }

            // Construir VentaDetalle
            VentaDetalle detalle = VentaDetalle.crear(producto, item.cantidad(), item.precioUnitario(), item.descuento());
            detalles.add(detalle);
        }

        if (!erroresValidacion.isEmpty()) {
            throw new VentaNotValidException(erroresValidacion);
        }

        // Calcular total
        double total = detalles.stream()
                .mapToDouble(VentaDetalle::getTotal)
                .sum();

        // 4. Construir Venta
        Venta venta = Venta.crear(detalles, dto.totalDescuento(), username, tipoPago, mesa);

        // 5. Llamar ventaOutPort.create()
        Venta ventaCreada = this.ventaOutPort.create(venta, username);

        // 6. Retornar VentaCreadaDto
        return this.ventaApplicationMapper.domainToCreadoDto(ventaCreada);
    }

    @Override
    public PageResponse<VentaApplicationDto> findAll(PaginationRequest pagination) {
        PageResponse<Venta> ventas = this.ventaOutPort.findAll(pagination);
        List<VentaApplicationDto> content = ventas.content()
                .stream()
                .map(this.ventaApplicationMapper::domainToApplicationDto)
                .toList();

        return new PageResponse<>(
                content,
                ventas.page(),
                ventas.size(),
                ventas.totalElements(),
                ventas.totalPages()
        );
    }

    @Override
    public VentaApplicationDto findById(int codigo) {
        Venta venta = this.ventaOutPort.findById(codigo);
        return this.ventaApplicationMapper.domainToApplicationDto(venta);
    }
}