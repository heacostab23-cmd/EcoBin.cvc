package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.WasteType;
import co.edu.umanizales.ecobin_csv_api.service.WasteTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/waste-types")
public class WasteTypeController {

    private final WasteTypeService service;

    public WasteTypeController(WasteTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<WasteType> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteType> get(@PathVariable long id) {
        Optional<WasteType> wasteType = service.get(id);
        
        if (wasteType.isPresent()) {
            return ResponseEntity.ok(wasteType.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody WasteType wt) {
        try {
            return ResponseEntity.ok(service.create(wt));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody WasteType wt) {
        try {
            Optional<WasteType> updated = service.update(id, wt);
            
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
