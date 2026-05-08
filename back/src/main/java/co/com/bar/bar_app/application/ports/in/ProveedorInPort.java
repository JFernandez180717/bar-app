package co.com.bar.bar_app.application.ports.in;

import co.com.bar.bar_app.application.dto.ActualizarProveedorDto;
import co.com.bar.bar_app.application.dto.ProveedorApplicationDto;
import co.com.bar.bar_app.application.dto.ProveedorCreadoDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearProveedorRequestDto;

import java.util.List;

public interface ProveedorInPort {
    ProveedorCreadoDto create(CrearProveedorRequestDto requestDto, String username);
    List<ProveedorApplicationDto> findAll();
    ProveedorApplicationDto update(String identificacion, ActualizarProveedorDto dto, String username);
}
