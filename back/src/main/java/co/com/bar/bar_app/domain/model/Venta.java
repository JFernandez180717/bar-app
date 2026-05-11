package co.com.bar.bar_app.domain.model;

import co.com.bar.bar_app.domain.exception.VentaNotValidException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Venta {
    private final int codigo;
    private final LocalDateTime fecha;
    private final double total;
    private final double totalDescuento;
    private final String usuario;
    private final TipoPago tipoPago;
    private final Mesa mesa;
    private final boolean activo;
    private final List<VentaDetalle> detalles;

    // Constructor público para MapStruct (con todos los campos, incluyendo total calculado)
    public Venta(int codigo, LocalDateTime fecha, double total, double totalDescuento, String usuario, TipoPago tipoPago, Mesa mesa, boolean activo, List<VentaDetalle> detalles) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.total = total;
        this.totalDescuento = totalDescuento;
        this.usuario = usuario;
        this.tipoPago = tipoPago;
        this.mesa = mesa;
        this.activo = activo;
        this.detalles = detalles;
    }

    // Constructor privado para validación de negocio
    private Venta(List<VentaDetalle> detalles, double totalDescuento, String usuario, TipoPago tipoPago, Mesa mesa) {
        List<String> errores = new ArrayList<>();
        validarFecha(LocalDateTime.now(), errores);
        validarUsuario(usuario, errores);
        validarTipoPago(tipoPago, errores);
        validarMesa(mesa, errores);
        validarDetalles(detalles, errores);

        if (!errores.isEmpty()) throw new VentaNotValidException(errores);

        double total = detalles.stream()
                .mapToDouble(d -> (d.getPrecioUnitario() * d.getCantidad()) - d.getDescuento())
                .sum();

        this.codigo = 0;
        this.fecha = LocalDateTime.now();
        this.total = total;
        this.totalDescuento = totalDescuento;
        this.usuario = usuario;
        this.tipoPago = tipoPago;
        this.mesa = mesa;
        this.activo = true;
        this.detalles = detalles;
    }

    private void validarFecha(LocalDateTime fecha, List<String> errores) {
        if (fecha == null) errores.add("La fecha no puede ser null");
    }

    private void validarUsuario(String usuario, List<String> errores) {
        if (usuario == null || usuario.isEmpty()) errores.add("El usuario no puede estar vacio");
    }

    private void validarTotal(double total, List<String> errores) {
        if (total <= 0) errores.add("El total debe ser mayor que 0");
    }

    private void validarTipoPago(TipoPago tipoPago, List<String> errores) {
        if (tipoPago == null) errores.add("El tipo de pago no puede ser null");
    }

    private void validarMesa(Mesa mesa, List<String> errores) {
        if (mesa == null) errores.add("La mesa no puede ser null");
    }

    private void validarDetalles(List<VentaDetalle> detalles, List<String> errores) {
        if (detalles == null || detalles.isEmpty()) errores.add("Los detalles de la venta no pueden estar vacios");
    }

    // Método fábrica para creación de negocio (valida y calcula total)
    public static Venta crear(List<VentaDetalle> detalles, double totalDescuento, String usuario, TipoPago tipoPago, Mesa mesa) {
        return new Venta(detalles, totalDescuento, usuario, tipoPago, mesa);
    }
}