package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Insignia otorgada por alcanzar umbral de puntos. */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Badge {
    private Long id;
    private String name;
    private long minPoints;
    private String description;
}
