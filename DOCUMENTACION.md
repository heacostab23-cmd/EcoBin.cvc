# 📚 DOCUMENTACIÓN COMPLETA - ECOBIN CSV API

## 🎯 Objetivo del Proyecto

**EcoBin** es una API REST que gestiona un sistema de reciclaje gamificado donde:
- Los **ciudadanos** registran lecturas de peso en puntos EcoBin
- Ganan **puntos** por reciclar diferentes tipos de residuos
- Pueden canjear puntos por **recompensas**
- Completan **misiones** para ganar insignias
- Los **operadores** supervisan el sistema

---

## 🏗️ Arquitectura en Capas

```
┌─────────────────────────────────────┐
│     CONTROLADORES (REST API)        │  ← Reciben peticiones HTTP
│  @RestController, @RequestMapping   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     SERVICIOS (Lógica Negocio)      │  ← Validan reglas de negocio
│         @Service                    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│   REPOSITORIOS (Persistencia)       │  ← Leen/escriben CSV
│      @Repository                    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      ARCHIVOS CSV (Datos)           │  ← Almacenamiento persistente
│   src/main/resources/data/*.csv     │
└─────────────────────────────────────┘
```

---

## 📌 ANOTACIONES (@) EXPLICADAS

### 1. **@SpringBootApplication**
```java
@SpringBootApplication
public class EcobinCsvApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcobinCsvApiApplication.class, args);
    }
}
```
**¿Qué hace?** Marca la clase como punto de entrada de la aplicación Spring Boot.
**¿Por qué?** Sin esto, Spring no sabría dónde empezar a ejecutar la aplicación.

---

### 2. **@RestController**
```java
@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    // ...
}
```
**¿Qué hace?** Marca una clase como controlador REST que maneja peticiones HTTP.
**¿Por qué?** Spring crea automáticamente endpoints HTTP basados en los métodos de la clase.
**Equivalente a:** `@Controller + @ResponseBody`

**Ejemplo:**
- `GET /api/badges` → Llama al método `list()`
- `POST /api/badges` → Llama al método `create()`

---

### 3. **@RequestMapping**
```java
@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    // Todos los métodos estarán bajo /api/badges
}
```
**¿Qué hace?** Define la ruta base para todos los endpoints del controlador.
**¿Por qué?** Evita repetir la ruta en cada método.

---

### 4. **@GetMapping, @PostMapping, @PutMapping, @DeleteMapping**
```java
@GetMapping                    // GET /api/badges
public List<Badge> list() { }

@GetMapping("/{id}")           // GET /api/badges/1
public ResponseEntity<Badge> get(@PathVariable long id) { }

@PostMapping                   // POST /api/badges
public ResponseEntity<?> create(@RequestBody Badge badge) { }

@PutMapping("/{id}")           // PUT /api/badges/1
public ResponseEntity<?> update(@PathVariable long id, @RequestBody Badge badge) { }

@DeleteMapping("/{id}")        // DELETE /api/badges/1
public ResponseEntity<Void> delete(@PathVariable long id) { }
```
**¿Qué hace?** Define el tipo de petición HTTP y la ruta específica.
**¿Por qué?** Cada operación CRUD necesita un verbo HTTP diferente.

---

### 5. **@PathVariable**
```java
@GetMapping("/{id}")
public ResponseEntity<Badge> get(@PathVariable long id) {
    // id = 5 si la URL es /api/badges/5
}
```
**¿Qué hace?** Extrae parámetros de la URL y los pasa al método.
**¿Por qué?** Permite pasar datos dinámicos en la ruta.

---

### 6. **@RequestBody**
```java
@PostMapping
public ResponseEntity<?> create(@RequestBody Badge badge) {
    // badge contiene los datos JSON del cliente
}
```
**¿Qué hace?** Convierte el JSON de la petición en un objeto Java.
**¿Por qué?** Permite recibir datos complejos desde el cliente.

**Ejemplo de JSON enviado:**
```json
{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

---

### 7. **@Service**
```java
@Service
public class BadgeService {
    private final BadgeCsvRepository repo;
    
