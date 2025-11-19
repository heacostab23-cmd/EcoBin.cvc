package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.EcoBinPoint;
import co.edu.umanizales.ecobin_csv_api.repository.EcoBinPointCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EcoBinPointService {

    private final EcoBinPointCsvRepository repository;

    public EcoBinPointService(EcoBinPointCsvRepository repository) {
        this.repository = repository;
    }

    public List<EcoBinPoint> list() {
        return repository.findAll();
    }

    public Optional<EcoBinPoint> get(long id) {
        return repository.findById(id);
    }

    public EcoBinPoint create(EcoBinPoint p) {
        validate(p);
        p.setId(0L);
        return repository.save(p);
    }

    public Optional<EcoBinPoint> update(long id, EcoBinPoint p) {
        validate(p);
        Optional<EcoBinPoint> optional = repository.findById(id);
        
        if (optional.isPresent()) {
            EcoBinPoint existing = optional.get();
            existing.setName(p.getName());
            existing.setLocation(p.getLocation());
            return Optional.of(repository.save(existing));
        }
        
        return Optional.empty();
    }

    public boolean delete(long id) {
        return repository.delete(id);
    }

    private void validate(EcoBinPoint p) {
        if (p.getName() == null || p.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (p.getLocation() == null) {
            throw new IllegalArgumentException("location is required");
        }
    }
}
