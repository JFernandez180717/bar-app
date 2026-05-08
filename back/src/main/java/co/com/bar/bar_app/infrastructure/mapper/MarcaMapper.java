package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.application.dto.MarcaApplicationDto;
import co.com.bar.bar_app.application.dto.MarcaCreadaDto;
import co.com.bar.bar_app.application.dto.ActualizarMarcaDto;
import co.com.bar.bar_app.domain.model.Marca;
import co.com.bar.bar_app.infrastructure.input.rest.dto.MarcaCreadaResponseDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.MarcaResponseDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ActualizarMarcaRequestDto;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.MarcaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MarcaMapper {

    @Mapping(target = "estado", source = "activo")
    MarcaEntity domainToEntity(Marca marca);

    @Mapping(source = "estado", target = "activo")
    Marca entityToDomain(MarcaEntity marcaEntity);

    MarcaCreadaResponseDto createdDtoToCreatedResponseDto(MarcaCreadaDto marcaDto);

    @Mapping(source = "activo", target = "estado", qualifiedByName = "booleanToInteger")
    List<MarcaResponseDto> applicationDtoListToResponseDtoList(List<MarcaApplicationDto> marcaApplicationDtoList);

    ActualizarMarcaDto requestToActualizarDto(ActualizarMarcaRequestDto requestDto);

    default Integer booleanToInteger(Boolean value) {
        if (value == null) return 0;
        return value ? 1 : 0;
    }
}
