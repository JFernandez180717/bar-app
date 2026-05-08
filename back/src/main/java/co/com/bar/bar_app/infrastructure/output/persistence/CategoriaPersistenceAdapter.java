package co.com.bar.bar_app.infrastructure.output.persistence;

import co.com.bar.bar_app.application.ports.out.CategoriaOutPort;
import co.com.bar.bar_app.domain.model.Categoria;
import co.com.bar.bar_app.infrastructure.mapper.CategoriaMapper;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.CategoriaEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.CategoriaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class CategoriaPersistenceAdapter implements CategoriaOutPort {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaPersistenceAdapter(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Override
    public Categoria create(Categoria categoria, String username) {
        CategoriaEntity categoriaEntity = new CategoriaEntity();
        categoriaEntity.setId(categoria.getId());
        categoriaEntity.setDescripcion(categoria.getDescripcion());
        categoriaEntity.setEstado(true);
        categoriaEntity.setUsuarioCrea(username);
        categoriaEntity.setFechaCreacion(LocalDateTime.now());
        return categoriaMapper.categoriaEntityToCategoria(this.categoriaRepository.save(categoriaEntity));
    }

    @Override
    public List<Categoria> findAll() {
        return this.categoriaRepository.findAll().stream().map(categoriaMapper::categoriaEntityToCategoria).toList();
    }

    @Override
    public void update(UUID id, String descripcion, String username) {
        CategoriaEntity entity = this.categoriaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        entity.setDescripcion(descripcion);
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setUsuarioModifica(username);
        this.categoriaRepository.save(entity);
    }

    @Override
    public void changeStatus(UUID id, String username) {
        CategoriaEntity entity = this.categoriaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setUsuarioModifica(username);
        entity.setEstado(!entity.isEstado());
        this.categoriaRepository.save(entity);
    }

    @Override
    public Categoria findById(UUID id) {
        return categoriaMapper.categoriaEntityToCategoria(this.categoriaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada")));
    }
}
