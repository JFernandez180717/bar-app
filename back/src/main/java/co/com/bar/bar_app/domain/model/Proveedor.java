package co.com.bar.bar_app.domain.model;

import co.com.bar.bar_app.domain.exception.ProveedorNotValidException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Proveedor {
    private final String identificacion;
    private final String tipoIdentificacion;
    private final String nombre;
    private final String direccion;
    private final String telefono;
    private final boolean estado;

    private Proveedor(Builder builder) {
        List<String> errores = new ArrayList<>();
        validarIdentificacion(builder.identificacion, errores);
        validarTipoIdentificacion(builder.tipoIdentificacion, errores);
        validarNombre(builder.nombre, errores);
        validarDireccion(builder.direccion, errores);
        validarTelefono(builder.telefono, errores);

        if (!errores.isEmpty()) throw new ProveedorNotValidException(errores);

        this.identificacion = builder.identificacion;
        this.tipoIdentificacion = builder.tipoIdentificacion;
        this.nombre = builder.nombre;
        this.direccion = builder.direccion;
        this.telefono = builder.telefono;
        this.estado = builder.estado;
    }

    private void validarIdentificacion(String identificacion, List<String> errores) {
        if (identificacion == null || identificacion.isEmpty()) {
            errores.add("La identificacion no puede estar vacia");
        } else if (identificacion.length() > 20) {
            errores.add("La identificacion es demasiado larga (max 20 caracteres)");
        }
    }

    private void validarTipoIdentificacion(String tipoIdentificacion, List<String> errores) {
        if (tipoIdentificacion == null || tipoIdentificacion.isEmpty()) {
            errores.add("El tipo de identificacion no puede estar vacio");
        }
    }

    private void validarNombre(String nombre, List<String> errores) {
        if (nombre == null || nombre.isEmpty()) {
            errores.add("El nombre no puede estar vacio");
        } else if (nombre.length() > 250) {
            errores.add("El nombre es demasiado largo (max 250 caracteres)");
        }
    }

    private void validarDireccion(String direccion, List<String> errores) {
        if (direccion != null && direccion.length() > 200) {
            errores.add("La direccion es demasiado larga (max 200 caracteres)");
        }
    }

    private void validarTelefono(String telefono, List<String> errores) {
        if (telefono != null && telefono.length() > 20) {
            errores.add("El telefono es demasiado largo (max 20 caracteres)");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String identificacion;
        private String tipoIdentificacion;
        private String nombre;
        private String direccion;
        private String telefono;
        private boolean estado;

        public Builder identificacion(String identificacion) { this.identificacion = identificacion; return this; }
        public Builder tipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder direccion(String direccion) { this.direccion = direccion; return this; }
        public Builder telefono(String telefono) { this.telefono = telefono; return this; }
        public Builder estado(boolean estado) { this.estado = estado; return this; }

        public Proveedor build() {
            return new Proveedor(this);
        }
    }
}
