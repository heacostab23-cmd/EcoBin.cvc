package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.Reading;
import co.edu.umanizales.ecobin_csv_api.repository.ReadingCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio para Reading.
 * Aquí validamos reglas como: grams > 0.
 */
@Service
public class ReadingService {

    private final ReadingCsvRepository repo;

    public ReadingService(ReadingCsvRepository repo) {
        this.repo = repo;
    }

    public List<Reading> list() {
        return repo.findAll();
    }

    public Reading getById(long id) {
        return repo.findById(id).orElse(null);
    }

    public Reading create(Reading reading) {
        if (reading.getGrams() <= 0) {
            throw new IllegalArgumentException("Grams must be greater than 0");
        }
        return repo.save(reading);
    }

    public Reading update(long id, Reading reading) {
        Reading existing = getById(id);
        if (existing == null) {
            return null;
        }
        if (reading.getGrams() <= 0) {
            throw new IllegalArgumentException("Grams must be greater than 0");
        }
        existing.setPoint(reading.getPoint());
        existing.setWasteType(reading.getWasteType());
        existing.setCitizen(reading.getCitizen());
        existing.setGrams(reading.getGrams());
        existing.setDate(reading.getDate());
        return repo.save(existing);
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }
}
