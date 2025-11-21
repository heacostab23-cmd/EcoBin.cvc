package co.edu.umanizales.ecobin_csv_api.repository;

import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;
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

/**
 * Repositorio CSV específico para Citizen.
 * Aquí leemos y escribimos el archivo citizens.csv.
 */
@Repository
public class CitizenCsvRepository {

    private static final String FILE_PATH = "src/main/resources/data/citizens.csv";
    private static final String DELIMITER = ",";

    public List<Citizen> findAll() {
        List<Citizen> result = new ArrayList<>();
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
                result.add(fromCsv(cols));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected Citizen fromCsv(String[] c) {
        // c[0] = id, c[1] = document, c[2] = firstName, c[3] = lastName, c[4] = email, c[5] = points
        Citizen citizen = new Citizen();
        citizen.setId(Long.parseLong(c[0]));
        citizen.setDocument(c[1]);
        citizen.setFirstName(c[2]);
        citizen.setLastName(c[3]);
        citizen.setEmail(c[4]);
        citizen.setPoints(Long.parseLong(c[5]));
        return citizen;
    }

    protected String[] toCsv(Citizen c) {
        return new String[]{
                // Convertimos el id y los puntos a texto
                String.valueOf(c.getId()),
                c.getDocument(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                String.valueOf(c.getPoints())
        };
    }

    public Optional<Citizen> findById(long id) {
        List<Citizen> all = findAll();
        for (Citizen c : all) {
            if (c.getId() == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    /**
     * Busca un ciudadano por su número de documento.
     * Si lo encuentra, devuelve Optional con el ciudadano.
     * Si no, devuelve Optional vacío.
     */
    public Optional<Citizen> findByDocument(String document) {
        List<Citizen> all = findAll(); // leemos todo el CSV

        for (Citizen c : all) {
            if (c.getDocument() != null && c.getDocument().equals(document)) {
                return Optional.of(c); // lo encontramos
            }
        }
        return Optional.empty(); // no existe
    }

    /**
     * Calcula el próximo id disponible.
     * Si el archivo está vacío, será 1; si no, último id + 1.
     */
    private long nextId() {
        List<Citizen> all = findAll();
        long maxId = 0;
        
        for (Citizen c : all) {
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        
        return maxId + 1;
    }

    /**
     * Guarda o actualiza un ciudadano en el CSV.
     */
    public Citizen save(Citizen entity) {
        List<Citizen> all = new ArrayList<>(findAll());

        if (entity.getId() == 0L) {
            // Nuevo ciudadano: le asignamos un id
            entity.setId(nextId());
            all.add(entity);
        } else {
            // Actualización: buscamos por id y reemplazamos
            boolean updated = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == entity.getId()) {
                    all.set(i, entity);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                all.add(entity); // por si acaso no estaba
            }
        }

        writeAll(all);
        return entity;
    }

    public boolean delete(long id) {
        List<Citizen> all = findAll();
        boolean removed = false;
        
        // Buscamos y eliminamos el ciudadano con ese id
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == id) {
                all.remove(i);
                removed = true;
                break;
            }
        }
        
        // Si lo encontramos y eliminamos, guardamos los cambios
        if (removed) {
            writeAll(all);
        }
        
        return removed;
    }

    private void writeAll(List<Citizen> all) {
        Path path = Paths.get(FILE_PATH);

        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("id,document,firstName,lastName,email,points");
            writer.newLine();
            for (Citizen c : all) {
                writer.write(String.join(DELIMITER, toCsv(c)));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}