package co.edu.umanizales.ecobin_csv_api.model;

/**
 * Location (record) = dato inmutable para ubicación.
 * Record crea: constructor, getters y equals/hashCode automáticamente.
 */
public record Location(double lat, double lon, String address) { }
