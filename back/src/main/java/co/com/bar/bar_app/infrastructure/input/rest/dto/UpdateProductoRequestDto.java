package co.com.bar.bar_app.infrastructure.input.rest.dto;

import java.util.UUID;

public record UpdateProductoRequestDto(
        String nombre,
        String descripcion,
        int stock,
        int stockMinimo,
        double precio,
        boolean destacado,
        UUID idCategoria,
        UUID idMarca,
        boolean estado
) {
}
