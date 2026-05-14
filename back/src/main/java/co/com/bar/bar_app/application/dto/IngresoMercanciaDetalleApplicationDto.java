package co.com.bar.bar_app.application.dto;

import java.util.UUID;

public record IngresoMercanciaDetalleApplicationDto(
    UUID idProducto,
    String nombreProducto,
    int cantidad,
    double precio
) {}