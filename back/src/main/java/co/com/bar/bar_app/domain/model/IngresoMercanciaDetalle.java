package co.com.bar.bar_app.domain.model;

import co.com.bar.bar_app.domain.exception.IngresoMercanciaNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngresoMercanciaDetalle {
    private final UUID idProducto;
    private final int cantidad;
    private final double precio;

    public IngresoMercanciaDetalle(UUID idProducto, int cantidad, double precio) {
        List<String> errores = new ArrayList<>();
        validarIdProducto(idProducto, errores);
        validarCantidad(cantidad, errores);
        validarPrecio(precio, errores);

        if (!errores.isEmpty()) throw new IngresoMercanciaNotValidException(errores);

        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public static IngresoMercanciaDetalle crear(UUID idProducto, int cantidad, double precio) {
        return new IngresoMercanciaDetalle(idProducto, cantidad, precio);
    }

    private void validarIdProducto(UUID idProducto, List<String> errores) {
        if (idProducto == null) {
            errores.add("El ID del producto no puede ser nulo");
        }
    }

    private void validarCantidad(int cantidad, List<String> errores) {
        if (cantidad <= 0) {
            errores.add("La cantidad debe ser mayor a 0");
        }
    }

    private void validarPrecio(double precio, List<String> errores) {
        if (precio <= 0) {
            errores.add("El precio debe ser mayor a 0");
        }
    }

    public UUID getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }
}