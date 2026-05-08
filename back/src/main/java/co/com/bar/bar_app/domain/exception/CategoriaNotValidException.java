package co.com.bar.bar_app.domain.exception;

import java.util.List;

public class CategoriaNotValidException extends RuntimeException {
    private final List<String> errores;

    public CategoriaNotValidException(List<String> errores) {
        super("Bad request");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
