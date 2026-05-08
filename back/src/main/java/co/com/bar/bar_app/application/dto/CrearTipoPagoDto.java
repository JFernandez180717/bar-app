package co.com.bar.bar_app.application.dto;

public record CrearTipoPagoDto(
        String descripcion,
        boolean esEfectivo,
        boolean esTransferencia,
        boolean esTarjetaDebito,
        boolean esTarjetaCredito
) {
}
