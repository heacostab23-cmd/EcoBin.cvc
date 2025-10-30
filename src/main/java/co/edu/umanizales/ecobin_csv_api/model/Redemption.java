package co.edu.umanizales.ecobin_csv_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Redemption = canje de una Reward por un Citizen.
 * Relaciones por ID: citizenId, rewardId.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Redemption {
    private Long id;
    private Long citizenId;          // Citizen
    private Long rewardId;           // Reward
    private RedemptionStatus status; // REQUESTED / APPROVED / DELIVERED / REJECTED
    private String isoDateTime;      // ISO-8601
}
