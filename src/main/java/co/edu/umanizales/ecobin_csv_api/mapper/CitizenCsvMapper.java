package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;

/**
 * Convierte entre Citizen y filas de texto (String[]) para el CSV.
 * SOLO guarda datos simples, no los objetos relacionados.
 */
public class CitizenCsvMapper {

    // Orden de columnas: id,document,firstName,lastName,email,points
    public static String[] toRow(Citizen c) {
        return new String[] {
            String.valueOf(c.getId()),
            c.getDocument(),
            c.getFirstName(),
            c.getLastName(),
            c.getEmail(),
            String.valueOf(c.getPoints())
        };
    }

    public static Citizen fromRow(String[] row) {
        Citizen c = new Citizen();
        c.setId(Long.parseLong(row[0]));
        c.setDocument(row[1]);
        c.setFirstName(row[2]);
        c.setLastName(row[3]);
        c.setEmail(row[4]);
        c.setPoints(Long.parseLong(row[5]));
        // user, badges y readings quedan null/vacíos por ahora (no se cargan desde CSV)
        return c;
    }
}

