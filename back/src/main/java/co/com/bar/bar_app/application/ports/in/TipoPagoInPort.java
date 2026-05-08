package co.com.bar.bar_app.application.ports.in;

import co.com.bar.bar_app.application.dto.CrearTipoPagoDto;
import co.com.bar.bar_app.application.dto.TipoPagoApplicationDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.UpdateTipoPagoRequestDto;

import java.util.List;
import java.util.UUID;

public interface TipoPagoInPort {
    TipoPagoApplicationDto create(CrearTipoPagoDto dto, String username);
    List<TipoPagoApplicationDto> findAll();
    void update(UUID id, UpdateTipoPagoRequestDto requestDto, String username);
}
