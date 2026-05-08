package co.com.bar.bar_app.application.service;

import co.com.bar.bar_app.application.dto.CrearTipoPagoDto;
import co.com.bar.bar_app.application.dto.TipoPagoApplicationDto;
import co.com.bar.bar_app.application.mapper.TipoPagoApplicationMapper;
import co.com.bar.bar_app.application.ports.in.TipoPagoInPort;
import co.com.bar.bar_app.application.ports.out.TipoPagoOutPort;
import co.com.bar.bar_app.domain.exception.TipoPagoNotValidException;
import co.com.bar.bar_app.domain.model.TipoPago;
import co.com.bar.bar_app.infrastructure.input.rest.dto.UpdateTipoPagoRequestDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class TipoPagoService implements TipoPagoInPort {
    private final TipoPagoOutPort tipoPagoOutPort;
    private final TipoPagoApplicationMapper tipoPagoMapper;

    public TipoPagoService(TipoPagoOutPort tipoPagoOutPort, TipoPagoApplicationMapper tipoPagoMapper) {
        this.tipoPagoOutPort = tipoPagoOutPort;
        this.tipoPagoMapper = tipoPagoMapper;
    }

    @Override
    public TipoPagoApplicationDto create(CrearTipoPagoDto dto, String username) {
        TipoPago tipoPago = TipoPago.create(
                dto.descripcion(),
                dto.esEfectivo(),
                dto.esTransferencia(),
                dto.esTarjetaDebito(),
                dto.esTarjetaCredito()
        );
        this.tipoPagoOutPort.create(tipoPago, username);
        return tipoPagoMapper.domainToApplicationDto(tipoPago);
    }

    @Override
    public List<TipoPagoApplicationDto> findAll() {
        return tipoPagoMapper.domainListToApplicationDtoList(tipoPagoOutPort.findAll());
    }

    @Override
    public void update(UUID id, UpdateTipoPagoRequestDto requestDto, String username) {
        // Validate existence
        if (tipoPagoOutPort.findById(id) == null) {
            throw new IllegalArgumentException("Tipo de pago no encontrado");
        }

        // Create TipoPago domain object
        TipoPago tipoPago = new TipoPago(
                id,
                requestDto.descripcion(),
                requestDto.esEfectivo(),
                requestDto.esTransferencia(),
                requestDto.esTarjetaDebito(),
                requestDto.esTarjetaCredito(),
                requestDto.activo()
        );

        this.tipoPagoOutPort.update(id, tipoPago, username);
    }
}
