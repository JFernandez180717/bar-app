package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.domain.model.Mesa;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.MesaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MesaMapper {
    @Mapping(source = "estado", target = "activo")
    Mesa entityToDomain(MesaEntity mesaEntity);
}
