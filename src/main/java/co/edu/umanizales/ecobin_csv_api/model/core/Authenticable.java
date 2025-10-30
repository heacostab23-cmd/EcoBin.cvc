package co.edu.umanizales.ecobin_csv_api.model.core;

import co.edu.umanizales.ecobin_csv_api.model.Role;
import java.util.Set;

/**
 * Interface = contrato (solo métodos).
 * Quien implemente Authenticable debe decir si está activo y qué roles tiene.
 */
public interface Authenticable {
    boolean active();
    Set<Role> roles();
}

