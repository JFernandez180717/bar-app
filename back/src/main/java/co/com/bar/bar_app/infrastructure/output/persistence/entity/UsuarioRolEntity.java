package co.com.bar.bar_app.infrastructure.output.persistence.entity;

import co.com.bar.bar_app.infrastructure.output.persistence.id.UsuarioRolId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_rol")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioRolEntity {
    @EmbeddedId
    private UsuarioRolId id;

    @ManyToOne
    @MapsId("username")  // le dice que "email" del EmbeddedId es esta FK
    @JoinColumn(name = "username")
    private UsuarioEntity usuario;
}
