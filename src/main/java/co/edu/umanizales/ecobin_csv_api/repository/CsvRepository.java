package co.edu.umanizales.ecobin_csv_api.repository;

import java.util.List;
import java.util.Optional;

/**
 * Contrato genérico para repositorios CSV.
 * T = tipo de entidad (Citizen, WasteType, etc).
 * ID = tipo del id (Long).
 */
public interface CsvRepository<T, ID> {
    List<T> findAll();             // Lista todos
    Optional<T> findById(ID id);   // Busca por id
    T save(T entity);              // Crea o actualiza
    boolean deleteById(ID id);     // Borra por id
    long nextId();                 // Calcula próximo id
}
