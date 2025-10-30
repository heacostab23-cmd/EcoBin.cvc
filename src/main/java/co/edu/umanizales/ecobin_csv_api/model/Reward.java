package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Reward = recompensa canjeable por puntos.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Reward {
    private Long id;
    private String name;       // Ej: "Descuento 10%"
    private long costPoints;   // Costo en puntos (>0)
    private int stock;         // Unidades disponibles (>=0)
    private String description;
}
