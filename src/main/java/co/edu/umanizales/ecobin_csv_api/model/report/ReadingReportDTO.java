package co.edu.umanizales.ecobin_csv_api.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que representa el reporte de un día:
 * - fecha
 * - total de gramos
 * - lista de reciclaje agrupado por tipo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingReportDTO {

    // Fecha del día del reporte (formato YYYY-MM-DD)
    private String fecha;

    // Total de gramos reciclados en esa fecha
    private int total;

    // Lista de tipos de residuos y su gramaje total
    private List<ReadingDetailDTO> reciclaje;
}
