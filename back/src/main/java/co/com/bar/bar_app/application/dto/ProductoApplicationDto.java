package co.com.bar.bar_app.application.dto;

import java.util.UUID;

public record ProductoApplicationDto(
        UUID id,
        String nombre,
        String descripcion,
        int stock,
        int stockMinimo,
        double precio,
        String imagen,
        boolean destacado,
        boolean activo,
        CategoriaApplicationDto categoria,
        MarcaApplicationDto marca
) {
}
