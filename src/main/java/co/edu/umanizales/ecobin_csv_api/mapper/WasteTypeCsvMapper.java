package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.WasteType;

/**
 * Mapper para WasteType.
 * CSV: id,name,description
 */
public class WasteTypeCsvMapper {

    public WasteType fromCsv(String[] c) {
        WasteType wt = new WasteType();
        wt.setId(Long.parseLong(c[0]));
        wt.setName(c[1]);
        wt.setDescription(c[2]);
        return wt;
    }

    public String[] toCsv(WasteType wt) {
        return new String[] {
                String.valueOf(wt.getId()),
                wt.getName(),
                wt.getDescription()
        };
    }
}
