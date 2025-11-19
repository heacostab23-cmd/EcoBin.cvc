package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.Badge;
import co.edu.umanizales.ecobin_csv_api.repository.BadgeCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de lógica para Badge.
 * Aquí NO tocamos archivos, solo usamos el repositorio.
 */
@Service
public class BadgeService {

    private final BadgeCsvRepository repo;

    public BadgeService(BadgeCsvRepository repo) {
        this.repo = repo;
    }

    public List<Badge> list() {
        return repo.findAll();
    }

    public Badge getById(long id) {
        return repo.findById(id).orElse(null);
    }

    public Badge create(Badge badge) {
        // puedes añadir validaciones básicas, por ejemplo nombre obligatorio
        if (badge.getName() == null || badge.getName().isBlank()) {
            throw new IllegalArgumentException("Badge name is required");
        }
        return repo.save(badge);
    }

    public Badge update(long id, Badge badge) {
        Badge existing = getById(id);
        if (existing == null) {
            return null; // el controller decidirá si responde 404
        }
        existing.setName(badge.getName());
        existing.setDescription(badge.getDescription());
        existing.setRequiredPoints(badge.getRequiredPoints());
        return repo.save(existing);
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }
}
