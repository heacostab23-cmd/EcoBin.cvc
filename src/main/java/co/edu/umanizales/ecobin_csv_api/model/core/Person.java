package co.edu.umanizales.ecobin_csv_api.model.core;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Clase base (abstracta) para Citizen y Operator.
 * protected: las subclases pueden acceder a los campos.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public abstract class Person {
    protected Long id;
    protected String document;
    protected String firstName;
    protected String lastName;
    protected String email;

    /** Devuelve "Nombre Apellido" evitando nulls. */
    public String fullName() {
        String fn = (firstName == null) ? "" : firstName;
        String ln = (lastName == null) ? "" : lastName;
        return (fn + " " + ln).trim();
    }
}
