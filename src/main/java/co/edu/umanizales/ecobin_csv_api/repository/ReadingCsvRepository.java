package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.ReadingCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.Reading;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio CSV para Reading.
 * Aquí hacemos TODAS las operaciones sobre el archivo readings.csv
 */
@Repository
public class ReadingCsvRepository {

    private final Path path = Paths.get("src/main/resources/data/readings.csv");
    private final ReadingCsvMapper mapper = new ReadingCsvMapper();

    public ReadingCsvRepository() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "id,pointId,wasteTypeId,citizenId,grams,date\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating readings.csv", e);
        }
    }

    public List<Reading> findAll() {
        List<Reading> result = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] columns = line.split(",", -1);
                result.add(mapper.fromCsv(columns));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading readings.csv", e);
        }
        return result;
    }

    public Optional<Reading> findById(long id) {
        List<Reading> all = findAll();
        for (Reading r : all) {
            if (r.getId() == id) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public void writeAll(List<Reading> readings) {
        List<String> lines = new ArrayList<>();
        lines.add("id,pointId,wasteTypeId,citizenId,grams,date");
        for (Reading r : readings) {
            String[] cols = mapper.toCsv(r);
            lines.add(String.join(",", cols));
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing readings.csv", e);
        }
    }

    public long nextId() {
        long max = 0;
        for (Reading r : findAll()) {
            if (r.getId() > max) {
                max = r.getId();
            }
        }
        return max + 1;
    }

    public Reading save(Reading entity) {
        List<Reading> all = new ArrayList<>(findAll());

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
        List<Reading> all = new ArrayList<>(findAll());
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
