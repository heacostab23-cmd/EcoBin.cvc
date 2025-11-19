package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.Reward;
import co.edu.umanizales.ecobin_csv_api.repository.RewardCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Capa de negocio para Reward.
 * Aquí validamos reglas sencillas antes de guardar en el CSV.
 */
@Service
public class RewardService {

    private final RewardCsvRepository repository;

    // Inyección por constructor (Spring se encarga)
    public RewardService(RewardCsvRepository repository) {
        this.repository = repository;
    }

    public List<Reward> list() {
        return repository.findAll();
    }

    public Optional<Reward> getById(long id) {
        return repository.findById(id);
    }

    /**
     * Crea una nueva recompensa con validaciones.
     */
    public Reward create(Reward reward) {
        validate(reward);

        // Regla extra: no repetir nombre
        Optional<Reward> existing = repository.findByName(reward.getName());
        if (existing.isPresent()) {
            Reward found = existing.get();
            throw new IllegalArgumentException(
                    "Reward with name '" + found.getName() +
                            "' already exists with id " + found.getId()
            );
        }

        // id = 0 significa "nuevo", el repositorio asigna el id real
        reward.setId(0);
        return repository.save(reward);
    }

    /**
     * Actualiza una recompensa existente.
     */
    public Reward update(long id, Reward data) {
        Optional<Reward> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Reward not found with id " + id);
        }
        
        Reward existing = optional.get();
        validate(data);

        // Verificar que el nuevo nombre no choque con otra recompensa
        Optional<Reward> other = repository.findByName(data.getName());
        if (other.isPresent() && other.get().getId() != id) {
            throw new IllegalArgumentException(
                    "Another reward with name '" + data.getName() +
                            "' already exists with id " + other.get().getId()
            );
        }

        existing.setName(data.getName());
        existing.setCostPoints(data.getCostPoints());
        existing.setStock(data.getStock());
        existing.setDescription(data.getDescription());

        return repository.save(existing);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    /**
     * Validaciones básicas de negocio.
     */
    private void validate(Reward reward) {
        if (reward.getName() == null || reward.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (reward.getCostPoints() <= 0) {
            throw new IllegalArgumentException("costPoints must be > 0");
        }
        if (reward.getStock() < 0) {
            throw new IllegalArgumentException("stock must be >= 0");
        }
    }
}
