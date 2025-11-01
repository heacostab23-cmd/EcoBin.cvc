package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Recompensa canjeable por puntos. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reward {
    private long id;
    private String name;
    private long costPoints; // > 0
    private int stock;       // >= 0
    private String description;
}
