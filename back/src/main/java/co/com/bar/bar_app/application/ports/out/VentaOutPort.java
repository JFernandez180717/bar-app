package co.com.bar.bar_app.application.ports.out;

import co.com.bar.bar_app.domain.model.PageResponse;
import co.com.bar.bar_app.domain.model.PaginationRequest;
import co.com.bar.bar_app.domain.model.Venta;

public interface VentaOutPort {
    Venta create(Venta venta, String username);
    PageResponse<Venta> findAll(PaginationRequest pagination);
    Venta findById(int codigo);
}