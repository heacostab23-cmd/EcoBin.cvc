package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.UserCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.core.User;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio CSV para User.
 * Aquí hacemos TODAS las operaciones sobre el archivo users.csv
 */
@Repository
public class UserCsvRepository {

    private final Path path = Paths.get("src/main/resources/data/users.csv");
    private final UserCsvMapper mapper = new UserCsvMapper();

    public UserCsvRepository() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "id,email,passwordHash,active\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating users.csv", e);
        }
    }

    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] columns = line.split(",", -1);
                result.add(mapper.fromCsv(columns));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading users.csv", e);
        }
        return result;
    }

    public Optional<User> findById(long id) {
        List<User> all = findAll();
        for (User u : all) {
            if (u.getId() == id) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        List<User> all = findAll();
        for (User u : all) {
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(email)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public void writeAll(List<User> users) {
        List<String> lines = new ArrayList<>();
        lines.add("id,email,passwordHash,active");
        for (User u : users) {
            String[] cols = mapper.toCsv(u);
            lines.add(String.join(",", cols));
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing users.csv", e);
        }
    }

    public long nextId() {
        long max = 0;
        for (User u : findAll()) {
            if (u.getId() > max) {
                max = u.getId();
            }
        }
        return max + 1;
    }

    public User save(User entity) {
        List<User> all = new ArrayList<>(findAll());

        if (entity.getId() == 0) {
            entity.setId(nextId());
            all.add(entity);
        } else {
            boolean updated = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == entity.getId()) {
                    all.set(i, entity);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                all.add(entity);
            }
        }

        writeAll(all);
        return entity;
    }

    public boolean deleteById(long id) {
        List<User> all = new ArrayList<>(findAll());
        boolean removed = false;

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == id) {
                all.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            writeAll(all);
        }
        return removed;
    }
}
