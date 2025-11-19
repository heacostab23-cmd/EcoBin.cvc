package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.core.Operator;
import co.edu.umanizales.ecobin_csv_api.repository.OperatorCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio para Operator.
 * Aquí validamos reglas como: documento único.
 */
@Service
public class OperatorService {

    private final OperatorCsvRepository repo;

    public OperatorService(OperatorCsvRepository repo) {
        this.repo = repo;
    }

    public List<Operator> list() {
        return repo.findAll();
    }

    public Operator getById(long id) {
        return repo.findById(id).orElse(null);
    }

    public Operator create(Operator operator) {
        if (operator.getDocument() == null || operator.getDocument().isBlank()) {
            throw new IllegalArgumentException("Document is required");
        }
        if (repo.findByDocument(operator.getDocument()).isPresent()) {
            throw new IllegalArgumentException("Document already exists");
        }
        return repo.save(operator);
    }

    public Operator update(long id, Operator operator) {
        Operator existing = getById(id);
        if (existing == null) {
            return null;
        }
        if (operator.getDocument() == null || operator.getDocument().isBlank()) {
            throw new IllegalArgumentException("Document is required");
        }
        existing.setDocument(operator.getDocument());
        existing.setFirstName(operator.getFirstName());
        existing.setLastName(operator.getLastName());
        existing.setEmail(operator.getEmail());
        existing.setUser(operator.getUser());
        return repo.save(existing);
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }
}
