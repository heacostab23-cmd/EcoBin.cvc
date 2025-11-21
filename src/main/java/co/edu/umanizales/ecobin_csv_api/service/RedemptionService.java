package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.Redemption;
import co.edu.umanizales.ecobin_csv_api.model.RedemptionStatus;
import co.edu.umanizales.ecobin_csv_api.model.Reward;
import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;
import co.edu.umanizales.ecobin_csv_api.repository.RedemptionCsvRepository;
import co.edu.umanizales.ecobin_csv_api.repository.CitizenCsvRepository;
import co.edu.umanizales.ecobin_csv_api.repository.RewardCsvRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Lógica de negocio para Redemption (Canjes).
 * Aquí validamos reglas como: puntos suficientes, stock disponible.
 */
@Service
public class RedemptionService {

    private final RedemptionCsvRepository redemptionRepo;
    private final CitizenCsvRepository citizenRepo;
    private final RewardCsvRepository rewardRepo;

    // Inyección por constructor
    public RedemptionService(RedemptionCsvRepository redemptionRepo, 
                             CitizenCsvRepository citizenRepo, 
                             RewardCsvRepository rewardRepo) {
        this.redemptionRepo = redemptionRepo;
        this.citizenRepo = citizenRepo;
        this.rewardRepo = rewardRepo;
    }

    public List<Redemption> list() { return redemptionRepo.findAll(); }

    public Optional<Redemption> get(long id) { return redemptionRepo.findById(id); }

    public Redemption create(long citizenId, long rewardId) {

        Citizen citizen = citizenRepo.findById(citizenId)
                .orElseThrow(() -> new IllegalArgumentException("Citizen not found"));

        Reward reward = rewardRepo.findById(rewardId)
                .orElseThrow(() -> new IllegalArgumentException("Reward not found"));

        // Validar puntos
        if (citizen.getPoints() < reward.getCostPoints()) {
            throw new IllegalArgumentException("Citizen does not have enough points");
        }

        // Validar stock
        if (reward.getStock() <= 0) {
            throw new IllegalArgumentException("Reward has no stock");
        }

        // Descontar puntos y stock
        citizen.addPoints(-reward.getCostPoints());
        reward.setStock(reward.getStock() - 1);

        citizenRepo.save(citizen);
        rewardRepo.save(reward);

        Redemption r = new Redemption();
        r.setId(redemptionRepo.nextId());
        r.setCitizen(citizen);
        r.setReward(reward);
        r.setStatus(RedemptionStatus.REQUESTED);
        r.setDate(LocalDate.now());

        redemptionRepo.save(r);

        return r;
    }
}