package co.com.bar.bar_app.infrastructure.input.rest.dto;

import java.util.UUID;

public record IngresoMercanciaDetalleResponseDto(
    UUID idProducto,
    String nombreProducto,
    int cantidad,
    double precio
) {}