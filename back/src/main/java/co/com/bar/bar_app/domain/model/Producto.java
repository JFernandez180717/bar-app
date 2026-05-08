package co.com.bar.bar_app.domain.model;

import co.com.bar.bar_app.domain.exception.ProductoNotValidException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Producto {
    private final UUID id;
    private final String nombre;
    private final String descripcion;
    private final int stock;
    private final int stockMinimo;
    private final double precio;
    private final String imagen;
    private final Categoria categoria;
    private final Marca marca;
    private final boolean destacado;
    private final boolean activo;

    public Producto(UUID id, String nombre, String descripcion, int stock, int stockMinimo, double precio, String imagen, Categoria categoria, Marca marca, boolean destacado, boolean activo) {
        List<String> erroes = new ArrayList<>();
        validarNombre(nombre, erroes);
        validarDescripcion(descripcion, erroes);
        validarStock(stock, erroes);
        validarStockMinimo(stockMinimo, erroes);
        validarPrecio(precio, erroes);
        validarImagen(imagen, erroes);
        validarCategoria(categoria, erroes);
        validarMarca(marca, erroes);

        if (!erroes.isEmpty()) throw new ProductoNotValidException(erroes);

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.precio = precio;
        this.imagen = imagen;
        this.categoria = categoria;
        this.marca = marca;
        this.destacado = destacado;
        this.activo = activo;
    }

    public static Producto crear(String nombre, String descripcion, int stock, int stockMinimo, double precio, String imagen, Categoria categoria, Marca marca, boolean destacado) {
        return new Producto(UUID.randomUUID(), nombre, descripcion, stock, stockMinimo, precio, imagen, categoria, marca, destacado, true);
    }

    private void validarNombre(String nombre, List<String> errores) {
        if (nombre == null || nombre.isEmpty()) errores.add("El nombre no puede estar vacio");
        else if (nombre.length() > 50) errores.add("El nombre es demasiado largo");
    }

    private void validarDescripcion(String descripcion, List<String> errores) {
        if (descripcion == null || descripcion.isEmpty()) errores.add("La descripcion no puede estar vacia");
        else if (descripcion.length() > 100) errores.add("La descripcion es demasiado larga");
    }

    private void validarStock(int stock, List<String> errores) {
        if (stock <= 0) errores.add("La cantidad de stock no puede ser igual o menor que 0");
    }

    private void validarStockMinimo(int stockMinimo, List<String> errores) {
        if (stockMinimo < 0) errores.add("La cantidad de stock minimo no puede ser menor que 0");
    }

    private void validarPrecio(double precio, List<String> errores) {
        if (precio <= 0) errores.add("El precio no puede ser menor o igual que 0");
    }

    private void validarImagen(String imagen, List<String> errores) {
        if (imagen == null || imagen.isEmpty()) errores.add("La imagen del producto no puede estar vacia");
        else if (imagen.length() > 500) errores.add("El nombre de la imagen es demasiado largo");
    }

    private void validarCategoria(Categoria categoria, List<String> errores) {
        if (categoria == null) errores.add("La categoria del producto no puede estar vacia");
    }

    private void validarMarca(Marca marca, List<String> errores) {
        if (marca == null) errores.add("La marca del producto no puede estar vacia");
    }
}
