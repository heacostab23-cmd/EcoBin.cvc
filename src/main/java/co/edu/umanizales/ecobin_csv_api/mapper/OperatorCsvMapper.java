package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.core.Operator;
import co.edu.umanizales.ecobin_csv_api.model.core.User;

/**
 * Convierte entre filas CSV y objetos Operator.
 * CSV: id,document,firstName,lastName,email,userId
 */
public class OperatorCsvMapper {

    public Operator fromCsv(String[] c) {
        Operator op = new Operator();
        op.setId(Long.parseLong(c[0]));
        op.setDocument(c[1]);
        op.setFirstName(c[2]);
        op.setLastName(c[3]);
        op.setEmail(c[4]);
        op.setUser(new User(Long.parseLong(c[5])));  // placeholder
        return op;
    }

    public String[] toCsv(Operator op) {
        return new String[]{
                String.valueOf(op.getId()),
                op.getDocument(),
                op.getFirstName(),
                op.getLastName(),
                op.getEmail(),
                String.valueOf(op.getUser().getId())
        };
    }
}
