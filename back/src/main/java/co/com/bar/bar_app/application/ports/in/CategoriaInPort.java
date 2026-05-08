package co.com.bar.bar_app.application.ports.in;

import co.com.bar.bar_app.application.dto.CategoriaApplicationDto;
import co.com.bar.bar_app.application.dto.CategoriaCreadaDto;

import java.util.List;
import java.util.UUID;

public interface CategoriaInPort {
    CategoriaCreadaDto create(String descripcion, String username);
    List<CategoriaApplicationDto> findAll();
    void update(UUID id, String descripcion, String username);
    void changeStatus(UUID id, String username);
}
