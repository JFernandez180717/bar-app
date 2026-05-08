package co.com.bar.bar_app.infrastructure.output.persistence;

import co.com.bar.bar_app.application.ports.out.TipoPagoOutPort;
import co.com.bar.bar_app.domain.model.TipoPago;
import co.com.bar.bar_app.infrastructure.mapper.TipoPagoMapper;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.TipoPagoEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.TipoPagoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class TipoPagoPersistenceAdapter implements TipoPagoOutPort {
    private final TipoPagoRepository tipoPagoRepository;
    private final TipoPagoMapper tipoPagoMapper;

    public TipoPagoPersistenceAdapter(TipoPagoRepository tipoPagoRepository, TipoPagoMapper tipoPagoMapper) {
        this.tipoPagoRepository = tipoPagoRepository;
        this.tipoPagoMapper = tipoPagoMapper;
    }

    @Override
    public void create(TipoPago tipoPago, String username) {
        TipoPagoEntity entity = new TipoPagoEntity();
        entity.setId(tipoPago.getId());
        entity.setDescripcion(tipoPago.getDescripcion());
        entity.setEsEfectivo(tipoPago.isEsEfectivo());
        entity.setEsTransferencia(tipoPago.isEsTransferencia());
        entity.setEsTarjetaDebito(tipoPago.isEsTarjetaDebito());
        entity.setEsTarjetaCredito(tipoPago.isEsTarjetaCredito());
        entity.setEstado(tipoPago.isActivo());
        entity.setFechaCreacion(LocalDateTime.now());
        entity.setUsuarioCrea(username);
        this.tipoPagoRepository.save(entity);
    }

    @Override
    public List<TipoPago> findAll() {
        return this.tipoPagoRepository.findAll().stream().map(tipoPagoMapper::entityToDomain).toList();
    }

    @Override
    public TipoPago findById(UUID id) {
        return this.tipoPagoRepository.findById(id)
                .map(tipoPagoMapper::entityToDomain)
                .orElse(null);
    }

    @Override
    public void update(UUID id, TipoPago tipoPago, String username) {
        TipoPagoEntity entity = this.tipoPagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de pago no encontrado"));
        entity.setDescripcion(tipoPago.getDescripcion());
        entity.setEsEfectivo(tipoPago.isEsEfectivo());
        entity.setEsTransferencia(tipoPago.isEsTransferencia());
        entity.setEsTarjetaDebito(tipoPago.isEsTarjetaDebito());
        entity.setEsTarjetaCredito(tipoPago.isEsTarjetaCredito());
        entity.setEstado(tipoPago.isActivo());
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setUsuarioModifica(username);
        this.tipoPagoRepository.save(entity);
    }
}
