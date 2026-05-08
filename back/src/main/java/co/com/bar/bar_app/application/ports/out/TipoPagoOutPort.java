package co.com.bar.bar_app.application.ports.out;

import co.com.bar.bar_app.domain.model.TipoPago;

import java.util.List;
import java.util.UUID;

public interface TipoPagoOutPort {
    void create(TipoPago tipoPago, String username);
    List<TipoPago> findAll();
    TipoPago findById(UUID id);
    void update(UUID id, TipoPago tipoPago, String username);
}
