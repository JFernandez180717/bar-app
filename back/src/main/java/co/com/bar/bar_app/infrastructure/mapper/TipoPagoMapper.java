package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.application.dto.CrearTipoPagoDto;
import co.com.bar.bar_app.application.dto.TipoPagoApplicationDto;
import co.com.bar.bar_app.domain.model.TipoPago;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CreateTipoPagoRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.TipoPagoResponseDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.UpdateTipoPagoRequestDto;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.TipoPagoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoPagoMapper {
    CrearTipoPagoDto requestDtoToApplicationDto(CreateTipoPagoRequestDto requestDto);
    CrearTipoPagoDto requestDtoToApplicationDto(UpdateTipoPagoRequestDto requestDto);
    TipoPagoEntity domainToEntity(TipoPago tipoPago);
    @Mapping(source = "estado", target = "activo")
    TipoPago entityToDomain(TipoPagoEntity entity);
    List<TipoPagoResponseDto> applicationDtoListToResponseDtoList(List<TipoPagoApplicationDto> tipoPagoApplicationDtoList);
}
