package co.com.bar.bar_app.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record IngresoMercanciaApplicationDto(
    UUID id,
    LocalDateTime fecha,
    String usuarioRecibe,
    String nombreProveedor,
    int estado,
    List<IngresoMercanciaDetalleApplicationDto> detalles
) {}