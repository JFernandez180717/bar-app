package co.com.bar.bar_app.infrastructure.input.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record IngresoMercanciaResponseDto(
    UUID id,
    LocalDateTime fecha,
    String usuarioRecibe,
    String nombreProveedor,
    int estado,
    List<IngresoMercanciaDetalleResponseDto> detalles
) {}