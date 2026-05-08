package co.com.bar.bar_app.application.ports.out;

import co.com.bar.bar_app.domain.model.Proveedor;

import java.util.List;

public interface ProveedorOutPort {
    Proveedor create(Proveedor proveedor, String username);
    List<Proveedor> findAll();
    Proveedor update(Proveedor proveedor, String username);
}
