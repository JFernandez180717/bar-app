package co.com.bar.bar_app.infrastructure.output.persistence.repository;

import co.com.bar.bar_app.infrastructure.output.persistence.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<VentaEntity, Integer> {
}