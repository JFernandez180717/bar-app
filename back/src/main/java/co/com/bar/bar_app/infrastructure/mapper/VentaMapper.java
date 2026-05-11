package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.application.dto.CrearVentaDto;
import co.com.bar.bar_app.application.dto.DetalleVentaItem;
import co.com.bar.bar_app.domain.model.Venta;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearVentaRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.DetalleVentaItemRequest;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.VentaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {VentaDetalleMapper.class, TipoPagoMapper.class, MesaMapper.class})
public interface VentaMapper {
    @Mapping(source = "estado", target = "activo")
    Venta entityToDomain(VentaEntity ventaEntity);

    CrearVentaDto requestToApplicationDto(CrearVentaRequestDto requestDto);
}