package co.com.bar.bar_app.infrastructure.config;

import co.com.bar.bar_app.domain.exception.CategoriaNotValidException;
import co.com.bar.bar_app.domain.exception.MarcaNotValidException;
import co.com.bar.bar_app.domain.exception.ProductoNotValidException;
import co.com.bar.bar_app.domain.exception.ProveedorNotValidException;
import co.com.bar.bar_app.domain.exception.UsuarioNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        ex.printStackTrace();
        ApiResponse<Object> response = ApiResponse.builder()
                .error(true)
                .message("Error al procesar la peticion")
                .data(Collections.emptyList())
                .errors(List.of(ex.getMessage()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<Object>> handleIOException(IOException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .error(true)
                .message("Error al guardar el archivo")
                .data(Collections.emptyList())
                .errors(List.of(ex.getMessage()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsuarioNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsuarioNotValidException(UsuarioNotValidException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .error(true)
                .message("Error de validación del usuario")
                .data(Collections.emptyList())
                .errors(ex.getErrores())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoriaNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleCategoriaNotValidException(CategoriaNotValidException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .error(true)
                .message("Error de validación de la categoria")
                .data(Collections.emptyList())
                .errors(ex.getErrores())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MarcaNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMarcaNotValidException(MarcaNotValidException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .error(true)
                .message("Error de validación de la marca")
                .data(Collections.emptyList())
                .errors(ex.getErrores())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductoNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleProductoNotValidException(ProductoNotValidException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .error(true)
                .message("Error de validación del producto")
                .data(Collections.emptyList())
                .errors(ex.getErrores())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProveedorNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleProveedorNotValidException(ProveedorNotValidException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .error(true)
                .message("Error de validación del proveedor")
                .data(Collections.emptyList())
                .errors(ex.getErrores())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                // Ejemplo: "nombre: El campo no puede estar vacío"
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ApiResponse<Object> response = ApiResponse.builder()
                .error(true)
                .message("Error de validación en los campos")
                .data(Collections.emptyList())
                .errors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
