package co.com.bar.bar_app.infrastructure.output.persistence.entity;

import co.com.bar.bar_app.infrastructure.output.persistence.converter.EstadoConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ingreso_mercancia")
public class IngresoMercanciaEntity {
    @Id
    private UUID id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "usuario_recibe", nullable = false, length = 30)
    private String usuarioRecibe;

    @Column(name = "id_proveedor", length = 20)
    private String idProveedor;

    @Column(name = "estado", nullable = false)
    @Convert(converter = EstadoConverter.class)
    private boolean estado = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "usuario_crea", nullable = false, length = 100)
    private String usuarioCrea;

    @Column(name = "usuario_modifica", length = 100)
    private String usuarioModifica;

    @OneToMany(mappedBy = "ingresoMercancia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IngresoMercanciaDetalleEntity> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}