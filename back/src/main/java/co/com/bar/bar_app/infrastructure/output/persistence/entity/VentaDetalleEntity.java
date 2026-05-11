package co.com.bar.bar_app.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "venta_detalle")
@IdClass(VentaDetalleEntityId.class)
@Getter
@Setter
@NoArgsConstructor
public class VentaDetalleEntity {
    @Id
    @Column(name = "codigo")
    private int codigo;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private ProductoEntity idProducto;

    private int cantidad;
    private double precioUnitario;
    private double descuento;
    private double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo", insertable = false, updatable = false)
    private VentaEntity venta;
}