package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Reading = lectura de peso registrada en un punto.
 * Relaciones por ID: pointId, wasteTypeId, citizenId.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Reading {
    private Long id;
    private Long pointId;       // EcoBinPoint
    private Long wasteTypeId;   // WasteType
    private Long citizenId;     // Citizen
    private long grams;         // > 0
    private String isoDateTime; // fecha/hora en formato ISO-8601
}

