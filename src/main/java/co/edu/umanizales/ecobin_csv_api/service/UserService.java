package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.core.User;
import co.edu.umanizales.ecobin_csv_api.repository.UserCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio para User.
 * Aquí validamos reglas como: email único.
 */
@Service
public class UserService {

    private final UserCsvRepository repo;

    public UserService(UserCsvRepository repo) {
        this.repo = repo;
    }

    public List<User> list() {
        return repo.findAll();
    }

    public User getById(long id) {
        return repo.findById(id).orElse(null);
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new IllegalArgumentException("Password hash is required");
        }
        return repo.save(user);
    }

    public User update(long id, User user) {
        User existing = getById(id);
        if (existing == null) {
            return null;
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        existing.setEmail(user.getEmail());
        existing.setPasswordHash(user.getPasswordHash());
        existing.setActive(user.isActive());
        return repo.save(existing);
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }
}
