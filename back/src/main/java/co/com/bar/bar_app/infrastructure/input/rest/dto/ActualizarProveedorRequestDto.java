package co.com.bar.bar_app.infrastructure.input.rest.dto;

public record ActualizarProveedorRequestDto(
        String identificacion,
        String tipoIdentificacion,
        String nombre,
        String direccion,
        String telefono,
        Boolean estado
) {
}