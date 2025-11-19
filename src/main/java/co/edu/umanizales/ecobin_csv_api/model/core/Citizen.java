package co.edu.umanizales.ecobin_csv_api.model.core;

import co.edu.umanizales.ecobin_csv_api.model.Badge;
import co.edu.umanizales.ecobin_csv_api.model.Reading;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Ciudadano del sistema EcoBin.
 * - Hereda datos comunes desde Person.
 * - Se relaciona por OBJETOS (POO): User, Badge, Reading.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Citizen extends Person {

    private User user; // Relación el ciudadano TIENE un User 
    private long points;  // Puntos acumulados por reciclar
    private List<Badge> badges = new ArrayList<>();  // Agregación: el ciudadano mantiene sus insignias como OBJETOS
    private List<Reading> readings = new ArrayList<>();  // Historial de lecturas como OBJETOS

    /** Suma puntos solo si el valor es positivo. */
    public void addPoints(long pts) {
        if (pts > 0) this.points += pts;
    }

    /** Intenta restar puntos; true si hay saldo suficiente. */
    public boolean subtractPoints(long pts) {
        if (pts <= 0 || this.points < pts) return false;
        this.points -= pts;
        return true;
    }

    /** Agrega una insignia (objeto) si no la tenía. */
    public void addBadge(Badge badge) {
        if (badge != null && !badges.contains(badge)) {
            badges.add(badge);
        }
    }

    /** (Opcional) Agrega una lectura al historial. */
    public void addReading(Reading reading) {
        if (reading != null) readings.add(reading);
    }

    /** Constructor con solo id (para placeholders en CSV). */
    public Citizen(long id) {
        this.id = id;
    }
}
