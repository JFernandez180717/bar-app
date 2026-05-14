package co.com.bar.bar_app.application.service;

import co.com.bar.bar_app.application.dto.CrearIngresoMercanciaDto;
import co.com.bar.bar_app.application.dto.IngresoMercanciaApplicationDto;
import co.com.bar.bar_app.application.dto.IngresoMercanciaCreadoDto;
import co.com.bar.bar_app.application.dto.IngresoMercanciaDetalleDto;
import co.com.bar.bar_app.application.mapper.IngresoMercanciaApplicationMapper;
import co.com.bar.bar_app.application.ports.in.IngresoMercanciaInPort;
import co.com.bar.bar_app.application.ports.out.IngresoMercanciaOutPort;
import co.com.bar.bar_app.domain.model.IngresoMercancia;
import co.com.bar.bar_app.domain.model.IngresoMercanciaDetalle;
import co.com.bar.bar_app.domain.model.Producto;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.ProveedorRepository;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IngresoMercanciaService implements IngresoMercanciaInPort {

    private final IngresoMercanciaOutPort ingresoOutPort;
    private final IngresoMercanciaApplicationMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;

    public IngresoMercanciaService(
            IngresoMercanciaOutPort ingresoOutPort,
            IngresoMercanciaApplicationMapper mapper,
            UsuarioRepository usuarioRepository,
            ProveedorRepository proveedorRepository) {
        this.ingresoOutPort = ingresoOutPort;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    @Transactional
    public IngresoMercanciaCreadoDto create(CrearIngresoMercanciaDto dto, String username) {
        // Validar que el usuario_recibe existe
        if (!usuarioRepository.existsById(dto.usuarioRecibe())) {
            throw new IllegalArgumentException("Usuario receptor no encontrado: " + dto.usuarioRecibe());
        }

        // Validar proveedor existe (si se envía)
        if (dto.idProveedor() != null && !dto.idProveedor().isEmpty()) {
            if (!proveedorRepository.existsById(dto.idProveedor())) {
                throw new IllegalArgumentException("Proveedor no encontrado: " + dto.idProveedor());
            }
        }

        // Validar que los productos existen y collecting IDs
        Set<UUID> productoIds = dto.detalles().stream()
                .map(IngresoMercanciaDetalleDto::idProducto)
                .collect(Collectors.toSet());

        Map<UUID, Producto> productosMap = productoIds.stream()
                .map(id -> ingresoOutPort.findProductoById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id)))
                .collect(Collectors.toMap(Producto::getId, p -> p));

        // Convertir DTO a Domain
        IngresoMercancia ingreso = mapper.toDomain(dto);

        // Persistir ingreso
        IngresoMercancia guardado = ingresoOutPort.create(ingreso, username);

        // Actualizar stock de cada producto
        for (IngresoMercanciaDetalle detalle : ingreso.getDetalles()) {
            Producto producto = productosMap.get(detalle.getIdProducto());
            int nuevoStock = producto.getStock() + detalle.getCantidad();
            // Crear producto actualizado con mismo ID
            Producto productoActualizado = new Producto(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    nuevoStock,
                    producto.getStockMinimo(),
                    producto.getPrecio(),
                    producto.getImagen(),
                    producto.getCategoria(),
                    producto.getMarca(),
                    producto.isDestacado(),
                    producto.isActivo()
            );
            ingresoOutPort.updateProducto(productoActualizado);
        }

        return mapper.toCreadoDto(guardado);
    }

    @Override
    public List<IngresoMercanciaApplicationDto> findAll(LocalDate fechaInicio, LocalDate fechaFin, String idProveedor) {
        List<IngresoMercancia> ingresos = ingresoOutPort.findAll(fechaInicio, fechaFin, idProveedor);

        // Obtener nombres de proveedores
        Set<String> proveedorIds = ingresos.stream()
                .filter(i -> i.getIdProveedor() != null)
                .map(IngresoMercancia::getIdProveedor)
                .collect(Collectors.toSet());

        Map<String, String> proveedorNombres = proveedorIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> proveedorRepository.findById(id)
                                .map(p -> p.getNombre())
                                .orElse("Desconocido")
                ));

        // Obtener nombres de productos
        Set<UUID> productoIds = ingresos.stream()
                .flatMap(i -> i.getDetalles().stream())
                .map(IngresoMercanciaDetalle::getIdProducto)
                .collect(Collectors.toSet());

        Map<UUID, String> productoNombres = productoIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> ingresoOutPort.findProductoById(id)
                                .map(Producto::getNombre)
                                .orElse("Desconocido")
                ));

        return ingresos.stream()
                .map(i -> mapper.toApplicationDtoWithProductoNombre(
                        i,
                        i.getIdProveedor() != null ? proveedorNombres.getOrDefault(i.getIdProveedor(), "Desconocido") : "Sin proveedor",
                        productoNombres
                ))
                .toList();
    }

    @Override
    public IngresoMercanciaApplicationDto findById(UUID id) {
        IngresoMercancia ingreso = ingresoOutPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingreso de mercancía no encontrado"));

        String nombreProveedor = ingreso.getIdProveedor() != null
                ? proveedorRepository.findById(ingreso.getIdProveedor())
                        .map(p -> p.getNombre())
                        .orElse("Desconocido")
                : "Sin proveedor";

        // Obtener nombres de productos
        Map<UUID, String> productoNombres = ingreso.getDetalles().stream()
                .collect(Collectors.toMap(
                        IngresoMercanciaDetalle::getIdProducto,
                        d -> ingresoOutPort.findProductoById(d.getIdProducto())
                                .map(Producto::getNombre)
                                .orElse("Desconocido")
                ));

        return mapper.toApplicationDtoWithProductoNombre(ingreso, nombreProveedor, productoNombres);
    }

    @Override
    @Transactional
    public IngresoMercanciaApplicationDto anular(UUID id, String username, String rol) {
        // Verificar permisos
        if (rol == null || (!rol.equals("super") && !rol.equals("admin"))) {
            throw new IllegalArgumentException("No tiene permisos para anular ingresos");
        }

        IngresoMercancia ingreso = ingresoOutPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingreso de mercancía no encontrado"));

        if (!ingreso.isEstado()) {
            throw new IllegalArgumentException("El ingreso ya está anulado");
        }

        // Restar stock de cada producto
        for (IngresoMercanciaDetalle detalle : ingreso.getDetalles()) {
            Producto producto = ingresoOutPort.findProductoById(detalle.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + detalle.getIdProducto()));

            int nuevoStock = producto.getStock() - detalle.getCantidad();
            if (nuevoStock < 0) nuevoStock = 0;

            Producto productoConId = new Producto(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    nuevoStock,
                    producto.getStockMinimo(),
                    producto.getPrecio(),
                    producto.getImagen(),
                    producto.getCategoria(),
                    producto.getMarca(),
                    producto.isDestacado(),
                    producto.isActivo()
            );
            ingresoOutPort.updateProducto(productoConId);
        }

        // Crear ingreso anulado (nuevo estado)
        IngresoMercancia ingresoAnulado = IngresoMercancia.builder()
                .id(ingreso.getId())
                .fecha(ingreso.getFecha())
                .usuarioRecibe(ingreso.getUsuarioRecibe())
                .idProveedor(ingreso.getIdProveedor())
                .estado(false)
                .detalles(ingreso.getDetalles())
                .build();

        IngresoMercancia actualizado = ingresoOutPort.update(ingresoAnulado, username)
                .orElseThrow(() -> new IllegalArgumentException("Error al actualizar el ingreso de mercancía"));

        String nombreProveedor = actualizado.getIdProveedor() != null
                ? proveedorRepository.findById(actualizado.getIdProveedor())
                        .map(p -> p.getNombre())
                        .orElse("Desconocido")
                : "Sin proveedor";

        Map<UUID, String> productoNombres = actualizado.getDetalles().stream()
                .collect(Collectors.toMap(
                        IngresoMercanciaDetalle::getIdProducto,
                        d -> ingresoOutPort.findProductoById(d.getIdProducto())
                                .map(Producto::getNombre)
                                .orElse("Desconocido")
                ));

        return mapper.toApplicationDtoWithProductoNombre(actualizado, nombreProveedor, productoNombres);
    }
}