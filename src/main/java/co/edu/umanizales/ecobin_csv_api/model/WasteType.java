package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * WasteType = tipo de residuo (plástico, vidrio, etc.).
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class WasteType {
    private Long id;
    private String name;        // Ej: "PLASTIC"
    private String description; // Texto opcional
}
