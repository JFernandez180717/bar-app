package co.com.bar.bar_app.application.mapper;

import co.com.bar.bar_app.application.dto.VentaApplicationDto;
import co.com.bar.bar_app.application.dto.VentaCreadaDto;
import co.com.bar.bar_app.domain.model.Venta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VentaApplicationMapper {
    VentaCreadaDto domainToCreadoDto(Venta venta);
    VentaApplicationDto domainToApplicationDto(Venta venta);
}