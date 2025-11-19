package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.Badge; // <-- si tu Badge está en model.core cambia el import
                                                     // a: import co.edu.umanizales.ecobin_csv_api.model.core.Badge;

/**
 * Convierte entre una fila del CSV (String[]) y un objeto Badge.
 * Esto nos ayuda a mantener el código POO y separado de los archivos.
 */
public class BadgeCsvMapper {

    /**
     * Convierte una fila del CSV en un objeto Badge.
     * c[0] -> id, c[1] -> name, c[2] -> description, c[3] -> requiredPoints
     */
    public Badge fromCsv(String[] c) {
        Badge b = new Badge();
        b.setId(Long.parseLong(c[0]));        // id
        b.setName(c[1]);                      // nombre
        b.setDescription(c[2]);               // descripción
        b.setRequiredPoints(Long.parseLong(c[3])); // puntos requeridos
        return b;
    }

    /**
     * Convierte un Badge a un arreglo de String para escribirlo en el CSV.
     */
    public String[] toCsv(Badge b) {
        return new String[] {
            String.valueOf(b.getId()),              // id
            b.getName(),                            // nombre
            b.getDescription(),                     // descripción
            String.valueOf(b.getRequiredPoints())   // puntos requeridos
        };
    }
}
