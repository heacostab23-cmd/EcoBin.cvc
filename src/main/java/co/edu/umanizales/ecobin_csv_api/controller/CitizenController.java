package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;
import co.edu.umanizales.ecobin_csv_api.service.CitizenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para /api/citizens.
 * Expone el CRUD completo para Citizen.
 */
@RestController
@RequestMapping("/api/citizens")
public class CitizenController {

    // 1. Definimos el atributo, pero SIN crear el objeto aquí
    private final CitizenService citizenService;

    // 2. Spring creará CitizenService y nos lo inyectará por el constructor
    public CitizenController(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    // ================== GET: listar todos ==================
    @GetMapping
    public List<Citizen> list() {
        return citizenService.list();
    }

    // ================== GET: buscar por id ==================
    @GetMapping("/{id}")
    public ResponseEntity<Citizen> get(@PathVariable long id) {
        Optional<Citizen> citizen = citizenService.getById(id);
        
        if (citizen.isPresent()) {
            return ResponseEntity.ok(citizen.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== POST: crear ==================
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Citizen citizen) {
        try {
            Citizen created = citizenService.create(citizen);
            // 200 OK con el ciudadano creado
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            // 400 Bad Request con el mensaje de error (por ejemplo "Citizen with document ... already exists with id X")
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ================== PUT: actualizar ==================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Citizen citizen) {
        try {
            Citizen updated = citizenService.update(id, citizen);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ================== DELETE: borrar ==================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        citizenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
