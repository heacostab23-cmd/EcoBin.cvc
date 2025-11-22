package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.core.Operator;
import co.edu.umanizales.ecobin_csv_api.repository.OperatorCsvRepository;
import co.edu.umanizales.ecobin_csv_api.repository.UserCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Lógica de negocio para Operator.
 * Aquí validamos reglas como: documento único.
 */
@Service
public class OperatorService {

    private final OperatorCsvRepository repo;
    private final UserCsvRepository userRepo;   // Repo de usuarios

    public OperatorService(OperatorCsvRepository repo,
                           UserCsvRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    /**
     * Cargar el User asociado al Operator.
     * 1. Si tiene userId (user con id) → buscamos por id.
     * 2. Si no, intentamos por email.
     */
    private void loadUser(Operator operator) {
        // 1) Intentar con el id del user (userId en el CSV)
        if (operator.getUser() != null && operator.getUser().getId() > 0) {
            userRepo.findById(operator.getUser().getId())
                    .ifPresent(operator::setUser);
            return; // ya lo cargamos, no hace falta seguir
        }

        // 2) Si no tiene userId, intentamos por email
        if (operator.getEmail() != null && !operator.getEmail().isBlank()) {
            userRepo.findByEmail(operator.getEmail())
                    .ifPresent(operator::setUser);
        }
    }

    /** Listar operadores con user cargado. */
    public List<Operator> list() {
        List<Operator> operators = repo.findAll();
        for (Operator op : operators) {
            loadUser(op);
        }
        return operators;
    }

    /** Buscar operador por ID con user cargado. */
    public Operator getById(long id) {
        Optional<Operator> op = repo.findById(id);
        if (op.isPresent()) {
            loadUser(op.get());
            return op.get();
        }
        return null;
    }

    /** Crear operador. */
    public Operator create(Operator operator) {
        if (operator.getDocument() == null || operator.getDocument().isBlank()) {
            throw new IllegalArgumentException("Document is required");
        }
        if (repo.findByDocument(operator.getDocument()).isPresent()) {
            throw new IllegalArgumentException("Document already exists");
        }

        Operator saved = repo.save(operator);
        // Si ya tiene userId o email, intentamos cargar el user completo
        loadUser(saved);
        return saved;
    }

    /** Actualizar operador. */
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
        // No tomamos operator.getUser(): el user completo se carga con loadUser
        loadUser(existing);

        return repo.save(existing);
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }
}
