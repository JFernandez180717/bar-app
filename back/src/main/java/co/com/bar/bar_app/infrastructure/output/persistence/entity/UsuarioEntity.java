package co.com.bar.bar_app.infrastructure.output.persistence.entity;

import co.com.bar.bar_app.infrastructure.output.persistence.converter.EstadoConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @Column(length = 30)
    private String username;

    @Column(length = 100)
    private String email;

    @Column(length = 250)
    private String pass;

    @Column(name = "primer_nombre", length = 40)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 40)
    private String segundoNombre;

    @Column(name = "primer_apellido", length = 40)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 40)
    private String segundoApellido;

    @Column(length = 20)
    private String identificacion;

    @Column(length = 200)
    private String direccion;

    @Column(length = 20)
    private String telefono;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tipo_identificacion",      // columna en usuario
            referencedColumnName = "tipo",     // columna en tipo_identificacion
            nullable = false
    )
    private TipoIdentificacionEntity tipoIdentificacion;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UsuarioRolEntity> roles;
}
