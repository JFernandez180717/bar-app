package co.com.bar.bar_app.infrastructure.input.rest.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CrearProductoRequestDto(
        String nombre,
        String descripcion,
        int stock,
        int stockMinino,
        double precio,
        String imagen,
        boolean destacado,
        UUID idCategoria,
        UUID idMarca,
        MultipartFile archivoImagen
) {
}
