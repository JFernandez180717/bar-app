package co.com.bar.bar_app.infrastructure.input.rest.dto;

public record CrearUsuarioRequestDto(
        String username,
        String pass,
        String email,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        String tipoIdentificacion,
        String identificacion,
        String direccion,
        String telefono,
        String[] roles
) {
}
