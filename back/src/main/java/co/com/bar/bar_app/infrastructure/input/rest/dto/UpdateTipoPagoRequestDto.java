package co.com.bar.bar_app.infrastructure.input.rest.dto;

public record UpdateTipoPagoRequestDto(
        String descripcion,
        boolean esEfectivo,
        boolean esTransferencia,
        boolean esTarjetaDebito,
        boolean esTarjetaCredito,
        boolean activo
) {
}
