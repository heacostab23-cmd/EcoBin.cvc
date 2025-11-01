package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Misión gamificada con una regla (MissionRule) y un estado.
 * Composición: contiene MissionRule.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mission {
    private long id;
    private String name;
    private MissionRule rule;
    private MissionStatus status;
}
