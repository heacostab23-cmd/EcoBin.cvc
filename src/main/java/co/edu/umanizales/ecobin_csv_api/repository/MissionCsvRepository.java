package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.MissionCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.Mission;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio CSV para Mission.
 * Aquí hacemos TODAS las operaciones sobre el archivo missions.csv
 */
@Repository
public class MissionCsvRepository {

    private final Path path = Paths.get("src/main/resources/data/missions.csv");
    private final MissionCsvMapper mapper = new MissionCsvMapper();

    public MissionCsvRepository() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "id,name,ruleId,status\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating missions.csv", e);
        }
    }

    public List<Mission> findAll() {
        List<Mission> result = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] columns = line.split(",", -1);
                result.add(mapper.fromCsv(columns));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading missions.csv", e);
        }
        return result;
    }

    public Optional<Mission> findById(long id) {
        List<Mission> all = findAll();
        for (Mission m : all) {
            if (m.getId() == id) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    public void writeAll(List<Mission> missions) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,ruleId,status");
        for (Mission m : missions) {
            String[] cols = mapper.toCsv(m);
            lines.add(String.join(",", cols));
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing missions.csv", e);
        }
    }

    public long nextId() {
        long max = 0;
        for (Mission m : findAll()) {
            if (m.getId() > max) {
                max = m.getId();
            }
        }
        return max + 1;
    }

    public Mission save(Mission entity) {
        List<Mission> all = new ArrayList<>(findAll());

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
        List<Mission> all = new ArrayList<>(findAll());
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
