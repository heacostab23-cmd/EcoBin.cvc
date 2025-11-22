package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;
import co.edu.umanizales.ecobin_csv_api.repository.CitizenCsvRepository;
import co.edu.umanizales.ecobin_csv_api.repository.UserCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Lógica de negocio para Ciudadanos.
 * Aquí validamos reglas como: no repetir documento.
 */
@Service
public class CitizenService {

    private final CitizenCsvRepository repository;
    private final UserCsvRepository userRepo;

    public CitizenService(CitizenCsvRepository repository, UserCsvRepository userRepo) {
        this.repository = repository;
        this.userRepo = userRepo;
    }

    /**
     * Cargar el User asociado al Citizen (si existe un usuario con el mismo email).
     */
    private void loadUser(Citizen citizen) {
        // Si el citizen no tiene email, no hay cómo buscar
        if (citizen.getEmail() == null || citizen.getEmail().isBlank()) {
            return;
        }

        // Buscar el user que tenga el mismo email en users.csv
        userRepo.findByEmail(citizen.getEmail())
                .ifPresent(citizen::setUser);
    }

    /** Listar todos los ciudadanos. */
    public List<Citizen> list() {
        List<Citizen> citizens = repository.findAll();

        // Enriquecemos cada citizen con su user (si existe)
        for (Citizen c : citizens) {
            loadUser(c);
        }

        return citizens;
    }

    /** Buscar un ciudadano por id. */
    public Optional<Citizen> getById(long id) {
        return repository.findById(id)
                .map(citizen -> {
                    loadUser(citizen); // también cargamos el user aquí
                    return citizen;
                });
    }

    /**
     * Crear un nuevo ciudadano.
     * - Verifica que el documento venga lleno.
     * - Verifica que no exista ya otro ciudadano con ese documento.
     */
    public Citizen create(Citizen citizen) {
        // Validar que el documento no esté vacío
        String document = citizen.getDocument();
        if (document == null || document.isBlank()) {
            throw new IllegalArgumentException("Document is required");
        }

        // Verificar si ya existe un ciudadano con ese documento
        Optional<Citizen> existing = repository.findByDocument(document);
        if (existing.isPresent()) {
            Citizen found = existing.get();
            String errorMsg = "Citizen with document " + found.getDocument() +
                    " already exists with id " + found.getId();
            throw new IllegalArgumentException(errorMsg);
        }

        // Si no existe, lo guardamos
        Citizen saved = repository.save(citizen);

        // Intentar cargar su user (por si ya existe en users.csv)
        loadUser(saved);

        return saved;
    }

    /**
     * Actualizar un ciudadano existente.
     */
    public Citizen update(long id, Citizen data) {
        // Buscamos el ciudadano existente
        Optional<Citizen> optional = repository.findById(id);

        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Citizen not found with id " + id);
        }

        Citizen existing = optional.get();

        // Actualizamos los datos
        existing.setDocument(data.getDocument());
        existing.setFirstName(data.getFirstName());
        existing.setLastName(data.getLastName());
        existing.setEmail(data.getEmail());
        existing.setPoints(data.getPoints());

        // Volvemos a intentar ligar el user según el nuevo email
        loadUser(existing);

        return repository.save(existing);
    }

    /** Eliminar ciudadano por id. */
    public void delete(long id) {
        repository.delete(id);
    }
}
