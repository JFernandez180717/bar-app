package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.domain.model.Categoria;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.CategoriaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    @Mapping(source = "estado", target = "activo")
    Categoria categoriaEntityToCategoria(CategoriaEntity entity);
}
