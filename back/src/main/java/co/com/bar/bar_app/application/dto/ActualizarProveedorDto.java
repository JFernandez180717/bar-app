package co.com.bar.bar_app.application.dto;

public record ActualizarProveedorDto(
        String identificacion,
        String tipoIdentificacion,
        String nombre,
        String direccion,
        String telefono,
        Boolean estado
) {
}