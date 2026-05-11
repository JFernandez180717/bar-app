package co.com.bar.bar_app.domain.exception;

import java.util.List;

public class VentaNotValidException extends RuntimeException {
    List<String> errores;

    public VentaNotValidException(List<String> errores) {
        super("Bad request");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}