package co.com.bar.bar_app.application.ports.out;

import co.com.bar.bar_app.domain.model.Categoria;

import java.util.List;
import java.util.UUID;

public interface CategoriaOutPort {
    Categoria create(Categoria categoria, String username);
    List<Categoria> findAll();
    void update(UUID id, String descripcion, String username);
    void changeStatus(UUID id, String username);
    Categoria findById(UUID id);
}
