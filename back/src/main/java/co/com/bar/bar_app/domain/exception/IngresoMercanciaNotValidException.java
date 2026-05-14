package co.com.bar.bar_app.domain.exception;

import java.util.List;

public class IngresoMercanciaNotValidException extends RuntimeException {
    private final List<String> errores;

    public IngresoMercanciaNotValidException(List<String> errores) {
        super("Bad Request");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}