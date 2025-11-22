package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.Role;
import co.edu.umanizales.ecobin_csv_api.model.core.User;

/**
 * Convierte entre filas CSV y objetos User.
 * CSV: id,email,passwordHash,active,roles
 * Roles se guardan como cadena separada por comas (ej: "ADMIN,OPERATOR")
 */
public class UserCsvMapper {

    public User fromCsv(String[] c) {

        User u = new User();

        // --- ID ---
        u.setId(Long.parseLong(c[0]));

        // --- EMAIL ---
        if (c.length > 1 && !c[1].isBlank()) {
            u.setEmail(c[1]);
        } else {
            u.setEmail(null);
        }

        // --- PASSWORD HASH ---
        if (c.length > 2 && !c[2].isBlank()) {
            u.setPasswordHash(c[2]);
        } else {
            u.setPasswordHash(null);
        }

        // --- ACTIVE ---
        if (c.length > 3 && !c[3].isBlank()) {
            u.setActive(Boolean.parseBoolean(c[3]));
        } else {
            u.setActive(false);
        }

        // --- ROLES ---
        if (c.length > 4 && !c[4].isBlank()) {
            String[] roleNames = c[4].split(",");
            for (String roleName : roleNames) {
                try {
                    u.addRole(Role.valueOf(roleName.trim()));
                } catch (IllegalArgumentException e) {
                    // Ignorar roles inválidos para evitar romper el sistema
                }
            }
        }

        return u;
    }

    public String[] toCsv(User u) {

        StringBuilder rolesStr = new StringBuilder();
        if (u.getRoles() != null && !u.getRoles().isEmpty()) {
            u.getRoles().forEach(role -> {
                if (rolesStr.length() > 0) rolesStr.append(",");
                rolesStr.append(role.name());
            });
        }

        return new String[]{
                String.valueOf(u.getId()),
                (u.getEmail() == null ? "" : u.getEmail()),
                (u.getPasswordHash() == null ? "" : u.getPasswordHash()),
                String.valueOf(u.isActive()),
                rolesStr.toString()
        };
    }
}
