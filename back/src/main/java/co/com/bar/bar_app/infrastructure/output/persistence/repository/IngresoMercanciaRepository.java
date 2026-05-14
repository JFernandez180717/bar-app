package co.com.bar.bar_app.infrastructure.output.persistence.repository;

import co.com.bar.bar_app.infrastructure.output.persistence.entity.IngresoMercanciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface IngresoMercanciaRepository extends JpaRepository<IngresoMercanciaEntity, UUID> {

    @Query("SELECT i FROM IngresoMercanciaEntity i WHERE i.estado = 1 " +
           "AND (:fechaInicio IS NULL OR i.fecha >= :fechaInicio) " +
           "AND (:fechaFin IS NULL OR i.fecha <= :fechaFin) " +
           "AND (:idProveedor IS NULL OR i.idProveedor = :idProveedor)")
    List<IngresoMercanciaEntity> findByFilters(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("idProveedor") String idProveedor
    );
}