package co.com.bar.bar_app.application.dto;

public record ProveedorApplicationDto(
        String identificacion,
        String tipoIdentificacion,
        String nombre,
        String direccion,
        String telefono,
        boolean estado
) {
}
