package co.com.bar.bar_app.infrastructure.output.persistence.entity;

import co.com.bar.bar_app.infrastructure.output.persistence.converter.EstadoConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
public class ProductoEntity {
    @Id
    private UUID id;

    @Column(length = 50)
    private String nombre;

    @Column(length = 100)
    private String descripcion;

    private int stock;

    private int stockMinimo;

    private double precio;

    @Column(length = 500)
    private String imagen;

    @Convert(converter = EstadoConverter.class)
    private boolean estado;

    @Convert(converter = EstadoConverter.class)
    private boolean destacado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "usuario_crea", length = 30)
    private String usuarioCrea;

    @Column(name = "usuario_modifica", length = 30)
    private String usuarioModifica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private CategoriaEntity categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca")
    private MarcaEntity marca;
}
