package co.com.bar.bar_app.domain.model;

import co.com.bar.bar_app.domain.exception.MarcaNotValidException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Marca {
    private final UUID id;
    private final String descripcion;
    private final boolean activo;

    public Marca(UUID id, String descripcion, boolean activo) {
        List<String> errores = new ArrayList<>();
        validarDescripcion(descripcion, errores);

        if (!errores.isEmpty()) throw new MarcaNotValidException(errores);

        this.id = id;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public static Marca crear(String descripcion) {
        return new Marca(UUID.randomUUID(), descripcion, true);
    }

    private void validarDescripcion(String descripcion, List<String> errores) {
        if (descripcion == null || descripcion.isEmpty()) errores.add("La descripcion no puede estar vacia");
        else if (descripcion.length() > 100) errores.add("La descripcion es demasiado larga");
    }
}
