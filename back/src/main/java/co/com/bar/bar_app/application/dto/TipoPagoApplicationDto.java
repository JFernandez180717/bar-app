package co.com.bar.bar_app.application.dto;

import java.util.UUID;

public record TipoPagoApplicationDto(
        UUID id,
        String descripcion,
        boolean esEfectivo,
        boolean esTransferencia,
        boolean esTarjetaDebito,
        boolean esTarjetaCredito,
        boolean activo
) {
}
