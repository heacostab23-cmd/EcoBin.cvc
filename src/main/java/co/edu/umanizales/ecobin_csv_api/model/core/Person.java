package co.edu.umanizales.ecobin_csv_api.model.core;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * Clase base (abstracta) para Citizen y Operator.
 * protected: las subclases pueden acceder a los campos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {
    protected long id;
    
    @NotBlank(message = "Document is required")
    protected String document;
    
    @NotBlank(message = "First name is required")
    protected String firstName;
    
    @NotBlank(message = "Last name is required")
    protected String lastName;
    
    @NotBlank(message = "Email is required")
    protected String email;

    /** Devuelve "Nombre Apellido" evitando nulls. */
    public String fullName() {
        String fn = (firstName == null) ? "" : firstName;
        String ln = (lastName == null) ? "" : lastName;
        return (fn + " " + ln).trim();
    }
}
