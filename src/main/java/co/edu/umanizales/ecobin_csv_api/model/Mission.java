package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Mission = reto gamificado.
 * COMPOSICIÓN con MissionRule (regla) y un estado (MissionStatus).
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Mission {
    private Long id;
    private String name;         // Ej: "2 kg de plástico en 7 días"
    private MissionRule rule;    // Regla (record)
    private MissionStatus status;// ACTIVE / PAUSED / FINISHED
}
