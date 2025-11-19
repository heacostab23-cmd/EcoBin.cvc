package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.core.User;

/**
 * Convierte entre filas CSV y objetos User.
 * CSV: id,email,passwordHash,active
 */
public class UserCsvMapper {

    public User fromCsv(String[] c) {
        User u = new User();
        u.setId(Long.parseLong(c[0]));
        u.setEmail(c[1]);
        u.setPasswordHash(c[2]);
        u.setActive(Boolean.parseBoolean(c[3]));
        return u;
    }

    public String[] toCsv(User u) {
        return new String[]{
                String.valueOf(u.getId()),
                u.getEmail(),
                u.getPasswordHash(),
                String.valueOf(u.isActive())
        };
    }
}
