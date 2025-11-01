package co.edu.umanizales.ecobin_csv_api.model;

import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Lectura de peso realizada por un Citizen en un EcoBinPoint para un WasteType.
 * Relaciones por OBJETO (POO): point, wasteType, citizen.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Reading {
    private Long id;
    private EcoBinPoint point;
    private WasteType wasteType;
    private Citizen citizen;
    private long grams;         // > 0
    private String isoDateTime; // ISO-8601
}

