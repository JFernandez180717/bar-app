package co.com.bar.bar_app.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CrearIngresoMercanciaDto(
    LocalDateTime fecha,
    String usuarioRecibe,
    String idProveedor,
    List<IngresoMercanciaDetalleDto> detalles
) {}