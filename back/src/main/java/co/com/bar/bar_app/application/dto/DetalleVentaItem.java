package co.com.bar.bar_app.application.dto;

import java.util.UUID;

public record DetalleVentaItem(
        UUID productoId,
        int cantidad,
        double precioUnitario,
        double descuento
) {
}