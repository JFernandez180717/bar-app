package co.com.bar.bar_app.application.mapper;

import co.com.bar.bar_app.application.dto.CategoriaApplicationDto;
import co.com.bar.bar_app.application.dto.CategoriaCreadaDto;
import co.com.bar.bar_app.domain.model.Categoria;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaApplicationMapper {
    CategoriaCreadaDto domainToDto(Categoria categoria);

    List<CategoriaApplicationDto> domainListToApplicationDtoList(List<Categoria> categorias);

    CategoriaApplicationDto domainToApplicationDto(Categoria categoria);
}
