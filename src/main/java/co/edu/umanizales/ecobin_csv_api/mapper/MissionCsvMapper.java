package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.Mission;
import co.edu.umanizales.ecobin_csv_api.model.MissionRule;
import co.edu.umanizales.ecobin_csv_api.model.MissionStatus;

/**
 * Convierte entre filas CSV y objetos Mission.
 * CSV: id,name,ruleId,status
 */
public class MissionCsvMapper {

    public Mission fromCsv(String[] c) {
        Mission m = new Mission();
        m.setId(Long.parseLong(c[0]));
        m.setName(c[1]);
        m.setRule(new MissionRule(Long.parseLong(c[2])));  // placeholder
        m.setStatus(MissionStatus.valueOf(c[3]));
        return m;
    }

    public String[] toCsv(Mission m) {
        return new String[]{
                String.valueOf(m.getId()),
                m.getName(),
                String.valueOf(m.getRule().getId()),
                m.getStatus().name()
        };
    }
}
