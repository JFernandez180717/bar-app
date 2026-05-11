package co.com.bar.bar_app.application.dto;

import java.util.UUID;

public record VentaDetalleApplicationDto(
        UUID productoId,
        String nombreProducto,
        int cantidad,
        double precioUnitario,
        double descuento,
        double total
) {
}