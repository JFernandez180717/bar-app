package co.com.bar.bar_app.infrastructure.output.persistence.repository;

import co.com.bar.bar_app.infrastructure.output.persistence.entity.ProductoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductoRepository extends JpaRepository<ProductoEntity, UUID> {
    List<ProductoEntity> findByDestacadoTrueAndEstadoTrue();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductoEntity p WHERE p.id = id")
    ProductoEntity findByIdWithLock(@Param("id") UUID id);

    @Query("UPDATE ProductoEntity p SET p.stock = :stock WHERE p.id = :id")
    void actualizarStock(@Param("id") UUID id, @Param("stock") int stock);
}
