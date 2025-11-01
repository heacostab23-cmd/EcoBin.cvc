package co.edu.umanizales.ecobin_csv_api.model.core;

import co.edu.umanizales.ecobin_csv_api.model.EcoBinPoint;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Operador que administra puntos EcoBin.
 * Tiene un User y una lista de puntos asignados.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Operator extends Person {
    private User user;
    private List<EcoBinPoint> assignedPoints = new ArrayList<>();
}
