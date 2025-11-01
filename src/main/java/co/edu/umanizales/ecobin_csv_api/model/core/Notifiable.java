package co.edu.umanizales.ecobin_csv_api.model.core;

/** Contrato opcional para recibir notificaciones. */
public interface Notifiable {
    void notify(String message);
}

