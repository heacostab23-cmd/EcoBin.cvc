package co.edu.umanizales.ecobin_csv_api.model.core;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Operator = operador que administra puntos EcoBin.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Operator extends Person {
    private Long userId;                        // Relación con User
    private List<Long> assignedPointIds = new ArrayList<>(); // Puntos EcoBin asignados
}

