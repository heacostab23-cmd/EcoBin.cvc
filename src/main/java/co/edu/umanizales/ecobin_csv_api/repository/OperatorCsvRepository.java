package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.OperatorCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.core.Operator;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio CSV para Operator.
 * Aquí hacemos TODAS las operaciones sobre el archivo operators.csv
 */
@Repository
public class OperatorCsvRepository {

    private final Path path = Paths.get("src/main/resources/data/operators.csv");
    private final OperatorCsvMapper mapper = new OperatorCsvMapper();

    public OperatorCsvRepository() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "id,document,firstName,lastName,email,userId\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating operators.csv", e);
        }
    }

    public List<Operator> findAll() {
        List<Operator> result = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] columns = line.split(",", -1);
                result.add(mapper.fromCsv(columns));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading operators.csv", e);
        }
        return result;
    }

    public Optional<Operator> findById(long id) {
        List<Operator> all = findAll();
        for (Operator op : all) {
            if (op.getId() == id) {
                return Optional.of(op);
            }
        }
        return Optional.empty();
    }

    public Optional<Operator> findByDocument(String document) {
        List<Operator> all = findAll();
        for (Operator op : all) {
            if (op.getDocument() != null && op.getDocument().equals(document)) {
                return Optional.of(op);
            }
        }
        return Optional.empty();
    }

    public void writeAll(List<Operator> operators) {
        List<String> lines = new ArrayList<>();
        lines.add("id,document,firstName,lastName,email,userId");
        for (Operator op : operators) {
            String[] cols = mapper.toCsv(op);
            lines.add(String.join(",", cols));
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing operators.csv", e);
        }
    }

    public long nextId() {
        long max = 0;
        for (Operator op : findAll()) {
            if (op.getId() > max) {
                max = op.getId();
            }
        }
        return max + 1;
    }

    public Operator save(Operator entity) {
        List<Operator> all = new ArrayList<>(findAll());

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
        List<Operator> all = new ArrayList<>(findAll());
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
