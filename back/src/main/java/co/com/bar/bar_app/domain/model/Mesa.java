package co.com.bar.bar_app.domain.model;

import lombok.Getter;

@Getter
public class Mesa {
    private final int id;
    private final boolean activo;

    public Mesa(int id, boolean activo) {
        this.id = id;
        this.activo = activo;
    }
}
