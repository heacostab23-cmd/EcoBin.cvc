package co.edu.umanizales.ecobin_csv_api.model.core;

/**
 * Interface opcional para poder "enviar mensajes" a un objeto.
 * A futuro, Citizen/Operator podrían implementarla.
 */
public interface Notifiable {
    void notify(String message);
}
