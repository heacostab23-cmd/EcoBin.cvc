package co.edu.umanizales.ecobin_csv_api.service;

import co.edu.umanizales.ecobin_csv_api.model.Reading;
import co.edu.umanizales.ecobin_csv_api.model.core.Citizen;
import co.edu.umanizales.ecobin_csv_api.model.WasteType;
import co.edu.umanizales.ecobin_csv_api.model.EcoBinPoint;
import co.edu.umanizales.ecobin_csv_api.model.Role;
import co.edu.umanizales.ecobin_csv_api.model.report.ReadingDetailDTO;   // DTO detalle reporte
import co.edu.umanizales.ecobin_csv_api.model.report.ReadingReportDTO;   // DTO resumen por día
import co.edu.umanizales.ecobin_csv_api.repository.ReadingCsvRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Lógica de negocio para Reading.
 * Aquí validamos:
 * - grams > 0
 * - solo ciudadanos activos pueden registrar lecturas
 * - usuarios con rol OPERATOR no pueden registrar lecturas
 * - generación de reportes de reciclaje por rango de fechas
 */
@Service
public class ReadingService {

    private final ReadingCsvRepository repo;

    // Servicios para cargar la info completa
    private final CitizenService citizenService;
    private final EcoBinPointService pointService;
    private final WasteTypeService wasteTypeService;

    public ReadingService(
            ReadingCsvRepository repo,
            CitizenService citizenService,
            EcoBinPointService pointService,
            WasteTypeService wasteTypeService
    ) {
        this.repo = repo;
        this.citizenService = citizenService;
        this.pointService = pointService;
        this.wasteTypeService = wasteTypeService;
    }

    /**
     * Cargar citizen, point y wasteType COMPLETOS usando list() y filtrando por id.
     */
    private void loadRelations(Reading r) {
        // Cargar citizen completo
        if (r.getCitizen() != null && r.getCitizen().getId() > 0) {
            Citizen citizen = citizenService.list().stream()
                    .filter(c -> c.getId() == r.getCitizen().getId())
                    .findFirst()
                    .orElse(null);
            if (citizen != null) {
                r.setCitizen(citizen);
            }
        }

        // Cargar point completo
        if (r.getPoint() != null && r.getPoint().getId() > 0) {
            EcoBinPoint point = pointService.list().stream()
                    .filter(p -> p.getId() == r.getPoint().getId())
                    .findFirst()
                    .orElse(null);
            if (point != null) {
                r.setPoint(point);
            }
        }

        // Cargar wasteType completo
        if (r.getWasteType() != null && r.getWasteType().getId() > 0) {
            WasteType waste = wasteTypeService.list().stream()
                    .filter(w -> w.getId() == r.getWasteType().getId())
                    .findFirst()
                    .orElse(null);
            if (waste != null) {
                r.setWasteType(waste);
            }
        }
    }

    /** Listar readings con relaciones completas */
    public List<Reading> list() {
        List<Reading> readings = repo.findAll();
        readings.forEach(this::loadRelations);
        return readings;
    }

    /** Buscar reading por id con relaciones completas */
    public Reading getById(long id) {
        return repo.findById(id)
                .map(r -> {
                    loadRelations(r);
                    return r;
                })
                .orElse(null);
    }

    public Reading create(Reading reading) {

        if (reading.getGrams() <= 0) {
            throw new IllegalArgumentException("Grams must be greater than 0");
        }

        // Debe venir un citizen con id
        if (reading.getCitizen() == null || reading.getCitizen().getId() <= 0) {
            throw new IllegalArgumentException("Citizen id is required");
        }

        // Cargar citizen COMPLETO (con roles, user y estado)
        Citizen citizen = citizenService.getById(reading.getCitizen().getId())
                .orElseThrow(() -> new IllegalArgumentException("Citizen not found"));

        // ---- VALIDACIONES DE USUARIO ----

        if (citizen.getUser() != null) {

            // (1) SI ES OPERADOR → NO PERMITIR
            if (citizen.getUser().getRoles().contains(Role.OPERATOR)) {
                throw new IllegalArgumentException(
                        "No puedes crear lecturas porque tu usuario es un operador."
                );
            }

            // (2) SI ESTÁ INACTIVO → NO PERMITIR
            if (!citizen.getUser().isActive()) {
                throw new IllegalArgumentException(
                        "Tu usuario está inactivo, no puedes ingresar lecturas."
                );
            }
        }

        // Actualizar citizen con datos completos
        reading.setCitizen(citizen);

        // Guardar
        Reading saved = repo.save(reading);

        // Cargar relaciones para devolver JSON completo
        loadRelations(saved);

        return saved;
    }

