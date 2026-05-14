package co.com.bar.bar_app.application.mapper;

import co.com.bar.bar_app.application.dto.*;
import co.com.bar.bar_app.domain.model.IngresoMercancia;
import co.com.bar.bar_app.domain.model.IngresoMercanciaDetalle;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IngresoMercanciaApplicationMapper {

    public IngresoMercancia toDomain(CrearIngresoMercanciaDto dto) {
        List<IngresoMercanciaDetalle> detalles = dto.detalles().stream()
                .map(this::toDomainDetalle)
                .toList();

        return IngresoMercancia.builder()
                .id(java.util.UUID.randomUUID())
                .fecha(dto.fecha())
                .usuarioRecibe(dto.usuarioRecibe())
                .idProveedor(dto.idProveedor())
                .estado(true)
                .detalles(detalles)
                .build();
    }

    private IngresoMercanciaDetalle toDomainDetalle(IngresoMercanciaDetalleDto dto) {
        return IngresoMercanciaDetalle.crear(
                dto.idProducto(),
                dto.cantidad(),
                dto.precio()
        );
    }

    public IngresoMercanciaCreadoDto toCreadoDto(IngresoMercancia domain) {
        return new IngresoMercanciaCreadoDto(
                domain.getId(),
                domain.getFecha(),
                domain.getUsuarioRecibe(),
                domain.getIdProveedor(),
                domain.isEstado() ? 1 : 0
        );
    }

    public IngresoMercanciaApplicationDto toApplicationDto(IngresoMercancia domain, String nombreProveedor) {
        List<IngresoMercanciaDetalleApplicationDto> detalles = domain.getDetalles().stream()
                .map(this::toApplicationDetalleDto)
                .toList();

        return new IngresoMercanciaApplicationDto(
                domain.getId(),
                domain.getFecha(),
                domain.getUsuarioRecibe(),
                nombreProveedor,
                domain.isEstado() ? 1 : 0,
                detalles
        );
    }

    private IngresoMercanciaDetalleApplicationDto toApplicationDetalleDto(IngresoMercanciaDetalle detalle) {
        return new IngresoMercanciaDetalleApplicationDto(
                detalle.getIdProducto(),
                null, // El nombre del producto se obtiene después
                detalle.getCantidad(),
                detalle.getPrecio()
        );
    }

    public IngresoMercanciaApplicationDto toApplicationDtoWithProductoNombre(IngresoMercancia domain, String nombreProveedor, java.util.Map<java.util.UUID, String> productoNombres) {
        List<IngresoMercanciaDetalleApplicationDto> detalles = domain.getDetalles().stream()
                .map(d -> new IngresoMercanciaDetalleApplicationDto(
                        d.getIdProducto(),
                        productoNombres.getOrDefault(d.getIdProducto(), "Desconocido"),
                        d.getCantidad(),
                        d.getPrecio()
                ))
                .toList();

        return new IngresoMercanciaApplicationDto(
                domain.getId(),
                domain.getFecha(),
                domain.getUsuarioRecibe(),
                nombreProveedor,
                domain.isEstado() ? 1 : 0,
                detalles
        );
    }
}