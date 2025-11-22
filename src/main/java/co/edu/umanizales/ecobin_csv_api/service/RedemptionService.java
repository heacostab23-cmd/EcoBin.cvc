package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.Redemption;
import co.edu.umanizales.ecobin_csv_api.model.RedemptionStatus;
import co.edu.umanizales.ecobin_csv_api.model.Reward;
import co.edu.umanizales.ecobin_csv_api.model.Role;
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
 * Regla:
 * - Debe tener puntos suficientes
 * - Debe haber stock
 * - Solo ciudadanos activos pueden canjear
 * - Usuarios con rol OPERATOR no pueden canjear
 */
@Service
public class RedemptionService {

    private final RedemptionCsvRepository redemptionRepo;
    private final CitizenCsvRepository citizenRepo;
    private final RewardCsvRepository rewardRepo;

    private final CitizenService citizenService;
    private final RewardService rewardService;

    // Inyección por constructor
    public RedemptionService(RedemptionCsvRepository redemptionRepo,
                             CitizenCsvRepository citizenRepo,
                             RewardCsvRepository rewardRepo,
                             CitizenService citizenService,
                             RewardService rewardService) {
        this.redemptionRepo = redemptionRepo;
        this.citizenRepo = citizenRepo;
        this.rewardRepo = rewardRepo;
        this.citizenService = citizenService;
        this.rewardService = rewardService;
    }

    /**
     * Carga citizen y reward completos dentro del Redemption.
     */
    private void loadRelations(Redemption r) {
        if (r.getCitizen() != null && r.getCitizen().getId() > 0) {
            citizenService.getById(r.getCitizen().getId())
                    .ifPresent(r::setCitizen);
        }
        if (r.getReward() != null && r.getReward().getId() > 0) {
            rewardService.getById(r.getReward().getId())
                    .ifPresent(r::setReward);
        }
    }

    public List<Redemption> list() {
        List<Redemption> all = redemptionRepo.findAll();
        all.forEach(this::loadRelations);
        return all;
    }

    public Optional<Redemption> get(long id) {
        return redemptionRepo.findById(id)
                .map(r -> {
                    loadRelations(r);
                    return r;
                });
    }

    /**
     * Crear un canje.
     * Los parámetros suelen venir en la URL: /api/redemptions/{citizenId}/{rewardId}
     */
    public Redemption create(long citizenId, long rewardId) {

        // Cargar citizen COMPLETO (con user, roles y estado)
        Citizen citizen = citizenService.getById(citizenId)
                .orElseThrow(() -> new IllegalArgumentException("Citizen not found"));

        // ----- VALIDACIONES de usuario -----
        if (citizen.getUser() != null) {

            // 1. Si es OPERADOR -> no puede canjear
            if (citizen.getUser().getRoles() != null &&
                    citizen.getUser().getRoles().contains(Role.OPERATOR)) {
                throw new IllegalArgumentException(
                        "No puedes solicitar recompensas porque tu usuario es un operador."
                );
            }

            // 2. Si está inactivo -> no puede canjear
            if (!citizen.getUser().isActive()) {
                throw new IllegalArgumentException(
                        "Tu usuario está inactivo, no puedes solicitar recompensas."
                );
            }
        }
        // -----------------------------------

        // Cargar reward normal desde el repo (para poder modificar stock)
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

        // Guardar cambios en CSV
        citizenRepo.save(citizen);
        rewardRepo.save(reward);

        // Crear el canje
        Redemption r = new Redemption();
        r.setId(redemptionRepo.nextId());
        r.setCitizen(citizen);
        r.setReward(reward);
        r.setStatus(RedemptionStatus.REQUESTED);
        r.setDate(LocalDate.now());

        redemptionRepo.save(r);

        // Enriquecer relaciones para la respuesta
        loadRelations(r);

        return r;
    }
}
