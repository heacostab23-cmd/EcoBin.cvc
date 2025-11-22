package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.Mission;
import co.edu.umanizales.ecobin_csv_api.model.MissionRule;
import co.edu.umanizales.ecobin_csv_api.repository.MissionCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio para Mission.
 */
@Service
public class MissionService {

    private final MissionCsvRepository repo;
    private final MissionRuleService missionRuleService; // 👈 NUEVO

    public MissionService(MissionCsvRepository repo,
                          MissionRuleService missionRuleService) { // 👈 INYECTADO
        this.repo = repo;
        this.missionRuleService = missionRuleService;
    }

    /**
     * Cargar la regla completa (con wasteType, targetKg, isoPeriod)
     * usando el id que viene desde el CSV.
     */
    private void loadRule(Mission mission) {
        if (mission.getRule() != null && mission.getRule().getId() > 0) {
            MissionRule fullRule = missionRuleService.getById(mission.getRule().getId());
            if (fullRule != null) {
                mission.setRule(fullRule);
            }
        }
    }

    /** Listar misiones con la regla completa */
    public List<Mission> list() {
        List<Mission> missions = repo.findAll();
        missions.forEach(this::loadRule);
        return missions;
    }

    /** Buscar misión por id con regla completa */
    public Mission getById(long id) {
        return repo.findById(id)
                .map(mission -> {
                    loadRule(mission);
                    return mission;
                })
                .orElse(null);
    }

    public Mission create(Mission mission) {
        if (mission.getName() == null || mission.getName().isBlank()) {
            throw new IllegalArgumentException("Mission name is required");
        }
        Mission saved = repo.save(mission);
        loadRule(saved);
        return saved;
    }

    public Mission update(long id, Mission mission) {
        Mission existing = getById(id);
        if (existing == null) {
            return null;
        }
        if (mission.getName() == null || mission.getName().isBlank()) {
            throw new IllegalArgumentException("Mission name is required");
        }
        existing.setName(mission.getName());
        existing.setRule(mission.getRule());
        existing.setStatus(mission.getStatus());

        Mission saved = repo.save(existing);
        loadRule(saved);
        return saved;
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }
}
