package co.com.bar.bar_app.application.dto;

import java.util.List;
import java.util.UUID;

public record CrearVentaDto(
        int mesaId,
        UUID tipoPagoId,
        double totalDescuento,
        List<DetalleVentaItem> items
) {
}