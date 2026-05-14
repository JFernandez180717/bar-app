package co.com.bar.bar_app.infrastructure.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngresoMercanciaDetalleId implements Serializable {
    private UUID codigo;
    private UUID idProducto;
}