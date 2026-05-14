package co.com.bar.bar_app.domain.model;

import co.com.bar.bar_app.domain.exception.IngresoMercanciaNotValidException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngresoMercancia {
    private final UUID id;
    private final LocalDateTime fecha;
    private final String usuarioRecibe;
    private final String idProveedor;
    private final boolean estado;
    private final List<IngresoMercanciaDetalle> detalles;

    private IngresoMercancia(Builder builder) {
        List<String> errores = new ArrayList<>();
        validarFecha(builder.fecha, errores);
        validarUsuarioRecibe(builder.usuarioRecibe, errores);
        validarDetalles(builder.detalles, errores);

        if (!errores.isEmpty()) throw new IngresoMercanciaNotValidException(errores);

        this.id = builder.id;
        this.fecha = builder.fecha;
        this.usuarioRecibe = builder.usuarioRecibe;
        this.idProveedor = builder.idProveedor;
        this.estado = builder.estado;
        this.detalles = builder.detalles;
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validarFecha(LocalDateTime fecha, List<String> errores) {
        if (fecha == null) {
            errores.add("La fecha no puede ser nula");
        }
    }

    private void validarUsuarioRecibe(String usuarioRecibe, List<String> errores) {
        if (usuarioRecibe == null || usuarioRecibe.isEmpty()) {
            errores.add("El usuario que recibe no puede estar vacío");
        }
    }

    private void validarDetalles(List<IngresoMercanciaDetalle> detalles, List<String> errores) {
        if (detalles == null || detalles.isEmpty()) {
            errores.add("El ingreso debe tener al menos un detalle");
        }
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getUsuarioRecibe() {
        return usuarioRecibe;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public boolean isEstado() {
        return estado;
    }

    public List<IngresoMercanciaDetalle> getDetalles() {
        return detalles;
    }

    public static class Builder {
        private UUID id;
        private LocalDateTime fecha;
        private String usuarioRecibe;
        private String idProveedor;
        private boolean estado = true;
        private List<IngresoMercanciaDetalle> detalles;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder fecha(LocalDateTime fecha) { this.fecha = fecha; return this; }
        public Builder usuarioRecibe(String usuarioRecibe) { this.usuarioRecibe = usuarioRecibe; return this; }
        public Builder idProveedor(String idProveedor) { this.idProveedor = idProveedor; return this; }
        public Builder estado(boolean estado) { this.estado = estado; return this; }
        public Builder detalles(List<IngresoMercanciaDetalle> detalles) { this.detalles = detalles; return this; }

        public IngresoMercancia build() {
            return new IngresoMercancia(this);
        }
    }
}