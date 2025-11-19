package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.Badge;
import co.edu.umanizales.ecobin_csv_api.service.BadgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para /api/badges
 * Expone el CRUD completo.
 */
@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final BadgeService service;

    public BadgeController(BadgeService service) {
        this.service = service;
    }

    // GET /api/badges
    @GetMapping
    public List<Badge> list() {
        return service.list();
    }

    // GET /api/badges/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Badge> get(@PathVariable long id) {
        Badge badge = service.getById(id);
        return badge != null
                ? ResponseEntity.ok(badge)
                : ResponseEntity.notFound().build();
    }

    // POST /api/badges
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Badge badge) {
        try {
            Badge created = service.create(badge);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // PUT /api/badges/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Badge badge) {
        Badge updated = service.update(id, badge);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/badges/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean deleted = service.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
