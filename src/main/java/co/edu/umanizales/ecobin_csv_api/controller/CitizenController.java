package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;
import co.edu.umanizales.ecobin_csv_api.service.CitizenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST para gestionar ciudadanos.
 */
@RestController
@RequestMapping("/api/citizens")
public class CitizenController {

    // Service simple, sin inyección todavía para que sea de tu nivel
    private final CitizenService service = new CitizenService();

    /** GET /api/citizens */
    @GetMapping
    public List<Citizen> list() {
        return service.list();
    }

    /** GET /api/citizens/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Citizen> get(@PathVariable Long id) {
        return service.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/citizens */
    @PostMapping
public ResponseEntity<?> create(@RequestBody Citizen citizen) {
    try {
        Citizen created = service.create(citizen);
        return ResponseEntity.ok(created);
    } catch (IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}




    /** PUT /api/citizens/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Citizen> update(@PathVariable Long id,
                                          @RequestBody Citizen citizen) {
        return service.update(id, citizen)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/citizens/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
