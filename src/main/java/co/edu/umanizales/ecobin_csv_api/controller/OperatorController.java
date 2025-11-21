package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.core.Operator;
import co.edu.umanizales.ecobin_csv_api.service.OperatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST para /api/operators
 * Expone el CRUD completo.
 */
@RestController
@RequestMapping("/api/operators")
public class OperatorController {

    private final OperatorService service;

    public OperatorController(OperatorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Operator> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operator> get(@PathVariable long id) {
        Operator operator = service.getById(id);
        return operator != null
                ? ResponseEntity.ok(operator)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Operator operator, BindingResult bindingResult) {
        // Validar campos obligatorios
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.append(error.getDefaultMessage()).append("; ")
            );
            return ResponseEntity.badRequest().body(errors.toString());
        }
        
        try {
            Operator created = service.create(operator);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody Operator operator, BindingResult bindingResult) {
        // Validar campos obligatorios
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.append(error.getDefaultMessage()).append("; ")
            );
            return ResponseEntity.badRequest().body(errors.toString());
        }
        
        try {
            Operator updated = service.update(id, operator);
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