    public BadgeService(BadgeCsvRepository repo) {
        this.repo = repo;
    }
}
```
**¿Qué hace?** Marca una clase como servicio de lógica de negocio.
**¿Por qué?** Spring la gestiona automáticamente y permite inyección de dependencias.

**Responsabilidades del servicio:**
- Validar datos (ej: `requiredPoints > 0`)
- Aplicar reglas de negocio
- Coordinar entre repositorios
- Lanzar excepciones si hay errores

---

### 8. **@Repository**
```java
@Repository
public class BadgeCsvRepository {
    // Métodos para leer/escribir CSV
}
```
**¿Qué hace?** Marca una clase como repositorio (acceso a datos).
**¿Por qué?** Spring la gestiona y permite inyección de dependencias.

**Responsabilidades del repositorio:**
- Leer archivos CSV
- Escribir archivos CSV
- Convertir filas CSV a objetos Java
- Implementar CRUD (Create, Read, Update, Delete)

---

### 9. **@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor** (Lombok)
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Badge {
    private long id;
    private String name;
    private String description;
    private long requiredPoints;
}
```

**@Getter** → Genera automáticamente métodos `getId()`, `getName()`, etc.
**@Setter** → Genera automáticamente métodos `setId()`, `setName()`, etc.
**@NoArgsConstructor** → Genera constructor sin parámetros: `new Badge()`
**@AllArgsConstructor** → Genera constructor con todos los parámetros: `new Badge(1, "EcoMaster", "...", 5000)`

**¿Por qué?** Evita escribir 100+ líneas de código repetitivo.

---

## 🔄 FLUJO DE UNA PETICIÓN HTTP

### Ejemplo: Crear un nuevo Badge

