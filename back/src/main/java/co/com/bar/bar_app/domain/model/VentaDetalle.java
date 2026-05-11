package co.com.bar.bar_app.domain.model;

import co.com.bar.bar_app.domain.exception.VentaDetalleNotValidException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class VentaDetalle {
    private final Producto producto;
    private final int cantidad;
    private final double precioUnitario;
    private final double descuento;
    private final double total;

    // Constructor público para creación de negocio (calcula total)
    public VentaDetalle(Producto producto, int cantidad, double precioUnitario, double descuento) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
        this.total = (precioUnitario * cantidad) - descuento;
    }

    // Constructor package-private para MapStruct (acepta total calculado de la entity)
    VentaDetalle(Producto producto, int cantidad, double precioUnitario, double descuento, double total) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
        this.total = total;
    }

    public static VentaDetalle crear(Producto producto, int cantidad, double precioUnitario, double descuento) {
        return new VentaDetalle(producto, cantidad, precioUnitario, descuento);
    }
}