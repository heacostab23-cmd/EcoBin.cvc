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
        u.setId(Long.parseLong(c[0]));
        u.setEmail(c[1]);
        u.setPasswordHash(c[2]);
        u.setActive(Boolean.parseBoolean(c[3]));
        
        // Parsear roles si existen
        if (c.length > 4 && !c[4].isBlank()) {
            String[] roleNames = c[4].split(",");
            for (String roleName : roleNames) {
                try {
                    u.addRole(Role.valueOf(roleName.trim()));
                } catch (IllegalArgumentException e) {
                    // Ignorar roles inválidos
                }
            }
        }
        
        return u;
    }

    public String[] toCsv(User u) {
        // Convertir roles a cadena separada por comas
        StringBuilder rolesStr = new StringBuilder();
        if (u.getRoles() != null && !u.getRoles().isEmpty()) {
            u.getRoles().forEach(role -> {
                if (rolesStr.length() > 0) rolesStr.append(",");
                rolesStr.append(role.name());
            });
        }
        
        return new String[]{
                String.valueOf(u.getId()),
                u.getEmail(),
                u.getPasswordHash(),
                String.valueOf(u.isActive()),
                rolesStr.toString()
        };
    }
}
