package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.domain.model.IngresoMercancia;
import co.com.bar.bar_app.domain.model.IngresoMercanciaDetalle;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.IngresoMercanciaDetalleEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.IngresoMercanciaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngresoMercanciaMapper {

    @Mapping(target = "detalles", ignore = true)
    IngresoMercanciaEntity domainToEntity(IngresoMercancia domain);

    @Mapping(target = "ingresoMercancia", ignore = true)
    IngresoMercanciaDetalleEntity domainDetalleToEntity(IngresoMercanciaDetalle domain);

    @Mapping(target = "detalles", ignore = true)
    IngresoMercancia entityToDomain(IngresoMercanciaEntity entity);

    IngresoMercanciaDetalle entityDetalleToDomain(IngresoMercanciaDetalleEntity entity);

    List<IngresoMercanciaDetalle> entityDetalleListToDomainList(List<IngresoMercanciaDetalleEntity> entities);
}