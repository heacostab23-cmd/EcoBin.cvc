package co.edu.umanizales.ecobin_csv_api.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un tipo de residuo y la suma de gramaje reciclado en un día.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingDetailDTO {

    // Nombre del tipo de residuo (ej: "Plástico", "Vidrio")
    private String tipo;

    // Total de gramos reciclados de ese tipo en el día
    private int gramaje;
}
