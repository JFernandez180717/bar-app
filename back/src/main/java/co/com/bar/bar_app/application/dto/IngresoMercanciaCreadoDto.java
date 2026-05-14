package co.com.bar.bar_app.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record IngresoMercanciaCreadoDto(
    UUID id,
    LocalDateTime fecha,
    String usuarioRecibe,
    String idProveedor,
    int estado
) {}