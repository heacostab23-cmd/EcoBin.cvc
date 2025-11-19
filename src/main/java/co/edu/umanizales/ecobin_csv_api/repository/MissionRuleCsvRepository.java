package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.MissionRuleCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.MissionRule;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio CSV para MissionRule.
 * Aquí hacemos TODAS las operaciones sobre el archivo mission_rules.csv
 */
@Repository
public class MissionRuleCsvRepository {

    private final Path path = Paths.get("src/main/resources/data/mission_rules.csv");
    private final MissionRuleCsvMapper mapper = new MissionRuleCsvMapper();

    public MissionRuleCsvRepository() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "id,wasteTypeId,targetKg,isoPeriod\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating mission_rules.csv", e);
        }
    }

    public List<MissionRule> findAll() {
        List<MissionRule> result = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] columns = line.split(",", -1);
                result.add(mapper.fromCsv(columns));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading mission_rules.csv", e);
        }
        return result;
    }

    public Optional<MissionRule> findById(long id) {
        List<MissionRule> all = findAll();
        for (MissionRule mr : all) {
            if (mr.getId() == id) {
                return Optional.of(mr);
            }
        }
        return Optional.empty();
    }

    public void writeAll(List<MissionRule> rules) {
        List<String> lines = new ArrayList<>();
        lines.add("id,wasteTypeId,targetKg,isoPeriod");
        for (MissionRule mr : rules) {
            String[] cols = mapper.toCsv(mr);
            lines.add(String.join(",", cols));
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing mission_rules.csv", e);
        }
    }

    public long nextId() {
        long max = 0;
        for (MissionRule mr : findAll()) {
            if (mr.getId() > max) {
                max = mr.getId();
            }
        }
        return max + 1;
    }

    public MissionRule save(MissionRule entity) {
        List<MissionRule> all = new ArrayList<>(findAll());

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
        List<MissionRule> all = new ArrayList<>(findAll());
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
