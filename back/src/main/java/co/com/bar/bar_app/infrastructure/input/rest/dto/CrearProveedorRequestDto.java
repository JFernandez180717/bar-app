package co.com.bar.bar_app.infrastructure.input.rest.dto;

public record CrearProveedorRequestDto(
        String identificacion,
        String tipoIdentificacion,
        String nombre,
        String direccion,
        String telefono
) {
}
