package co.com.bar.bar_app.infrastructure.output.persistence;

import co.com.bar.bar_app.application.ports.out.MarcaOutPort;
import co.com.bar.bar_app.application.ports.out.MesaOutPort;
import co.com.bar.bar_app.domain.model.Marca;
import co.com.bar.bar_app.domain.model.Mesa;
import co.com.bar.bar_app.infrastructure.mapper.MarcaMapper;
import co.com.bar.bar_app.infrastructure.mapper.MesaMapper;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.MarcaEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.MesaEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.MarcaRepository;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.MesaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class MarcaPersistenceAdapter implements MarcaOutPort {
    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public MarcaPersistenceAdapter(MarcaRepository marcaRepository, MarcaMapper marcaMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    @Override
    public void create(Marca marca, String username) {
        MarcaEntity entity = marcaMapper.domainToEntity(marca);
        entity.setEstado(true);
        entity.setFechaCreacion(LocalDateTime.now());
        entity.setUsuarioCrea(username);
        this.marcaRepository.save(entity);
    }

    @Override
    public List<Marca> findAll() {
        return this.marcaRepository.findAll().stream().map(marcaMapper::entityToDomain).toList();
    }

    @Override
    public void update(UUID id, String descripcion, Integer estado, String username) {
        MarcaEntity entity = this.marcaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Marca no encontrada"));
        entity.setDescripcion(descripcion);
        if (estado != null) {
            entity.setEstado(estado == 1);
        }
        entity.setUsuarioModifica(username);
        entity.setFechaActualizacion(LocalDateTime.now());
        this.marcaRepository.save(entity);
    }

    @Override
    public void changeStatus(UUID id, String username) {
        MarcaEntity entity = this.marcaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Marca no encontrada"));
        entity.setEstado(!entity.isEstado());
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setUsuarioModifica(username);
        this.marcaRepository.save(entity);
    }

    @Override
    public Marca findById(UUID id) {
        return marcaMapper.entityToDomain(this.marcaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Marca no encontrada")));
    }
}
