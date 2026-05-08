package co.com.bar.bar_app.application.dto;

public record CrearUsuarioResponseDto(
        String username,
        String email,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        String tipoIdentificacion,
        String identificacion,
        String direccion,
        String telefono
) {
}
