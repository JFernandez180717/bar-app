package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.application.dto.CrearProductoDto;
import co.com.bar.bar_app.application.dto.UpdateProductoDto;
import co.com.bar.bar_app.domain.model.Producto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearProductoRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.UpdateProductoRequestDto;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.ProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(source = "estado", target = "activo")
    Producto entityToDomain(ProductoEntity productoEntity);

    CrearProductoDto requestCreateDtoToApplicationCreateDto(CrearProductoRequestDto requestDto);

    UpdateProductoDto requestUpdateDtoToApplicationUpdateDto(UpdateProductoRequestDto requestDto);
}
