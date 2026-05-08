package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.application.dto.ProveedorApplicationDto;
import co.com.bar.bar_app.application.dto.ProveedorCreadoDto;
import co.com.bar.bar_app.domain.model.Proveedor;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ProveedorCreadoResponseDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ProveedorResponseDto;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.ProveedorEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.TipoIdentificacionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProveedorMapper {

    ProveedorEntity domainToEntity(Proveedor proveedor);

    // Le dices a MapStruct cómo convertir TipoIdentificacionEntity -> String
    default String tipoIdentificacionToString(TipoIdentificacionEntity tipo) {
        return tipo != null ? tipo.getTipo() : null;
    }

    // Y también para el sentido inverso (dominio -> entidad)
    default TipoIdentificacionEntity stringToTipoIdentificacion(String tipo) {
        if (tipo == null) return null;
        TipoIdentificacionEntity entity = new TipoIdentificacionEntity();
        entity.setTipo(tipo);
        return entity;
    }

    Proveedor entityToDomain(ProveedorEntity entity);

    ProveedorCreadoResponseDto createdDtoToCreatedResponseDto(ProveedorCreadoDto dto);

    List<ProveedorResponseDto> applicationDtoListToResponseDtoList(List<ProveedorApplicationDto> dtos);

    ProveedorResponseDto applicationDtoToResponseDto(ProveedorApplicationDto dto);
}
