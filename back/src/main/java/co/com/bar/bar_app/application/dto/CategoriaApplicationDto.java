package co.com.bar.bar_app.application.dto;

import java.util.UUID;

public record CategoriaApplicationDto(
        UUID id,
        String descripcion,
        boolean activo
) {
}
