package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.RedemptionCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.Redemption;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio CSV para la entidad Redemption (Canje).
 * Aquí hacemos TODAS las operaciones sobre el archivo redemptions.csv
 */
@Repository
public class RedemptionCsvRepository {

    private final Path path = Paths.get("src/main/resources/data/redemptions.csv");
    private final RedemptionCsvMapper mapper = new RedemptionCsvMapper();

    public RedemptionCsvRepository() {
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
                Files.writeString(path, "id,citizenId,rewardId,status,date\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating redemptions.csv", e);
        }
    }

    /**
     * Lee TODO el archivo redemptions.csv y devuelve la lista de canjes.
     */
    public List<Redemption> findAll() {
        List<Redemption> result = new ArrayList<>();
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
            throw new RuntimeException("Error reading redemptions.csv", e);
        }
        return result;
    }

    /**
     * Busca un canje por su id.
     */
    public Optional<Redemption> findById(long id) {
        List<Redemption> all = findAll();
        for (Redemption r : all) {
            if (r.getId() == id) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    /**
     * Escribe TODOS los canjes al archivo CSV (sobrescribe).
     */
    public void writeAll(List<Redemption> redemptions) {
        List<String> lines = new ArrayList<>();
        lines.add("id,citizenId,rewardId,status,date");
        for (Redemption r : redemptions) {
            String[] cols = mapper.toCsv(r);
            lines.add(String.join(",", cols));
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing redemptions.csv", e);
        }
    }

    /**
     * Calcula el próximo id disponible.
     */
    public long nextId() {
        long max = 0;
        for (Redemption r : findAll()) {
            if (r.getId() > max) {
                max = r.getId();
            }
        }
        return max + 1;
    }

    /**
     * Guarda un canje.
     */
    public Redemption save(Redemption entity) {
        List<Redemption> all = new ArrayList<>(findAll());

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
}