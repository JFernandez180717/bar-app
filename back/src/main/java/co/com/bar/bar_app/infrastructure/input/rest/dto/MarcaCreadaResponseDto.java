package co.com.bar.bar_app.infrastructure.input.rest.dto;

import java.util.UUID;

public record MarcaCreadaResponseDto(
        UUID id,
        String descripcion
) {
}
