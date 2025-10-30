package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Badge = insignia otorgada al superar un mínimo de puntos.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Badge {
    private Long id;
    private String name;      // Ej: "EcoHero Lv1"
    private long minPoints;   // Umbral de puntos (>=0)
    private String description;
}

