package co.com.bar.bar_app.application.dto;

import java.util.UUID;

public record ProductoCreadoDto(
        UUID id,
        String nombre,
        String descripcion,
        int stock,
        int stockMinino,
        double precio,
        String imangen,
        boolean destacado,
        boolean activo
) {
}
