package co.edu.umanizales.ecobin_csv_api.model;

/**
 * Valor inmutable de ubicación (record).
 * Usado por composición dentro de EcoBinPoint.
 */
public record Location(double lat, double lon, String address) { }
