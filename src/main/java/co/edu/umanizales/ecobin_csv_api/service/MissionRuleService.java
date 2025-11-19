package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.MissionRule;
import co.edu.umanizales.ecobin_csv_api.repository.MissionRuleCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio para MissionRule.
 */
@Service
public class MissionRuleService {

    private final MissionRuleCsvRepository repo;

    public MissionRuleService(MissionRuleCsvRepository repo) {
        this.repo = repo;
    }

    public List<MissionRule> list() {
        return repo.findAll();
    }

    public MissionRule getById(long id) {
        return repo.findById(id).orElse(null);
    }

    public MissionRule create(MissionRule rule) {
        if (rule.getTargetKg() <= 0) {
            throw new IllegalArgumentException("Target kg must be greater than 0");
        }
        if (rule.getIsoPeriod() == null || rule.getIsoPeriod().isBlank()) {
            throw new IllegalArgumentException("ISO period is required");
        }
        return repo.save(rule);
    }

    public MissionRule update(long id, MissionRule rule) {
        MissionRule existing = getById(id);
        if (existing == null) {
            return null;
        }
        if (rule.getTargetKg() <= 0) {
            throw new IllegalArgumentException("Target kg must be greater than 0");
        }
        if (rule.getIsoPeriod() == null || rule.getIsoPeriod().isBlank()) {
            throw new IllegalArgumentException("ISO period is required");
        }
        existing.setWasteType(rule.getWasteType());
        existing.setTargetKg(rule.getTargetKg());
        existing.setIsoPeriod(rule.getIsoPeriod());
        return repo.save(existing);
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }
}
