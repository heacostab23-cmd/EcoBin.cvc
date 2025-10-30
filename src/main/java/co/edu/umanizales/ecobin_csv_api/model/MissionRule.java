package co.edu.umanizales.ecobin_csv_api.model;

/**
 * MissionRule (record) = regla de misión:
 * - wasteTypeId: tipo de residuo objetivo.
 * - targetKg: meta en kilogramos (usa long para enteros sencillos).
 * - isoPeriod: duración ISO (ej: "P7D" = 7 días).
 */
public record MissionRule(Long wasteTypeId, long targetKg, String isoPeriod) { }
