package co.com.bar.bar_app.infrastructure.output.persistence.repository;

import co.com.bar.bar_app.infrastructure.output.persistence.entity.VentaDetalleEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.VentaDetalleEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalleEntity, VentaDetalleEntityId> {
    List<VentaDetalleEntity> findByCodigo(int codigo);
}