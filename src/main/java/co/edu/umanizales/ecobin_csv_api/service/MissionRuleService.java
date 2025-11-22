package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.MissionRule;
import co.edu.umanizales.ecobin_csv_api.model.WasteType;
import co.edu.umanizales.ecobin_csv_api.repository.MissionRuleCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio para MissionRule.
 */
@Service
public class MissionRuleService {

    private final MissionRuleCsvRepository repo;
    private final WasteTypeService wasteTypeService;  // 👈 NUEVO

    public MissionRuleService(MissionRuleCsvRepository repo,
                              WasteTypeService wasteTypeService) {  // 👈 INYECTADO
        this.repo = repo;
        this.wasteTypeService = wasteTypeService;
    }

    /**
     * Cargar el WasteType completo según el id que viene desde CSV.
     */
    private void loadWasteType(MissionRule rule) {
        if (rule.getWasteType() != null && rule.getWasteType().getId() > 0) {

            WasteType fullWaste = wasteTypeService.list().stream()
                    .filter(w -> w.getId() == rule.getWasteType().getId())
                    .findFirst()
                    .orElse(null);

            if (fullWaste != null) {
                rule.setWasteType(fullWaste);
            }
        }
    }

    /** Listar reglas con wasteType completo */
    public List<MissionRule> list() {
        List<MissionRule> rules = repo.findAll();
        rules.forEach(this::loadWasteType);
        return rules;
    }

    /** Buscar regla por id con wasteType completo */
    public MissionRule getById(long id) {
        return repo.findById(id)
                .map(rule -> {
                    loadWasteType(rule);
                    return rule;
                })
                .orElse(null);
    }

    public MissionRule create(MissionRule rule) {
        if (rule.getTargetKg() <= 0) {
            throw new IllegalArgumentException("Target kg must be greater than 0");
        }
        if (rule.getIsoPeriod() == null || rule.getIsoPeriod().isBlank()) {
            throw new IllegalArgumentException("ISO period is required");
        }

        MissionRule saved = repo.save(rule);
        loadWasteType(saved);
        return saved;
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

        MissionRule saved = repo.save(existing);
        loadWasteType(saved);
        return saved;
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }
}
