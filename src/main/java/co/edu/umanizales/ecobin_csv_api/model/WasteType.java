package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Tipo de residuo (PLASTIC, GLASS, etc.). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WasteType {
    private long id;
    private String name;
    private String description;
}
