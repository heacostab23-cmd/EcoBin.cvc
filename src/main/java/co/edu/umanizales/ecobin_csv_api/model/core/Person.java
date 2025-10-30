package co.edu.umanizales.ecobin_csv_api.model.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

public abstract class Person {
//Campos de persona
    protected Long id;          // Identificador
    protected String document;  // Documento (cédula)
    protected String firstName; // Nombre
    protected String lastName;  // Apellido
    protected String email;     // Correo

     /** Método simple para mostrar "Nombre Apellido". */
    public String fullName() {
        // Evita "null null" si falta alguno
        String fn = (firstName == null) ? "" : firstName;
        String ln = (lastName == null) ? "" : lastName;
        return (fn + " " + ln).trim();
    }
}
