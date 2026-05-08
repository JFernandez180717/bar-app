package co.com.bar.bar_app.application.mapper;

import co.com.bar.bar_app.application.dto.CrearTipoPagoDto;
import co.com.bar.bar_app.application.dto.TipoPagoApplicationDto;
import co.com.bar.bar_app.domain.model.TipoPago;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoPagoApplicationMapper {
    TipoPago tipoPagoCreateDtoToDomain(CrearTipoPagoDto dto);
    TipoPagoApplicationDto domainToApplicationDto(TipoPago tipoPago);
    List<TipoPagoApplicationDto> domainListToApplicationDtoList(List<TipoPago> tipoPago);
}
