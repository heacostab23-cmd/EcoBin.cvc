package co.edu.umanizales.ecobin_csv_api.model;

import java.time.LocalDate;

import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Canje de una Reward por parte de un Citizen.
 * Relaciones por OBJETO (POO): citizen y reward.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Redemption {
    private long id;
    private Citizen citizen;
    private Reward reward;
    private RedemptionStatus status;
    private LocalDate date; 
}
