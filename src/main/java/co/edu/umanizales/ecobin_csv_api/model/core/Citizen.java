package co.edu.umanizales.ecobin_csv_api.model.core;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Citizen = ciudadano que recicla y gana puntos/insignias.
 *
 * Nota: EXTIENDE Person (herencia).
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Citizen extends Person {
    private Long userId;                 // Relación con User (por ID)
    private long points;                 // Puntos acumulados (>= 0)
    private List<Long> badgeIds = new ArrayList<>(); // IDs de insignias que tiene

    /** Sumar puntos (evita valores negativos o cero). */
    public void addPoints(long pts) {
        if (pts > 0) {
            points += pts;
        }
    }

    /**
     * Restar puntos si alcanza el saldo.
     * Devuelve true si sí pudo restar, false si no.
     */
    public boolean subtractPoints(long pts) {
        if (pts <= 0) return false;
        if (points < pts) return false;
        points -= pts;
        return true;
    }

    /** Guardar una insignia si no la tenía. */
    public void addBadge(Long badgeId) {
        if (badgeId != null && !badgeIds.contains(badgeId)) {
            badgeIds.add(badgeId);
        }
    }
}
