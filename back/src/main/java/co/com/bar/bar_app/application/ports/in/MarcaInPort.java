package co.com.bar.bar_app.application.ports.in;

import co.com.bar.bar_app.application.dto.MarcaApplicationDto;
import co.com.bar.bar_app.application.dto.MarcaCreadaDto;
import co.com.bar.bar_app.application.dto.ActualizarMarcaDto;

import java.util.List;
import java.util.UUID;

public interface MarcaInPort {
    MarcaCreadaDto create(String descripcion, String username);
    List<MarcaApplicationDto> findAll();
    void update(UUID id, ActualizarMarcaDto dto, String username);
    void changeStatus(UUID id, String username);
}
