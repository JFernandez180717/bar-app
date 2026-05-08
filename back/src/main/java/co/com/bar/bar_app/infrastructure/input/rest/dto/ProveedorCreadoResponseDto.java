package co.com.bar.bar_app.infrastructure.input.rest.dto;

public record ProveedorCreadoResponseDto(
        String identificacion,
        String tipoIdentificacion,
        String nombre,
        String direccion,
        String telefono,
        boolean estado
) {
}
