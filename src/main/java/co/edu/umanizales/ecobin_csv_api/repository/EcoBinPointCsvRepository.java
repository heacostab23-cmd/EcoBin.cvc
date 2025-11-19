package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.EcoBinPointCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.EcoBinPoint;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EcoBinPointCsvRepository {

    private static final String FILE_PATH = "src/main/resources/data/ecobin_points.csv";
    private static final String DELIMITER = ",";

    private final EcoBinPointCsvMapper mapper = new EcoBinPointCsvMapper();

    public List<EcoBinPoint> findAll() {
        List<EcoBinPoint> result = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            return result;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            reader.readLine(); // cabecera

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] cols = line.split(DELIMITER);
                result.add(mapper.fromCsv(cols));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Optional<EcoBinPoint> findById(long id) {
        return findAll().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public EcoBinPoint save(EcoBinPoint p) {
        List<EcoBinPoint> all = findAll();

        if (p.getId() == 0L) {
            p.setId(nextId(all));
        }

        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == p.getId()) {
                all.set(i, p);
                updated = true;
                break;
            }
        }
        if (!updated) {
            all.add(p);
        }

        writeAll(all);
        return p;
    }

    public boolean delete(long id) {
        List<EcoBinPoint> all = findAll();
        boolean removed = all.removeIf(p -> p.getId() == id);
        if (removed) {
            writeAll(all);
        }
        return removed;
    }

    private long nextId(List<EcoBinPoint> all) {
        return all.stream()
                .mapToLong(EcoBinPoint::getId)
                .max()
                .orElse(0L) + 1;
    }

    private void writeAll(List<EcoBinPoint> all) {
        Path path = Paths.get(FILE_PATH);

        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("id,name,lat,lon,address");
            writer.newLine();
            for (EcoBinPoint p : all) {
                writer.write(String.join(DELIMITER, mapper.toCsv(p)));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
