package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.Reading;
import co.edu.umanizales.ecobin_csv_api.model.report.ReadingReportDTO;  // 👈 DTO del reporte
import co.edu.umanizales.ecobin_csv_api.service.ReadingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;                         // 👈 para manejar fechas
import java.time.format.DateTimeParseException;    // 👈 para capturar errores de formato
import java.util.List;

/**
 * Controlador REST para /api/readings
 * Expone el CRUD completo y el reporte por rango de fechas.
 */
@RestController
@RequestMapping("/api/readings")
public class ReadingController {

    private final ReadingService service;

    public ReadingController(ReadingService service) {
        this.service = service;
    }

    // ----------------------------------------------------
    //  CRUD BÁSICO
    // ----------------------------------------------------

    // GET /api/readings  → lista todas las lecturas
    @GetMapping
    public List<Reading> list() {
        return service.list();
    }

    // GET /api/readings/{id} → obtiene una lectura por id
    @GetMapping("/{id}")
    public ResponseEntity<Reading> get(@PathVariable long id) {
        Reading reading = service.getById(id);
        return reading != null
                ? ResponseEntity.ok(reading)
                : ResponseEntity.notFound().build();
    }

    // POST /api/readings → crea una nueva lectura
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Reading reading) {
        try {
            Reading created = service.create(reading);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            // Si las validaciones fallan (usuario inactivo, operador, grams <= 0, etc.)
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // PUT /api/readings/{id} → actualiza una lectura existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Reading reading) {
        try {
            Reading updated = service.update(id, reading);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // DELETE /api/readings/{id} → elimina una lectura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean deleted = service.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // ----------------------------------------------------
    //  NUEVO ENDPOINT: REPORTE POR RANGO DE FECHAS
    // ----------------------------------------------------

    /**
     * GET /api/readings/report?fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD
     *
     * Ejemplo en Postman:
     *   GET http://localhost:8080/api/readings/report?fechaInicio=2025-11-10&fechaFin=2025-11-12
     *
     * - fechaInicio y fechaFin llegan como texto (String) en la URL.
     * - Aquí las convertimos a LocalDate.
     * - Llamamos a ReadingService.reportByDate(fechaInicio, fechaFin).
     * - Devolvemos una lista de ReadingReportDTO con el formato del reporte.
     */
    @GetMapping("/report")
    public ResponseEntity<?> reportByDate(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin
    ) {
        try {
            // 1. Convertir cadenas de texto a LocalDate
            LocalDate fi = LocalDate.parse(fechaInicio);
            LocalDate ff = LocalDate.parse(fechaFin);

            // 2. Delegar toda la lógica de negocio al servicio
            List<ReadingReportDTO> reporte = service.reportByDate(fi, ff);

            // 3. Responder 200 OK con la lista de reportes por día
            return ResponseEntity.ok(reporte);

        } catch (DateTimeParseException ex) {
            // Si las fechas vienen en un formato incorrecto
            return ResponseEntity.badRequest()
                    .body("Las fechas deben tener el formato YYYY-MM-DD. Ejemplo: 2025-11-10");

        } catch (IllegalArgumentException ex) {
            // Aquí atrapamos:
            // - "La fecha final no puede ser menor que la fecha inicial"
            // - o cualquier otra validación que haga el servicio
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
