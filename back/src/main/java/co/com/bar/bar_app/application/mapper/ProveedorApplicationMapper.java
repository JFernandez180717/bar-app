package co.com.bar.bar_app.application.mapper;

import co.com.bar.bar_app.application.dto.ProveedorApplicationDto;
import co.com.bar.bar_app.application.dto.ProveedorCreadoDto;
import co.com.bar.bar_app.domain.model.Proveedor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProveedorApplicationMapper {
    ProveedorCreadoDto domainToProveedorCreadoDto(Proveedor proveedor);

    ProveedorApplicationDto domainToProveedorApplicationDto(Proveedor proveedor);

    List<ProveedorApplicationDto> domainListToProveedorApplicationDtoList(List<Proveedor> proveedores);
}
