package co.com.bar.bar_app.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record VentaCreadaDto(
        int codigo,
        LocalDateTime fecha,
        double total,
        String usuario,
        int mesaId
) {
}