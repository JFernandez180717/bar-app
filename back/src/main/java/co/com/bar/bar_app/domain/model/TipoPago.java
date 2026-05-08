package co.com.bar.bar_app.domain.model;

import co.com.bar.bar_app.domain.exception.TipoPagoNotValidException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
public class TipoPago {
    private final UUID id;
    private final String descripcion;
    private final boolean esEfectivo;
    private final boolean esTransferencia;
    private final boolean esTarjetaDebito;
    private final boolean esTarjetaCredito;
    private final boolean activo;

    public TipoPago(UUID id, String descripcion, boolean esEfectivo, boolean esTransferencia, boolean esTarjetaDebito, boolean esTarjetaCredito, boolean activo) {
        List<String> errors = new ArrayList<>();
        validateDescripcion(descripcion, errors);
        validatePaymentMethods(esEfectivo, esTransferencia, esTarjetaDebito, esTarjetaCredito, errors);
        if (!errors.isEmpty()) throw new TipoPagoNotValidException(errors);

        this.id = id;
        this.descripcion = descripcion;
        this.esEfectivo = esEfectivo;
        this.esTransferencia = esTransferencia;
        this.esTarjetaDebito = esTarjetaDebito;
        this.esTarjetaCredito = esTarjetaCredito;
        this.activo = activo;
    }

    public static TipoPago create(String descripcion, boolean esEfectivo, boolean esTransferencia, boolean esTarjetaDebito, boolean esTarjetaCredito) {
        return new TipoPago(UUID.randomUUID(), descripcion, esEfectivo, esTransferencia, esTarjetaDebito, esTarjetaCredito, true);
    }

    private void validateDescripcion(String descripcion, List<String> errors) {
        if (descripcion == null || descripcion.isEmpty()) errors.add("La descripción no puede estar vacia");
        else if (descripcion.length() > 100) errors.add("La descripción es demasiado larga");
    }

    private void validatePaymentMethods(boolean esEfectivo, boolean esTransferencia, boolean esTarjetaDebito, boolean esTarjetaCredito, List<String> errors) {
        if (esEfectivo && esTransferencia && esTarjetaDebito && esTarjetaCredito) errors.add("El tipo de pago solo puede pertenecer a un solo metodo de pago.");
        else if (!esEfectivo && !esTransferencia && !esTarjetaDebito && !esTarjetaCredito) errors.add("El tipo de pago debe pertenecer a un metodo de pago.");
        long count = Stream.of(esEfectivo, esTransferencia, esTarjetaDebito, esTarjetaCredito)
                .filter(val -> val)
                .count();
        if (count > 1) errors.add("El tipo de pago solo puede pertenecer a un solo metodo de pago.");
    }

}
