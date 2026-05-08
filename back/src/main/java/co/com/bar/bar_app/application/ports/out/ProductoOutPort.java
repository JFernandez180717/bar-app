package co.com.bar.bar_app.application.ports.out;

import co.com.bar.bar_app.domain.model.PageResponse;
import co.com.bar.bar_app.domain.model.PaginationRequest;
import co.com.bar.bar_app.domain.model.Producto;

import java.util.List;
import java.util.UUID;

public interface ProductoOutPort {
    Producto create(Producto producto, String username);
    PageResponse<Producto> findAll(PaginationRequest pagination);
    void update(UUID id, Producto producto, String username);
    Producto findById(UUID id);
    void changeStatus(UUID id, String username);
    void updateImage(UUID id, String image, String username);
    String getProductImage(UUID id);
    List<Producto> findDestacadosActivos();
}
