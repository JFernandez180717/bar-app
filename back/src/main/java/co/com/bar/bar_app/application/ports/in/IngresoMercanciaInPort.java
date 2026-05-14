package co.com.bar.bar_app.application.ports.in;

import co.com.bar.bar_app.application.dto.CrearIngresoMercanciaDto;
import co.com.bar.bar_app.application.dto.IngresoMercanciaApplicationDto;
import co.com.bar.bar_app.application.dto.IngresoMercanciaCreadoDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IngresoMercanciaInPort {
    IngresoMercanciaCreadoDto create(CrearIngresoMercanciaDto dto, String username);
    List<IngresoMercanciaApplicationDto> findAll(LocalDate fechaInicio, LocalDate fechaFin, String idProveedor);
    IngresoMercanciaApplicationDto findById(UUID id);
    IngresoMercanciaApplicationDto anular(UUID id, String username, String rol);
}