package co.com.bar.bar_app.application.dto;

import java.util.List;

public record UsuarioApplicationDto(
    String username,
    String email,
    String primerNombre,
    String segundoNombre,
    String primerApellido,
    String segundoApellido,
    String tipoIdentificacion,
    String identificacion,
    String direccion,
    String telefono,
    boolean activo,
    List<String> roles
) {
}
