package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.model.Reward;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio CSV para la entidad Reward (Recompensa).
 * Aquí hacemos TODAS las operaciones sobre el archivo rewards.csv
 * sin extender ninguna otra clase, para que sea fácil de entender.
 */
@Repository
public class RewardCsvRepository {

    // Ruta del archivo CSV donde se guardan las recompensas
    private final Path path = Paths.get("src/main/resources/data/rewards.csv");

    public RewardCsvRepository() {
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
                // Cabecera del archivo CSV
                Files.writeString(path,
                        "id,name,costPoints,stock,description\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating rewards.csv", e);
        }
    }

    /**
     * Convierte una fila del CSV (array de String) en un objeto Reward.
     * c[0] = id
     * c[1] = name
     * c[2] = costPoints
     * c[3] = stock
     * c[4] = description
     */
    private Reward fromCsv(String[] c) {
        Reward reward = new Reward();
        reward.setId(Long.parseLong(c[0]));
        reward.setName(c[1]);
        reward.setCostPoints(Long.parseLong(c[2]));
        reward.setStock(Integer.parseInt(c[3]));
        reward.setDescription(c[4]);
        return reward;
    }

    /**
     * Convierte un Reward a un arreglo de Strings para escribirlo en el CSV.
     */
    private String[] toCsv(Reward r) {
        return new String[]{
                String.valueOf(r.getId()),
                r.getName(),
                String.valueOf(r.getCostPoints()),
                String.valueOf(r.getStock()),
                r.getDescription() == null ? "" : r.getDescription()
        };
    }

    /**
     * Lee todo el archivo rewards.csv y devuelve la lista de recompensas.
     */
    public List<Reward> findAll() {
        List<Reward> result = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            // línea 0 es la cabecera, empezamos en la 1
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] columns = line.split(",", -1); // -1 para conservar vacíos
                result.add(fromCsv(columns));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading rewards.csv", e);
        }
        return result;
    }

    /**
     * Busca una recompensa por su id.
     */
    public Optional<Reward> findById(long id) {
        List<Reward> all = findAll();
        for (Reward r : all) {
            if (r.getId() == id) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    /**
     * Busca una recompensa por su nombre (para evitar duplicados de nombre).
     */
    public Optional<Reward> findByName(String name) {
        List<Reward> all = findAll();
        for (Reward r : all) {
            if (r.getName() != null && r.getName().equalsIgnoreCase(name)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    /**
     * Escribe TODAS las recompensas al archivo CSV (sobrescribe).
     */
    public void writeAll(List<Reward> rewards) {
        List<String> lines = new ArrayList<>();
        // Cabecera
        lines.add("id,name,costPoints,stock,description");
        // Filas
        for (Reward r : rewards) {
            String[] cols = toCsv(r);
            lines.add(String.join(",", cols));
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing rewards.csv", e);
        }
    }

    /**
     * Calcula el próximo id disponible.
     * Si no hay recompensas, devuelve 1.
     */
    public long nextId() {
        long max = 0;
        for (Reward r : findAll()) {
            if (r.getId() > max) {
                max = r.getId();
            }
        }
        return max + 1;
    }

    /**
     * Guarda una recompensa:
     * - Si id == 0 → la considera nueva y le asigna nextId().
     * - Si id != 0 → busca por id y la reemplaza.
     */
    public Reward save(Reward entity) {
        List<Reward> all = new ArrayList<>(findAll());

        if (entity.getId() == 0) {
            // Nueva recompensa
            entity.setId(nextId());
            all.add(entity);
        } else {
            // Actualización
            boolean updated = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == entity.getId()) {
                    all.set(i, entity);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                all.add(entity); // por si no estaba
            }
        }

        writeAll(all);
        return entity;
    }

    /**
     * Elimina una recompensa por id.
     */
    public void deleteById(long id) {
        List<Reward> all = new ArrayList<>(findAll());
        
        // Buscamos y eliminamos la recompensa con ese id
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == id) {
                all.remove(i);
                break;
            }
        }
        
        writeAll(all);
    }
}
