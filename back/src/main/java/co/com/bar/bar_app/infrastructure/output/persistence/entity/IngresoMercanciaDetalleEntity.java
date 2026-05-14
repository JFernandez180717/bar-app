package co.com.bar.bar_app.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ingreso_mercancia_det")
@IdClass(IngresoMercanciaDetalleId.class)
public class IngresoMercanciaDetalleEntity {
    @Id
    @Column(name = "codigo")
    private UUID codigo;

    @Id
    @Column(name = "id_producto")
    private UUID idProducto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio", nullable = false)
    private double precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo", insertable = false, updatable = false)
    private IngresoMercanciaEntity ingresoMercancia;
}