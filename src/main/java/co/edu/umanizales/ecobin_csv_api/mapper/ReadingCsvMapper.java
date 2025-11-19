package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.Reading;
import co.edu.umanizales.ecobin_csv_api.model.EcoBinPoint;
import co.edu.umanizales.ecobin_csv_api.model.WasteType;
import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;

import java.time.LocalDate;

/**
 * Convierte entre filas CSV y objetos Reading.
 * CSV: id,pointId,wasteTypeId,citizenId,grams,date
 */
public class ReadingCsvMapper {

    public Reading fromCsv(String[] c) {
        Reading r = new Reading();
        r.setId(Long.parseLong(c[0]));
        r.setPoint(new EcoBinPoint(Long.parseLong(c[1])));      // placeholder
        r.setWasteType(new WasteType(Long.parseLong(c[2])));    // placeholder
        r.setCitizen(new Citizen(Long.parseLong(c[3])));        // placeholder
        r.setGrams(Long.parseLong(c[4]));
        r.setDate(LocalDate.parse(c[5]));
        return r;
    }

    public String[] toCsv(Reading r) {
        return new String[]{
                String.valueOf(r.getId()),
                String.valueOf(r.getPoint().getId()),
                String.valueOf(r.getWasteType().getId()),
                String.valueOf(r.getCitizen().getId()),
                String.valueOf(r.getGrams()),
                r.getDate().toString()
        };
    }
}
