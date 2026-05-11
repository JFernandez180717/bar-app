package co.com.bar.bar_app.infrastructure.output.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaDetalleEntityId implements Serializable {
    private int codigo;
    private UUID idProducto;
}