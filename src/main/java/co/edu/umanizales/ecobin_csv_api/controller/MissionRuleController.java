package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.MissionRule;
import co.edu.umanizales.ecobin_csv_api.service.MissionRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para /api/mission-rules
 * Expone el CRUD completo.
 */
@RestController
@RequestMapping("/api/mission-rules")
public class MissionRuleController {

    private final MissionRuleService service;

    public MissionRuleController(MissionRuleService service) {
        this.service = service;
    }

    @GetMapping
    public List<MissionRule> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionRule> get(@PathVariable long id) {
        MissionRule rule = service.getById(id);
        return rule != null
                ? ResponseEntity.ok(rule)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MissionRule rule) {
        try {
            MissionRule created = service.create(rule);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody MissionRule rule) {
        try {
            MissionRule updated = service.update(id, rule);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean deleted = service.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
