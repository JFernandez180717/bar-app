package co.com.bar.bar_app.infrastructure.output.persistence;

import co.com.bar.bar_app.application.ports.out.ProveedorOutPort;
import co.com.bar.bar_app.domain.model.Proveedor;
import co.com.bar.bar_app.infrastructure.mapper.ProveedorMapper;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.ProveedorEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.ProveedorRepository;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.TipoIdentificacionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProveedorPersistenceAdapter implements ProveedorOutPort {
    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;
    private final TipoIdentificacionRepository tipoIdentificacionRepository;

    public ProveedorPersistenceAdapter(
            ProveedorRepository proveedorRepository,
            ProveedorMapper proveedorMapper,
            TipoIdentificacionRepository tipoIdentificacionRepository) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
    }

    @Override
    public Proveedor create(Proveedor proveedor, String username) {
        ProveedorEntity entity = new ProveedorEntity();  // No usa mapper
        entity.setIdentificacion(proveedor.getIdentificacion());
        entity.setNombre(proveedor.getNombre());
        entity.setDireccion(proveedor.getDireccion());
        entity.setTelefono(proveedor.getTelefono());
        entity.setTipoIdentificacion(
                tipoIdentificacionRepository.findById(proveedor.getTipoIdentificacion())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Tipo de identificacion no encontrado: " + proveedor.getTipoIdentificacion()))
        );
        entity.setEstado(true);
        entity.setFechaCreacion(LocalDateTime.now());
        entity.setUsuarioCrea(username);
        ProveedorEntity saved = this.proveedorRepository.save(entity);
        return proveedorMapper.entityToDomain(saved);
    }

    @Override
    public List<Proveedor> findAll() {
        return this.proveedorRepository.findAll().stream()
                .map(proveedorMapper::entityToDomain)
                .toList();
    }

    @Override
    public Proveedor update(Proveedor proveedor, String username) {
        ProveedorEntity entity = this.proveedorRepository.findById(proveedor.getIdentificacion())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + proveedor.getIdentificacion()));

        entity.setNombre(proveedor.getNombre());
        entity.setDireccion(proveedor.getDireccion());
        entity.setTelefono(proveedor.getTelefono());
        entity.setTipoIdentificacion(
                tipoIdentificacionRepository.findById(proveedor.getTipoIdentificacion())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Tipo de identificacion no encontrado: " + proveedor.getTipoIdentificacion()))
        );
        entity.setEstado(proveedor.isEstado());
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setUsuarioModifica(username);

        ProveedorEntity saved = this.proveedorRepository.save(entity);
        return proveedorMapper.entityToDomain(saved);
    }
}
