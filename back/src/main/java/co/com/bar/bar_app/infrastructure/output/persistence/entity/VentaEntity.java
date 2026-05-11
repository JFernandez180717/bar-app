package co.com.bar.bar_app.infrastructure.output.persistence.entity;

import co.com.bar.bar_app.infrastructure.output.persistence.converter.EstadoConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
@Getter
@Setter
@NoArgsConstructor
public class VentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    private LocalDateTime fecha;
    private double total;
    private double totalDescuento;

    @Column(length = 30)
    private String usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_pago")
    private TipoPagoEntity tipoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mesa_id")
    private MesaEntity mesa;

    @Convert(converter = EstadoConverter.class)
    private boolean estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "usuario_crea", length = 30)
    private String usuarioCrea;

    @Column(name = "usuario_modifica", length = 30)
    private String usuarioModifica;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VentaDetalleEntity> detalles = new ArrayList<>();

    public void addDetalle(VentaDetalleEntity detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }

    public void removeDetalle(VentaDetalleEntity detalle) {
        detalles.remove(detalle);
        detalle.setVenta(null);
    }
}