package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.Mission;
import co.edu.umanizales.ecobin_csv_api.repository.MissionCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio para Mission.
 */
@Service
public class MissionService {

    private final MissionCsvRepository repo;

    public MissionService(MissionCsvRepository repo) {
        this.repo = repo;
    }

    public List<Mission> list() {
        return repo.findAll();
    }

    public Mission getById(long id) {
        return repo.findById(id).orElse(null);
    }

    public Mission create(Mission mission) {
        if (mission.getName() == null || mission.getName().isBlank()) {
            throw new IllegalArgumentException("Mission name is required");
        }
        return repo.save(mission);
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
        return repo.save(existing);
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }
}
