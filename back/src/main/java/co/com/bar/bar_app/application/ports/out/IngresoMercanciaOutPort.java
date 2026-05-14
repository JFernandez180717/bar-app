package co.com.bar.bar_app.application.ports.out;

import co.com.bar.bar_app.domain.model.IngresoMercancia;
import co.com.bar.bar_app.domain.model.Producto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngresoMercanciaOutPort {
    IngresoMercancia create(IngresoMercancia ingreso, String username);
    Optional<IngresoMercancia> findById(UUID id);
    List<IngresoMercancia> findAll(LocalDate fechaInicio, LocalDate fechaFin, String idProveedor);
    Optional<Producto> findProductoById(UUID id);
    Producto updateProducto(Producto producto);
    Optional<IngresoMercancia> update(IngresoMercancia ingreso, String username);
}