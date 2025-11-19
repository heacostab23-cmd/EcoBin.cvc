package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.WasteTypeCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.WasteType;
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
public class WasteTypeCsvRepository {

    private static final String FILE_PATH = "src/main/resources/data/waste_types.csv";
    private static final String DELIMITER = ",";

    private final WasteTypeCsvMapper mapper = new WasteTypeCsvMapper();

    public List<WasteType> findAll() {
        List<WasteType> result = new ArrayList<>();
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

    public Optional<WasteType> findById(long id) {
        return findAll().stream()
                .filter(w -> w.getId() == id)
                .findFirst();
    }

    public WasteType save(WasteType wt) {
        List<WasteType> all = findAll();

        if (wt.getId() == 0L) {
            wt.setId(nextId(all));
        }

        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == wt.getId()) {
                all.set(i, wt);
                updated = true;
                break;
            }
        }
        if (!updated) {
            all.add(wt);
        }

        writeAll(all);
        return wt;
    }

    public boolean delete(long id) {
        List<WasteType> all = findAll();
        boolean removed = all.removeIf(w -> w.getId() == id);
        if (removed) {
            writeAll(all);
        }
        return removed;
    }

    private long nextId(List<WasteType> all) {
        return all.stream()
                .mapToLong(WasteType::getId)
                .max()
                .orElse(0L) + 1;
    }

    private void writeAll(List<WasteType> all) {
        Path path = Paths.get(FILE_PATH);

        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("id,name,description");
            writer.newLine();
            for (WasteType wt : all) {
                writer.write(String.join(DELIMITER, mapper.toCsv(wt)));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
