package co.com.bar.bar_app.infrastructure.output.persistence.repository;

import co.com.bar.bar_app.infrastructure.output.persistence.entity.TipoIdentificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoIdentificacionRepository extends JpaRepository<TipoIdentificacionEntity, String> {
}
