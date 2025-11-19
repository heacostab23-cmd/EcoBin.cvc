package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Regla de misión: tipo de residuo, meta (kg) y periodo ISO (ej: P7D).
 * Se usa por composición dentro de Mission.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionRule {
    private long id;
    private WasteType wasteType;
    private long targetKg;
    private String isoPeriod;

    /** Constructor con solo id (para placeholders en CSV). */
    public MissionRule(long id) {
        this.id = id;
    }
}
