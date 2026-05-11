package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.domain.model.VentaDetalle;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.VentaDetalleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductoMapper.class)
public interface VentaDetalleMapper {
    @Mapping(source = "idProducto", target = "producto")
    @Mapping(target = "total", ignore = true)
    VentaDetalle entityToDomain(VentaDetalleEntity ventaDetalleEntity);
}
