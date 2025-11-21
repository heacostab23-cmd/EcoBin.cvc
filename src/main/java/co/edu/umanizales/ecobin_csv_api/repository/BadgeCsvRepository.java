package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.BadgeCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.Badge;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio CSV para Badge.
 * Aquí hacemos TODAS las operaciones sobre el archivo badges.csv
 */
@Repository
public class BadgeCsvRepository {

    private final Path path = Paths.get("src/main/resources/data/badges.csv");
    private final BadgeCsvMapper mapper = new BadgeCsvMapper();

    public BadgeCsvRepository() {
        ensureFileExists();
    }

    /**
     * Verifica que el archivo exista.
     * Si no existe, lo crea junto con la cabecera.
     */
    private void ensureFileExists() {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "id,name,description,requiredPoints\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating badges.csv", e);
        }
    }

    /**
     * Lee TODO el archivo badges.csv y devuelve la lista de insignias.
     */
    public List<Badge> findAll() {
        List<Badge> result = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            // línea 0 es la cabecera, empezamos en la 1
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] columns = line.split(",", -1);
                result.add(mapper.fromCsv(columns));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading badges.csv", e);
        }
        return result;
    }

    /**
     * Busca una insignia por su id.
     */
    public Optional<Badge> findById(long id) {
        List<Badge> all = findAll();
        for (Badge b : all) {
            if (b.getId() == id) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    /**
     * Busca una insignia por su nombre.
     */
    public Badge findByName(String name) {
        List<Badge> all = findAll();
        return all.stream()
                .filter(b -> b.getName() != null && b.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Escribe TODAS las insignias al archivo CSV (sobrescribe).
     */
    public void writeAll(List<Badge> badges) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,description,requiredPoints");
        for (Badge b : badges) {
            String[] cols = mapper.toCsv(b);
            lines.add(String.join(",", cols));
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing badges.csv", e);
        }
    }

    /**
     * Calcula el próximo id disponible.
     */
    public long nextId() {
        long max = 0;
        for (Badge b : findAll()) {
            if (b.getId() > max) {
                max = b.getId();
            }
        }
        return max + 1;
    }

    /**
     * Guarda una insignia.
     */
    public Badge save(Badge entity) {
        List<Badge> all = new ArrayList<>(findAll());

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

    /**
     * Elimina una insignia por id.
     */
    public boolean deleteById(long id) {
        List<Badge> all = new ArrayList<>(findAll());
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