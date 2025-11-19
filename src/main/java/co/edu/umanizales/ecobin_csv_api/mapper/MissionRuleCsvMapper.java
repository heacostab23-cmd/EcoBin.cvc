package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.MissionRule;
import co.edu.umanizales.ecobin_csv_api.model.WasteType;

/**
 * Convierte entre filas CSV y objetos MissionRule.
 * CSV: id,wasteTypeId,targetKg,isoPeriod
 */
public class MissionRuleCsvMapper {

    public MissionRule fromCsv(String[] c) {
        MissionRule mr = new MissionRule();
        mr.setId(Long.parseLong(c[0]));
        mr.setWasteType(new WasteType(Long.parseLong(c[1])));  // placeholder
        mr.setTargetKg(Long.parseLong(c[2]));
        mr.setIsoPeriod(c[3]);
        return mr;
    }

    public String[] toCsv(MissionRule mr) {
        return new String[]{
                String.valueOf(mr.getId()),
                String.valueOf(mr.getWasteType().getId()),
                String.valueOf(mr.getTargetKg()),
                mr.getIsoPeriod()
        };
    }
}
