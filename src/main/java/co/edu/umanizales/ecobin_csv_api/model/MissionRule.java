package co.edu.umanizales.ecobin_csv_api.model;

/**
 * Regla de misión: tipo de residuo, meta (kg) y periodo ISO (ej: P7D).
 * Se usa por composición dentro de Mission.
 */
public record MissionRule(WasteType wasteType, long targetKg, String isoPeriod) { }
