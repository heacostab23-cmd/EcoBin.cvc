package co.edu.umanizales.ecobin_csv_api.model.core;

import co.edu.umanizales.ecobin_csv_api.model.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Cuenta de acceso del sistema.
 * Implementa Authenticable (polimorfismo).
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User implements Authenticable {
    private Long id;
    private String email;         // se validará formato/único a nivel de servicio
    private String passwordHash;  // nunca guardar la contraseña en texto plano
    private boolean active;
    private Set<Role> roles = new HashSet<>();

    @Override public boolean active() { return active; }
    @Override public Set<Role> roles() { return roles; }

    public void addRole(Role role) {
        if (role != null) roles.add(role);
    }
}

