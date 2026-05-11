package co.com.bar.bar_app.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VentaApplicationDto(
        int codigo,
        LocalDateTime fecha,
        double total,
        double totalDescuento,
        String usuario,
        TipoPagoApplicationDto tipoPago,
        int mesaId,
        List<VentaDetalleApplicationDto> detalles
) {
}