package co.com.bar.bar_app.application.dto;

import java.util.UUID;

public record CrearProductoDto(
        String nombre,
        String descripcion,
        int stock,
        int stockMinimo,
        double precio,
        String imagen,
        UUID idCategoria,
        UUID idMarca,
        boolean destacado
) {
}
