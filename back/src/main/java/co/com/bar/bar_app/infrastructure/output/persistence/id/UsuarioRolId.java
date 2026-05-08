package co.com.bar.bar_app.infrastructure.output.persistence.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UsuarioRolId implements Serializable {
    @Column(length = 30)
    private String username;
    @Column(length = 30)
    private String rol;
}
