package co.edu.umanizales.ecobin_csv_api.model.core;

import co.edu.umanizales.ecobin_csv_api.model.Role;
import java.util.Set;

/** Contrato para objetos autenticables con roles. */
public interface Authenticable {
    boolean active();
    Set<Role> roles();
}

