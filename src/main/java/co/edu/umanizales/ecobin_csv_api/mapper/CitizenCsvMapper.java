package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;

/**
 * Convierte entre:
 *  - una fila del CSV (String[]) y
 *  - un objeto Citizen.
 *
 * En el archivo citizens.csv usamos las columnas:
 * id,document,firstName,lastName,email,points
 */
public class CitizenCsvMapper {

    /**
     * Convierte un arreglo de columnas a un objeto Citizen.
     */
    public Citizen fromCsv(String[] c) {
        Citizen citizen = new Citizen();
        citizen.setId(Long.parseLong(c[0]));    // id
        citizen.setDocument(c[1]);              // document
        citizen.setFirstName(c[2]);             // firstName
        citizen.setLastName(c[3]);              // lastName
        citizen.setEmail(c[4]);                 // email
        citizen.setPoints(Long.parseLong(c[5]));// points
        return citizen;
    }

    /**
     * Convierte un objeto Citizen en columnas de texto para CSV.
     */
    public String[] toCsv(Citizen c) {
        return new String[] {
                String.valueOf(c.getId()),
                c.getDocument(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                String.valueOf(c.getPoints())
        };
    }
}