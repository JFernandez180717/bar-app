package co.com.bar.bar_app.domain.exception;

import java.util.ArrayList;
import java.util.List;

public class TipoPagoNotValidException extends RuntimeException {
    private List<String> errors = new ArrayList<>();
    public TipoPagoNotValidException(List<String> errors) {
        super("Bad request: ");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
