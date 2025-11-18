package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.mapper.CitizenCsvMapper;
import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Implementación de CsvRepository para Citizen.
 * Trabaja con src/main/resources/data/citizens.csv
 */
public class CitizenCsvRepository implements CsvRepository<Citizen, Long> {

    private static final String[] HEADER =
            {"id","document","firstName","lastName","email","points"};

    private final Path file =
            Paths.get("src/main/resources/data/citizens.csv");

    public CitizenCsvRepository() {
        ensureFile();
    }

    // Crea carpeta/archivo si no existen, y escribe el header
    private void ensureFile() {
        try {
            if (Files.notExists(file.getParent())) {
                Files.createDirectories(file.getParent());
            }
            if (Files.notExists(file)) {
                try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                    bw.write(String.join(",", HEADER));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Citizen> findAll() {
        List<Citizen> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false; // saltar header
                    continue;
                }
                if (line.isBlank()) continue;
                String[] row = line.split(",", -1);
                list.add(CitizenCsvMapper.fromRow(row));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Citizen> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return findAll().stream()
                .filter(c -> c.getId() == id)   // c.getId() es long, id es Long
                .findFirst();
    }

    @Override
    public Citizen save(Citizen entity) {
        // Si el id es 0, consideramos que todavía no tiene id asignado
        if (entity.getId() == 0L) {
            entity.setId(nextId());
        }
        List<Citizen> all = findAll();
        // eliminamos cualquier registro con el mismo id para luego agregar el actualizado
        all.removeIf(c -> c.getId() == entity.getId());
        all.add(entity);
        writeAll(all);
        return entity;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        List<Citizen> all = findAll();
        boolean removed = all.removeIf(c -> c.getId() == id);
        if (removed) {
            writeAll(all);
        }
        return removed;
    }

    @Override
    public long nextId() {
        // Como getId() es long, aquí NO hay null: usamos 0 como "sin id"
        return findAll().stream()
                .mapToLong(Citizen::getId)
                .max()
                .orElse(0L) + 1;
    }

    private void writeAll(List<Citizen> list) {
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            bw.write(String.join(",", HEADER));
            bw.newLine();
            for (Citizen c : list) {
                bw.write(String.join(",", CitizenCsvMapper.toRow(c)));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
