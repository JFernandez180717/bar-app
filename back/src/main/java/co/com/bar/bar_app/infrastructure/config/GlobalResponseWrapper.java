package co.com.bar.bar_app.infrastructure.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Obtenemos el contexto de la petición actual
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String path = attributes.getRequest().getRequestURI();

            // Excluimos las rutas comunes de Swagger y OpenAPI
            return !path.contains("/v3/api-docs") &&
                    !path.contains("/swagger-ui") &&
                    !path.contains("/swagger-resources") &&
                    !path.contains("/check");
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ){

        if (body instanceof ApiResponse || body instanceof String) {
            return body;
        }

        return ApiResponse.builder()
                .error(false)
                .message("Operacion realizada exitosamente")
                .data(body)
                .errors(Collections.emptyList())
                .build();
    }
}
