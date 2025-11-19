package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.EcoBinPoint;
import co.edu.umanizales.ecobin_csv_api.service.EcoBinPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ecobin-points")
public class EcoBinPointController {

    private final EcoBinPointService service;

    public EcoBinPointController(EcoBinPointService service) {
        this.service = service;
    }

    @GetMapping
    public List<EcoBinPoint> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EcoBinPoint> get(@PathVariable long id) {
        Optional<EcoBinPoint> ecoBinPoint = service.get(id);
        
        if (ecoBinPoint.isPresent()) {
            return ResponseEntity.ok(ecoBinPoint.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EcoBinPoint p) {
        try {
            return ResponseEntity.ok(service.create(p));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody EcoBinPoint p) {
        try {
            Optional<EcoBinPoint> updated = service.update(id, p);
            
            if (updated.isPresent()) {
                return ResponseEntity.ok(updated.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
