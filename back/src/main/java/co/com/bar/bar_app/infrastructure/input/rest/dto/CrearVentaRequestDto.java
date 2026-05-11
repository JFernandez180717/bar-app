package co.com.bar.bar_app.infrastructure.input.rest.dto;

import java.util.List;
import java.util.UUID;

public record CrearVentaRequestDto(
        int mesaId,
        UUID tipoPagoId,
        double totalDescuento,
        List<DetalleVentaItemRequest> items
) {
}