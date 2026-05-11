package co.com.bar.bar_app.application.ports.in;

import co.com.bar.bar_app.application.dto.CrearVentaDto;
import co.com.bar.bar_app.application.dto.VentaApplicationDto;
import co.com.bar.bar_app.application.dto.VentaCreadaDto;
import co.com.bar.bar_app.domain.model.PageResponse;
import co.com.bar.bar_app.domain.model.PaginationRequest;

public interface VentaInPort {
    VentaCreadaDto crearVenta(CrearVentaDto dto, String username);
    PageResponse<VentaApplicationDto> findAll(PaginationRequest pagination);
    VentaApplicationDto findById(int codigo);
}