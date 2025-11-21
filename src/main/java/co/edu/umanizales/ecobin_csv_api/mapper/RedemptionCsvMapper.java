package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.Redemption;
import co.edu.umanizales.ecobin_csv_api.model.RedemptionStatus;
import co.edu.umanizales.ecobin_csv_api.model.Reward;
import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;

import java.time.LocalDate;

/**
 * Convierte entre filas CSV y objetos Redemption.
 * SOLO se mueven IDs aquí. El service luego re-hidrata (busca los objetos reales).
 */
public class RedemptionCsvMapper {

    // CSV → OBJETO
    public Redemption fromCsv(String[] c) {
        Redemption r = new Redemption();
        r.setId(Long.parseLong(c[0]));
        r.setCitizen(new Citizen(Long.parseLong(c[1])));  // objeto “placeholder”
        r.setReward(new Reward(Long.parseLong(c[2])));    // objeto “placeholder”
        r.setStatus(RedemptionStatus.valueOf(c[3]));
        r.setDate(LocalDate.parse(c[4]));
        return r;
    }

    // OBJETO → CSV (solo los IDs)
    public String[] toCsv(Redemption r) {
        return new String[]{
                String.valueOf(r.getId()),
                String.valueOf(r.getCitizen().getId()),
                String.valueOf(r.getReward().getId()),
                r.getStatus().name(),
                r.getDate().toString()
        };
    }
}