package co.com.bar.bar_app.infrastructure.input.rest.dto;

public record ActualizarUsuarioRequestDto(
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
