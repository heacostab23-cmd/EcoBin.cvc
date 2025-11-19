package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.Reward;
import co.edu.umanizales.ecobin_csv_api.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para /api/rewards
 * Expone el CRUD completo de Reward.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService service;

    public RewardController(RewardService service) {
        this.service = service;
    }

    // GET /api/rewards  → lista todas las recompensas
    @GetMapping
    public List<Reward> list() {
        return service.list();
    }

    // GET /api/rewards/{id}  → obtiene una recompensa por id
    @GetMapping("/{id}")
    public ResponseEntity<Reward> get(@PathVariable long id) {
        Optional<Reward> reward = service.getById(id);
        
        if (reward.isPresent()) {
            return ResponseEntity.ok(reward.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/rewards  → crea una nueva recompensa
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Reward reward) {
        try {
            Reward created = service.create(reward);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            // Si las validaciones fallan, devolvemos 400 con el mensaje
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // PUT /api/rewards/{id} → actualiza una recompensa
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody Reward reward) {
        try {
            Reward updated = service.update(id, reward);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // DELETE /api/rewards/{id} → elimina una recompensa
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
