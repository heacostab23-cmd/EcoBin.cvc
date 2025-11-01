package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Contenedor/báscula EcoBin con geolocalización.
 * Composición: contiene Location.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EcoBinPoint {
    private Long id;
    private String name;
    private Location location;
}

