package co.com.bar.bar_app.application.dto;

import java.util.UUID;

public record UpdateProductoDto(
        String nombre,
        String descripcion,
        int stock,
        int stockMinimo,
        double precio,
        boolean destacado,
        UUID idCategoria,
        UUID idMarca,
        Boolean estado
) {
}