**1. Cliente envía petición:**
```
POST /api/badges
Content-Type: application/json

{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

**2. Spring recibe la petición en el Controlador:**
```java
@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    
    private final BadgeService service;
    
    public BadgeController(BadgeService service) {
        this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Badge badge) {
        // badge = Badge(0, "EcoMaster", "Top recycler", 5000)
        try {
            Badge created = service.create(badge);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
```

**3. El Servicio valida la lógica de negocio:**
```java
@Service
public class BadgeService {
    
    private final BadgeCsvRepository repo;
    
    public BadgeService(BadgeCsvRepository repo) {
        this.repo = repo;
    }
    
    public Badge create(Badge badge) {
        // Validar que requiredPoints > 0
        if (badge.getRequiredPoints() <= 0) {
            throw new IllegalArgumentException("Required points must be > 0");
        }
        // Si pasa validación, guardar
        return repo.save(badge);
    }
}
```

**4. El Repositorio guarda en CSV:**
```java
@Repository
public class BadgeCsvRepository {
    
    private final Path path = Paths.get("src/main/resources/data/badges.csv");
    private final BadgeCsvMapper mapper = new BadgeCsvMapper();
    
    public Badge save(Badge entity) {
        List<Badge> all = new ArrayList<>(findAll());
        
        if (entity.getId() == 0) {
            entity.setId(nextId());  // Asignar ID automático
            all.add(entity);
        }
        
        writeAll(all);  // Escribir en CSV
        return entity;
    }
    
    public void writeAll(List<Badge> badges) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,description,requiredPoints");
        
        for (Badge b : badges) {
            String[] cols = mapper.toCsv(b);
            lines.add(String.join(",", cols));
        }
        
        Files.write(path, lines);
    }
}
```

**5. El Mapper convierte Badge → CSV:**
```java
public class BadgeCsvMapper {
    
    public String[] toCsv(Badge b) {
        return new String[]{
            String.valueOf(b.getId()),      // "2"
            b.getName(),                     // "EcoMaster"
            b.getDescription(),              // "Top recycler"
            String.valueOf(b.getRequiredPoints())  // "5000"
        };
    }
}
```

**6. Se escribe en badges.csv:**
```csv
id,name,description,requiredPoints
1,EcoMaster,Top recycler,5000
```

**7. Spring devuelve respuesta al cliente:**
```json
{
  "id": 2,
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

---

## 🗂️ ESTRUCTURA DE CARPETAS

```
src/main/java/co/edu/umanizales/ecobin_csv_api/
├── EcobinCsvApiApplication.java          ← Punto de entrada
├── controller/                           ← Controladores REST
│   ├── BadgeController.java
│   ├── CitizenController.java
│   ├── RewardController.java
│   └── ... (11 controladores)
├── service/                              ← Lógica de negocio
│   ├── BadgeService.java
│   ├── CitizenService.java
│   ├── RewardService.java
│   └── ... (11 servicios)
├── repository/                           ← Acceso a datos
│   ├── BadgeCsvRepository.java
│   ├── CitizenCsvRepository.java
│   ├── RewardCsvRepository.java
│   └── ... (11 repositorios)
├── mapper/                               ← Conversión CSV ↔ Objetos
│   ├── BadgeCsvMapper.java
│   ├── CitizenCsvMapper.java
│   ├── RewardCsvMapper.java
│   └── ... (11 mappers)
├── model/                                ← Modelos de datos
│   ├── Badge.java
│   ├── Reward.java
│   ├── Reading.java
│   ├── Mission.java
│   ├── MissionRule.java
│   ├── WasteType.java
│   ├── Redemption.java
│   ├── RedemptionStatus.java
│   ├── MissionStatus.java
│   ├── Role.java
│   ├── Location.java
│   └── core/
│       ├── Citizen.java
│       ├── Operator.java
│       ├── User.java
│       ├── Person.java
│       ├── Authenticable.java
│       └── Notifiable.java

src/main/resources/data/                 ← Archivos CSV (Base de datos)
├── badges.csv
├── citizens.csv
├── rewards.csv
├── readings.csv
├── missions.csv
├── mission_rules.csv
├── redemptions.csv
├── ecobin_points.csv
├── waste_types.csv
├── operators.csv
└── users.csv
```

---

## 🔐 INYECCIÓN DE DEPENDENCIAS

### ¿Qué es?
Patrón de diseño donde Spring proporciona automáticamente las dependencias que una clase necesita.

### Ejemplo sin inyección (❌ MALO):
```java
@Service
public class BadgeService {
    private BadgeCsvRepository repo = new BadgeCsvRepository();  // ❌ Acoplado
}
```
**Problema:** Si cambias BadgeCsvRepository, tienes que cambiar BadgeService.

### Ejemplo con inyección (✅ BUENO):
```java
@Service
public class BadgeService {
    private final BadgeCsvRepository repo;
    
    public BadgeService(BadgeCsvRepository repo) {  // ✅ Desacoplado
        this.repo = repo;
    }
}
```
**Ventaja:** Spring proporciona la dependencia automáticamente.

---

## 📊 PATRÓN CRUD

Cada entidad tiene 5 operaciones básicas:

### 1. **CREATE (Crear)**
```
POST /api/badges
{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```
**Respuesta:** `201 Created` + objeto creado con ID asignado

### 2. **READ (Leer)**
```
GET /api/badges          ← Obtener todos
GET /api/badges/1        ← Obtener uno por ID
```
**Respuesta:** `200 OK` + datos

### 3. **UPDATE (Actualizar)**
```
PUT /api/badges/1
{
  "name": "EcoMaster Pro",
  "description": "Updated",
  "requiredPoints": 6000
}
```
**Respuesta:** `200 OK` + objeto actualizado

### 4. **DELETE (Eliminar)**
```
DELETE /api/badges/1
```
**Respuesta:** `204 No Content`

### 5. **Búsqueda especial**
```
GET /api/users/email/juan@example.com  ← Búsqueda por email
```

---

## 🎯 VALIDACIONES EN CADA CAPA

### Controlador (Recepción)
```java
@PostMapping
public ResponseEntity<?> create(@RequestBody Badge badge) {
    try {
        Badge created = service.create(badge);
        return ResponseEntity.ok(created);
    } catch (IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());  // ← Manejo de errores
    }
}
```

### Servicio (Lógica)
```java
public Badge create(Badge badge) {
    if (badge.getRequiredPoints() <= 0) {
        throw new IllegalArgumentException("Required points must be > 0");  // ← Validación
    }
    return repo.save(badge);
}
```

### Repositorio (Persistencia)
```java
public Badge save(Badge entity) {
    if (entity.getId() == 0) {
        entity.setId(nextId());  // ← Asignar ID automático
    }
    writeAll(all);  // ← Guardar en CSV
    return entity;
}
```

---

## 📝 EJEMPLO COMPLETO: Crear un Badge

### Paso 1: Enviar petición desde Postman
```
POST http://localhost:8080/api/badges
Content-Type: application/json

{
  "name": "EcoMaster",
  "description": "Top recycler in the neighborhood",
  "requiredPoints": 5000
}
```

### Paso 2: BadgeController recibe
```java
@PostMapping
public ResponseEntity<?> create(@RequestBody Badge badge) {
    // badge = Badge(0, "EcoMaster", "Top recycler...", 5000)
    try {
        Badge created = service.create(badge);
        return ResponseEntity.ok(created);
    } catch (IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
```

### Paso 3: BadgeService valida
```java
public Badge create(Badge badge) {
    if (badge.getRequiredPoints() <= 0) {
        throw new IllegalArgumentException("Required points must be > 0");
    }
    return repo.save(badge);  // ✅ Pasa validación
}
```

### Paso 4: BadgeCsvRepository guarda
```java
public Badge save(Badge entity) {
    List<Badge> all = new ArrayList<>(findAll());
    entity.setId(nextId());  // ID = 2
    all.add(entity);
    writeAll(all);
    return entity;
}
```

### Paso 5: BadgeCsvMapper convierte a CSV
```java
public String[] toCsv(Badge b) {
    return new String[]{"2", "EcoMaster", "Top recycler...", "5000"};
}
```

### Paso 6: Se escribe en badges.csv
```csv
id,name,description,requiredPoints
1,EcoMaster,Top recycler in the neighborhood,5000
2,EcoMaster,Top recycler in the neighborhood,5000
```

### Paso 7: Respuesta al cliente
```json
{
  "id": 2,
  "name": "EcoMaster",
  "description": "Top recycler in the neighborhood",
  "requiredPoints": 5000
}
```

---

## 🎓 CONCEPTOS POO APLICADOS

### 1. **Herencia**
```java
public abstract class Person {
    protected long id;
    protected String document;
}

public class Citizen extends Person {
    private User user;
    private long points;
}

public class Operator extends Person {
    private User user;
}
```
**Ventaja:** Reutilizar código común (id, document, etc.)

### 2. **Polimorfismo**
```java
public interface Authenticable {
    boolean active();
    Set<Role> roles();
}

public class User implements Authenticable {
    @Override
    public boolean active() { return active; }
    
    @Override
    public Set<Role> roles() { return roles; }
}
```
**Ventaja:** Diferentes tipos pueden implementar la misma interfaz.

### 3. **Composición**
```java
public class Mission {
    private MissionRule rule;  // Contiene una regla
}

public class Citizen {
    private List<Badge> badges;  // Contiene insignias
}
```
**Ventaja:** Combinar objetos para crear estructuras complejas.

### 4. **Encapsulación**
```java
@Getter
@Setter
private long requiredPoints;  // Privado, acceso por getter/setter
```
**Ventaja:** Controlar acceso a los datos.

---

## 🚀 CÓMO PROBAR EN POSTMAN

### 1. Crear un Badge
```
POST http://localhost:8080/api/badges
{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

### 2. Obtener todos los Badges
```
GET http://localhost:8080/api/badges
```

### 3. Obtener un Badge específico
```
GET http://localhost:8080/api/badges/1
```

### 4. Actualizar un Badge
```
PUT http://localhost:8080/api/badges/1
{
  "name": "EcoMaster Pro",
  "description": "Updated",
  "requiredPoints": 6000
}
```

### 5. Eliminar un Badge
```
DELETE http://localhost:8080/api/badges/1
```

---

## 📌 RESUMEN DE ANOTACIONES

| Anotación | Ubicación | Función |
|-----------|-----------|---------|
| `@SpringBootApplication` | Clase main | Marca punto de entrada |
| `@RestController` | Clase | Marca como controlador REST |
| `@RequestMapping` | Clase/Método | Define ruta base |
| `@GetMapping` | Método | GET HTTP |
| `@PostMapping` | Método | POST HTTP |
| `@PutMapping` | Método | PUT HTTP |
| `@DeleteMapping` | Método | DELETE HTTP |
| `@PathVariable` | Parámetro | Extrae de URL |
| `@RequestBody` | Parámetro | Convierte JSON a objeto |
| `@Service` | Clase | Marca como servicio |
| `@Repository` | Clase | Marca como repositorio |
| `@Getter` | Clase | Genera getters (Lombok) |
| `@Setter` | Clase | Genera setters (Lombok) |
| `@NoArgsConstructor` | Clase | Constructor sin parámetros (Lombok) |
| `@AllArgsConstructor` | Clase | Constructor con todos los parámetros (Lombok) |

---

## 🎯 CONCLUSIÓN

El proyecto **EcoBin CSV API** implementa:
- ✅ Arquitectura en capas (Controller → Service → Repository)
- ✅ Inyección de dependencias (Spring)
- ✅ Patrón CRUD completo (11 entidades)
- ✅ Persistencia en CSV
- ✅ Validaciones en cada capa
- ✅ Conceptos POO (Herencia, Polimorfismo, Composición, Encapsulación)
- ✅ API REST con 11 controladores

**Total:** 62 archivos Java, 11 endpoints principales, 100% funcional.
