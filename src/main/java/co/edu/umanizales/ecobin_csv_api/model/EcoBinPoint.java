package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * EcoBinPoint = contenedor/báscula físico.
 * Tiene una Location por COMPOSICIÓN (Location vive dentro del punto).
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EcoBinPoint {
    private Long id;
    private String name;
    private Location location; // lat/lon/dirección
}
