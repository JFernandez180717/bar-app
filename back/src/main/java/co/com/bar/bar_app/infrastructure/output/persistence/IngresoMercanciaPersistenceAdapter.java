package co.com.bar.bar_app.infrastructure.output.persistence;

import co.com.bar.bar_app.application.ports.out.IngresoMercanciaOutPort;
import co.com.bar.bar_app.domain.model.IngresoMercancia;
import co.com.bar.bar_app.domain.model.IngresoMercanciaDetalle;
import co.com.bar.bar_app.domain.model.Producto;
import co.com.bar.bar_app.infrastructure.mapper.IngresoMercanciaMapper;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.IngresoMercanciaDetalleEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.IngresoMercanciaEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.IngresoMercanciaRepository;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.ProductoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class IngresoMercanciaPersistenceAdapter implements IngresoMercanciaOutPort {

    private final IngresoMercanciaRepository ingresoRepository;
    private final ProductoRepository productoRepository;
    private final IngresoMercanciaMapper mapper;

    public IngresoMercanciaPersistenceAdapter(
            IngresoMercanciaRepository ingresoRepository,
            ProductoRepository productoRepository,
            IngresoMercanciaMapper mapper) {
        this.ingresoRepository = ingresoRepository;
        this.productoRepository = productoRepository;
        this.mapper = mapper;
    }

    @Override
    public IngresoMercancia create(IngresoMercancia ingreso, String username) {
        IngresoMercanciaEntity entity = mapper.domainToEntity(ingreso);
        entity.setFechaCreacion(LocalDateTime.now());
        entity.setUsuarioCrea(username);
        entity.setEstado(true);

        // Guardar cabecera primero
        IngresoMercanciaEntity saved = ingresoRepository.save(entity);

        // Guardar detalles
        for (IngresoMercanciaDetalle detalle : ingreso.getDetalles()) {
            IngresoMercanciaDetalleEntity detalleEntity = mapper.domainDetalleToEntity(detalle);
            detalleEntity.setCodigo(saved.getId());
            // La entity se guarda automáticamente por cascade
            saved.getDetalles().add(detalleEntity);
        }

        // Guardar con detalles
        saved = ingresoRepository.save(saved);

        return mapper.entityToDomain(saved);
    }

    @Override
    public Optional<IngresoMercancia> findById(UUID id) {
        return ingresoRepository.findById(id)
                .map(entity -> {
                    IngresoMercancia ingreso = mapper.entityToDomain(entity);
                    // Cargar detalles manualmente si es necesario
                    List<IngresoMercanciaDetalle> detalles = entity.getDetalles().stream()
                            .map(mapper::entityDetalleToDomain)
                            .collect(Collectors.toList());
                    // Crear nuevo ingreso con detalles
                    return IngresoMercancia.builder()
                            .id(ingreso.getId())
                            .fecha(ingreso.getFecha())
                            .usuarioRecibe(ingreso.getUsuarioRecibe())
                            .idProveedor(ingreso.getIdProveedor())
                            .estado(ingreso.isEstado())
                            .detalles(detalles)
                            .build();
                });
    }

    @Override
    public List<IngresoMercancia> findAll(LocalDate fechaInicio, LocalDate fechaFin, String idProveedor) {
        List<IngresoMercanciaEntity> entities = ingresoRepository.findByFilters(fechaInicio, fechaFin, idProveedor);

        return entities.stream()
                .map(entity -> {
                    IngresoMercancia ingreso = mapper.entityToDomain(entity);
                    List<IngresoMercanciaDetalle> detalles = entity.getDetalles().stream()
                            .map(mapper::entityDetalleToDomain)
                            .collect(Collectors.toList());
                    return IngresoMercancia.builder()
                            .id(ingreso.getId())
                            .fecha(ingreso.getFecha())
                            .usuarioRecibe(ingreso.getUsuarioRecibe())
                            .idProveedor(ingreso.getIdProveedor())
                            .estado(ingreso.isEstado())
                            .detalles(detalles)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Producto> findProductoById(UUID id) {
        return productoRepository.findById(id)
                .map(entity -> {
                    // Crear Producto sin categoría ni marca para evitar validación
                    // Solo necesitamos el stock para la operación de ingreso
                    return new Producto(
                            entity.getId(),
                            entity.getNombre(),
                            entity.getDescripcion(),
                            entity.getStock(),
                            entity.getStockMinimo(),
                            entity.getPrecio(),
                            entity.getImagen(),
                            entity.getCategoria() != null ? entity.getCategoria().toDomain() : null,
                            entity.getMarca() != null ? entity.getMarca().toDomain() : null,
                            entity.isDestacado(),
                            entity.isEstado()
                    );
                });
    }

    @Override
    public Producto updateProducto(Producto producto) {
        // Actualizar solo el stock usando JPQL
        productoRepository.actualizarStock(producto.getId(), producto.getStock());
        return producto;
    }

    @Override
    public Optional<IngresoMercancia> update(IngresoMercancia ingreso, String username) {
        return ingresoRepository.findById(ingreso.getId())
                .map(entity -> {
                    entity.setEstado(ingreso.isEstado());
                    entity.setFechaActualizacion(LocalDateTime.now());
                    entity.setUsuarioModifica(username);
                    return ingresoRepository.save(entity);
                })
                .map(saved -> findById(saved.getId()).orElse(null));
    }
}