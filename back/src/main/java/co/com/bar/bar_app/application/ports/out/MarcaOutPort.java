package co.com.bar.bar_app.application.ports.out;

import co.com.bar.bar_app.domain.model.Marca;

import java.util.List;
import java.util.UUID;

public interface MarcaOutPort {
    void create(Marca marca, String username);
    List<Marca> findAll();
    void update(UUID id, String descripcion, Integer estado, String username);
    void changeStatus(UUID id, String username);
    Marca findById(UUID id);
}