    public Reading update(long id, Reading reading) {
        Reading existing = getById(id);
        if (existing == null) {
            return null;
        }
        if (reading.getGrams() <= 0) {
            throw new IllegalArgumentException("Grams must be greater than 0");
        }

        // Igual que en create: validamos citizen si lo está cambiando
        if (reading.getCitizen() != null && reading.getCitizen().getId() > 0) {
            Citizen citizen = citizenService.getById(reading.getCitizen().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Citizen not found"));

            if (citizen.getUser() != null) {
                if (citizen.getUser().getRoles().contains(Role.OPERATOR)) {
                    throw new IllegalArgumentException(
                            "No puedes actualizar lecturas porque tu usuario es un operador."
                    );
                }
                if (!citizen.getUser().isActive()) {
                    throw new IllegalArgumentException(
                            "Tu usuario está inactivo, no puedes actualizar lecturas."
                    );
                }
            }

            existing.setCitizen(citizen);
        }

        existing.setPoint(reading.getPoint());
        existing.setWasteType(reading.getWasteType());
        existing.setGrams(reading.getGrams());
        existing.setDate(reading.getDate());

        Reading saved = repo.save(existing);
        loadRelations(saved);
        return saved;
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }

    // ------------------------------------------------------------
    //  MÉTODO NUEVO: REPORTE DE LECTURAS POR RANGO DE FECHAS
    // ------------------------------------------------------------

    /**
     * Reporte de lecturas por rango de fechas.
     *
     * - Valida que la fecha final NO sea menor que la inicial.
     * - Agrupa por fecha y por tipo de residuo.
     * - Construye una lista de ReadingReportDTO para enviar al controlador.
     */
    public List<ReadingReportDTO> reportByDate(LocalDate fechaInicio, LocalDate fechaFin) {

        // 0. Validar que la fecha final tenga lógica (no puede ser menor)
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException(
                    "La fecha final no puede ser menor que la fecha inicial"
            );
        }

        // 1. Traer todas las lecturas ya con relaciones completas
        List<Reading> readings = list();

        // 2. Filtrar por rango de fechas (incluye inicio y fin)
        List<Reading> filtradas = readings.stream()
                .filter(r -> r.getDate() != null
                        && !r.getDate().isBefore(fechaInicio)
                        && !r.getDate().isAfter(fechaFin))
                .collect(Collectors.toList());

        // 3. Agrupar las lecturas por fecha
        Map<LocalDate, List<Reading>> porDia = filtradas.stream()
                .collect(Collectors.groupingBy(Reading::getDate));

        List<ReadingReportDTO> resultado = new ArrayList<>();

        // 4. Recorrer cada día del mapa (cada fecha es una llave)
        for (Map.Entry<LocalDate, List<Reading>> entry : porDia.entrySet()) {
            LocalDate fecha = entry.getKey();
            List<Reading> delDia = entry.getValue();

            // 4.1 Agrupar por tipo de residuo (nombre del wasteType)
            Map<String, Integer> gramosPorTipo = new HashMap<>();

            for (Reading r : delDia) {
                // Si por alguna razón no hay wasteType, lo marcamos como "SIN_TIPO"
                String tipo = (r.getWasteType() != null && r.getWasteType().getName() != null)
                        ? r.getWasteType().getName()
                        : "SIN_TIPO";

                // Valor actual en el mapa para ese tipo
                int gramosActuales = gramosPorTipo.getOrDefault(tipo, 0);

                // r.getGrams() es long → lo convertimos a int antes de sumar
                int nuevosGramos = gramosActuales + (int) r.getGrams();

                // Guardamos el nuevo total en el mapa
                gramosPorTipo.put(tipo, nuevosGramos);
            }

            // 4.2 Convertir el mapa en lista de detalles (tipo + gramaje)
            List<ReadingDetailDTO> detalles = gramosPorTipo.entrySet().stream()
                    .map(e -> new ReadingDetailDTO(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            // 4.3 Calcular el total del día (suma de todos los tipos)
            int total = detalles.stream()
                    .mapToInt(ReadingDetailDTO::getGramaje)
                    .sum();

            // 4.4 Crear el DTO del día y agregarlo al resultado
            resultado.add(new ReadingReportDTO(
                    fecha.toString(), // lo pasamos a String para el JSON
                    total,
                    detalles
            ));
        }

        // 5. Ordenar el resultado por fecha ascendente (para que salga ordenado en el JSON)
        resultado.sort(Comparator.comparing(ReadingReportDTO::getFecha));

        return resultado;
    }
}

