package co.com.bar.bar_app.application.mapper;

import co.com.bar.bar_app.application.dto.MarcaApplicationDto;
import co.com.bar.bar_app.application.dto.MarcaCreadaDto;
import co.com.bar.bar_app.domain.model.Marca;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MarcaApplicationMapper {
    MarcaCreadaDto domainToMarcaCreadaDto(Marca marca);

    MarcaApplicationDto domainToMarcaApplicationDto(Marca marca);

    List<MarcaApplicationDto> domainListToMarcaApplicationDtoList(List<Marca> marca);
}
