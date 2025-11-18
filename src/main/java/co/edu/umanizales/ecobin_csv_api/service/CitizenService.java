package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;
import co.edu.umanizales.ecobin_csv_api.repository.CitizenCsvRepository;

import java.util.List;
import java.util.Optional;

/**
 * Capa de negocio para Citizen.
 * Aplica validaciones sencillas antes de guardar en CSV.
 */
public class CitizenService {

    private final CitizenCsvRepository repo = new CitizenCsvRepository();

    /** Listar todos los ciudadanos. */
    public List<Citizen> list() {
        return repo.findAll();
    }

    /** Buscar un ciudadano por id. */
    public Optional<Citizen> get(Long id) {
        return repo.findById(id);
    }

    /** Crear un nuevo ciudadano. */
    public Citizen create(Citizen c) {
    // 1. Validaciones básicas
    if (c.getDocument() == null || c.getDocument().isBlank()) {
        throw new IllegalArgumentException("document is required");
    }
    if (c.getEmail() == null || !c.getEmail().contains("@")) {
        throw new IllegalArgumentException("email format invalid");
    }

    // 2. Regla de negocio: NO permitir duplicados por document y email
    for (Citizen existing : repo.findAll()) {
        if (existing.getDocument().equals(c.getDocument())) {
            throw new IllegalArgumentException("Citizen with this document already exists");
        }
        if (existing.getEmail().equalsIgnoreCase(c.getEmail())) {
            throw new IllegalArgumentException("Citizen with this email already exists");
        }
    }

    // 3. Preparar el objeto antes de guardar
    c.setId(0L); // 0 = sin id todavía

    if (c.getPoints() < 0) {
        c.setPoints(0);
    }

    // 4. Guardar en el CSV
    return repo.save(c);
}


    /** Actualizar un ciudadano existente. */
    public Optional<Citizen> update(Long id, Citizen data) {
        return repo.findById(id).map(existing -> {
            existing.setDocument(data.getDocument());
            existing.setFirstName(data.getFirstName());
            existing.setLastName(data.getLastName());
            existing.setEmail(data.getEmail());
            existing.setPoints(Math.max(0, data.getPoints()));
            return repo.save(existing);
        });
    }

    /** Eliminar un ciudadano por id. */
    public boolean delete(Long id) {
        // Más adelante puedes impedir borrar si tiene lecturas o canjes
        return repo.deleteById(id);
    }
}
