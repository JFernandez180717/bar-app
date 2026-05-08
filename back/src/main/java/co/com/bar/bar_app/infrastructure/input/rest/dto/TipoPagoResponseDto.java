package co.com.bar.bar_app.infrastructure.input.rest.dto;

import java.util.UUID;

public record TipoPagoResponseDto(
        UUID id,
        String descripcion,
        boolean esEfectivo,
        boolean esTransferencia,
        boolean esTarjetaDebito,
        boolean esTarjetaCredito,
        boolean activo
) {
}
