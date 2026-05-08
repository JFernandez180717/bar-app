package co.com.bar.bar_app.application.mapper;

import co.com.bar.bar_app.application.dto.ProductoApplicationDto;
import co.com.bar.bar_app.application.dto.ProductoCreadoDto;
import co.com.bar.bar_app.domain.model.Producto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoApplicationMapper {
    ProductoCreadoDto domainToCretedDto(Producto producto);
    ProductoApplicationDto domainToApplicationDto(Producto producto);
}
