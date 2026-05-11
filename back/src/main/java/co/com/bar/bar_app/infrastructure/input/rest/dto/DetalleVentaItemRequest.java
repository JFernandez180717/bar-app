package co.com.bar.bar_app.infrastructure.input.rest.dto;

import java.util.UUID;

public record DetalleVentaItemRequest(
        UUID productoId,
        int cantidad,
        double precioUnitario,
        double descuento
) {
}