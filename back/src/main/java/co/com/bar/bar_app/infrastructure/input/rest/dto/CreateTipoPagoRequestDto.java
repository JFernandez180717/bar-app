package co.com.bar.bar_app.infrastructure.input.rest.dto;

public record CreateTipoPagoRequestDto(
        String descripcion,
        boolean esEfectivo,
        boolean esTransferencia,
        boolean esTarjetaDebito,
        boolean esTarjetaCredito
) {
}
