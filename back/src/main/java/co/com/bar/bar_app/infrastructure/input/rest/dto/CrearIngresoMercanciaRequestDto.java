package co.com.bar.bar_app.infrastructure.input.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CrearIngresoMercanciaRequestDto(
    LocalDateTime fecha,
    String usuarioRecibe,
    String idProveedor,
    List<IngresoMercanciaDetalleRequestDto> detalles
) {}