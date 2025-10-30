package co.edu.umanizales.ecobin_csv_api.model.core;

import co.edu.umanizales.ecobin_csv_api.model.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * User = cuenta de acceso (credenciales y roles).
 *
 * Implementa Authenticable (polimorfismo: puedo tratar un User como Authenticable).
 */

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User implements Authenticable {
    private Long id;
    private String email;         // En servicios validaremos formato y unicidad
    private String passwordHash;  // Nunca guardes la contraseña en texto plano
    private boolean active;       // true si la cuenta está habilitada
    private Set<Role> roles = new HashSet<>(); // ADMIN/OPERATOR/CITIZEN

    // Implementaciones del contrato Authenticable:
    @Override public boolean active() { return active; }
    @Override public Set<Role> roles() { return roles; }

    public void addRole(Role role) {
        if (role != null) roles.add(role);
    }
}
