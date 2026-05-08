package co.com.bar.bar_app.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Rol {
    private final String rol;
    private final String descripcion;
    private final boolean estaActivo;
}
