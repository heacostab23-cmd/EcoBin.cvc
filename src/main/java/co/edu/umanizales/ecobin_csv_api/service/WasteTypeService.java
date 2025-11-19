package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.WasteType;
import co.edu.umanizales.ecobin_csv_api.repository.WasteTypeCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WasteTypeService {

    private final WasteTypeCsvRepository repository;

    public WasteTypeService(WasteTypeCsvRepository repository) {
        this.repository = repository;
    }

    public List<WasteType> list() {
        return repository.findAll();
    }

    public Optional<WasteType> get(long id) {
        return repository.findById(id);
    }

    public WasteType create(WasteType wt) {
        validate(wt);
        wt.setId(0L);
        return repository.save(wt);
    }

    public Optional<WasteType> update(long id, WasteType wt) {
        validate(wt);
        Optional<WasteType> optional = repository.findById(id);
        
        if (optional.isPresent()) {
            WasteType existing = optional.get();
            existing.setName(wt.getName());
            existing.setDescription(wt.getDescription());
            return Optional.of(repository.save(existing));
        }
        
        return Optional.empty();
    }

    public boolean delete(long id) {
        return repository.delete(id);
    }

    private void validate(WasteType wt) {
        if (wt.getName() == null || wt.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (wt.getDescription() == null || wt.getDescription().isBlank()) {
            throw new IllegalArgumentException("description is required");
        }
    }
}
