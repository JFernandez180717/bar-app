package co.com.bar.bar_app.infrastructure.output.persistence;

import co.com.bar.bar_app.application.ports.out.MesaOutPort;
import co.com.bar.bar_app.domain.model.Mesa;
import co.com.bar.bar_app.infrastructure.mapper.MesaMapper;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.MesaEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.MesaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MesaPersistenceAdapter implements MesaOutPort {
    private final MesaRepository mesaRepository;
    private final MesaMapper mesaMapper;

    public MesaPersistenceAdapter(MesaRepository mesaRepository, MesaMapper mesaMapper) {
        this.mesaRepository = mesaRepository;
        this.mesaMapper = mesaMapper;
    }

    @Override
    public Mesa create(String username) {
        MesaEntity mesaEntity = new MesaEntity();
        mesaEntity.setEstado(true);
        mesaEntity.setFechaCreacion(LocalDateTime.now());
        mesaEntity.setUsuarioCrea(username);
        return mesaMapper.entityToDomain(this.mesaRepository.save(mesaEntity));
    }

    @Override
    public List<Mesa> findAll() {
        return this.mesaRepository.findAll().stream().map(mesaMapper::entityToDomain).toList();
    }

    @Override
    public void changeStatus(int id, String username) {
        MesaEntity mesaEntity = this.mesaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe el mesa con el id: " + id));
        mesaEntity.setEstado(!mesaEntity.isEstado());
        mesaEntity.setFechaActualizacion(LocalDateTime.now());
        mesaEntity.setUsuarioModifica(username);
        this.mesaRepository.save(mesaEntity);
    }
}
