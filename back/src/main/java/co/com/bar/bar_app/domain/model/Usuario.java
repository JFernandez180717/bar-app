package co.com.bar.bar_app.domain.model;

import co.com.bar.bar_app.domain.exception.UsuarioNotValidException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Usuario {
    private final String username;
    private final String email;
    private final String pass;
    private final String primerNombre;
    private final String segundoNombre;
    private final String primerApellido;
    private final String segundoApellido;
    private final String tipoIdentificacion;
    private final String identificacion;
    private final String direccion;
    private final String telefono;
    private final boolean activo;
    private final List<Rol> roles;

    private Usuario(Builder builder) {
        List<String> errores = new ArrayList<>();
        validarUsername(builder.username, errores);
        validarEmail(builder.email, errores);
        validarNombres(builder.primerNombre, builder.segundoNombre, errores);
        validarApellidos(builder.primerApellido, builder.segundoApellido, errores);
        validarIdentificacion(builder.tipoIdentificacion, builder.identificacion, errores);
        validarContacto(builder.direccion, builder.telefono, errores);
        validarRoles(builder.roles, errores);

        if (!errores.isEmpty()) throw new UsuarioNotValidException(errores);

        this.username = builder.username;
        this.email = builder.email;
        this.pass = builder.pass;
        this.primerNombre = builder.primerNombre;
        this.segundoNombre = builder.segundoNombre;
        this.primerApellido = builder.primerApellido;
        this.segundoApellido = builder.segundoApellido;
        this.tipoIdentificacion = builder.tipoIdentificacion;
        this.identificacion = builder.identificacion;
        this.direccion = builder.direccion;
        this.telefono = builder.telefono;
        this.activo = builder.activo;
        this.roles = List.copyOf(builder.roles);
    }

    // métodos de validación privados
    private void validarUsername(String username, List<String> errores) {
        if (username == null || username.isEmpty()) errores.add("El nombre de usuario no puede estar vacio");
        else if (username.length() > 30) errores.add("El nombre de usuario es demasiado largo");
    }

    private void validarEmail(String email, List<String> errores) {
        if (email == null || email.isEmpty()) errores.add("El email no puede estar vacio");
        else if (email.length() > 100) errores.add("El email es demasiado largo");
    }

    private void validarNombres(String primerNombre, String segundoNombre, List<String> errores) {
        if (primerNombre == null || primerNombre.isEmpty()) errores.add("El primer nombre no puede estar vacio");
        else if (primerNombre.length() > 40) errores.add("El primer nombre es demasiado largo");
        if (segundoNombre != null && segundoNombre.length() > 40) errores.add("El segundo nombre es demasiado largo");
    }

    private void validarApellidos(String primerApellido, String segundoApellido, List<String> errores) {
        if (primerApellido == null || primerApellido.isEmpty()) errores.add("El primer apellido no puede estar vacio");
        else if (primerApellido.length() > 40) errores.add("El primer apellido es demasiado largo");
        if (segundoApellido != null && segundoApellido.length() > 40) errores.add("El segundo apellido es demasiado largo");
    }

    private void validarIdentificacion(String tipoIdentificacion, String identificacion, List<String> errores) {
        if (tipoIdentificacion == null || tipoIdentificacion.isEmpty()) errores.add("El tipo de identificacion no puede estar vacio");
        if (identificacion == null || identificacion.isEmpty()) errores.add("El numero de identificacion no puede estar vacio");
        else if (identificacion.length() > 20) errores.add("El numero de identificacion es demasiado largo");
    }

    private void validarContacto(String direccion, String telefono, List<String> errores) {
        if (direccion != null && direccion.length() > 200) errores.add("La direccion es demasiado larga");
        if (telefono != null && telefono.length() > 20) errores.add("El numero de telefono es demasiado largo");
    }

    private void validarRoles(List<Rol> roles, List<String> errores) {
        if (roles == null || roles.isEmpty()) errores.add("No se asignó ningun rol al usuario");
    }

    public static Builder builder() {
        return new Builder();
    }

    // Builder
    public static class Builder {
        private String username;
        private String email;
        private String pass;
        private String primerNombre;
        private String segundoNombre;
        private String primerApellido;
        private String segundoApellido;
        private String tipoIdentificacion;
        private String identificacion;
        private String direccion;
        private String telefono;
        private boolean activo;
        private List<Rol> roles;

        public Builder username(String username) { this.username = username; return this;}
        public Builder email(String email) { this.email = email; return this; }
        public Builder pass(String pass) { this.pass = pass; return this; }
        public Builder primerNombre(String primerNombre) { this.primerNombre = primerNombre; return this; }
        public Builder segundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; return this; }
        public Builder primerApellido(String primerApellido) { this.primerApellido = primerApellido; return this; }
        public Builder segundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; return this; }
        public Builder tipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; return this; }
        public Builder identificacion(String identificacion) { this.identificacion = identificacion; return this; }
        public Builder direccion(String direccion) { this.direccion = direccion; return this; }
        public Builder telefono(String telefono) { this.telefono = telefono; return this; }
        public Builder activo(boolean activo) { this.activo = activo; return this; }
        public Builder roles(List<Rol> roles) { this.roles = roles; return this; }

        public Usuario build() { return new Usuario(this); }
    }
}