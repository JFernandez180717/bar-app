package co.com.bar.bar_app.infrastructure.input.rest.dto;

import java.util.UUID;

public record MarcaResponseDto(
        UUID id,
        String descripcion,
        Integer estado
) {
}
