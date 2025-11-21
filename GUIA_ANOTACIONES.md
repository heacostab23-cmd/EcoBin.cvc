# 🔍 GUÍA DETALLADA DE ANOTACIONES (@) - ECOBIN CSV API

## Tabla de Contenidos
1. [Anotaciones de Spring Boot](#anotaciones-de-spring-boot)
2. [Anotaciones de Controladores](#anotaciones-de-controladores)
3. [Anotaciones de Servicios y Repositorios](#anotaciones-de-servicios-y-repositorios)
4. [Anotaciones de Lombok](#anotaciones-de-lombok)
5. [Ejemplos Prácticos](#ejemplos-prácticos)

---

## Anotaciones de Spring Boot

### 1. @SpringBootApplication
**Ubicación:** Clase principal de la aplicación

```java
@SpringBootApplication
public class EcobinCsvApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcobinCsvApiApplication.class, args);
    }
}
```

**¿Qué hace?**
- Marca la clase como aplicación Spring Boot
- Habilita auto-configuración
- Escanea componentes en el paquete actual y subpaquetes
- Inicia el servidor Tomcat en puerto 8080

**¿Por qué la necesitamos?**
Sin esta anotación, Spring no sabría que esta es la aplicación principal.

**Equivalente manual:**
```java
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class EcobinCsvApiApplication {
    // ...
}
```

---

## Anotaciones de Controladores

### 2. @RestController
**Ubicación:** Clase que maneja peticiones HTTP

```java
@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    // Los métodos aquí manejan peticiones HTTP
}
```

**¿Qué hace?**
- Marca la clase como controlador REST
- Todos los métodos devuelven JSON automáticamente
- Spring crea endpoints HTTP basados en los métodos

**¿Por qué?**
Simplifica la creación de APIs REST sin necesidad de configuración adicional.

**Diferencia con @Controller:**
```java
@Controller           // Devuelve vistas HTML (MVC)
@RestController       // Devuelve JSON (API REST)
```

---

### 3. @RequestMapping
**Ubicación:** Clase o método

```java
// En la clase - define ruta base
@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    
    // En el método - define ruta específica
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Badge> get(@PathVariable long id) {
        // GET /api/badges/{id}
    }
}
```

**¿Qué hace?**
- Define la ruta HTTP para acceder a los endpoints
- Puede especificar el método HTTP (GET, POST, etc.)

**¿Por qué?**
Organiza los endpoints de forma clara y accesible.

---

### 4. @GetMapping
**Ubicación:** Método en @RestController

```java
@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    
    @GetMapping
    public List<Badge> list() {
        // GET /api/badges
        return service.list();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Badge> get(@PathVariable long id) {
        // GET /api/badges/1
        return ResponseEntity.ok(service.getById(id));
    }
}
```

**¿Qué hace?**
- Marca el método para manejar peticiones GET
- Equivalente a: `@RequestMapping(method = RequestMethod.GET)`

**¿Por qué?**
Más legible y conciso que @RequestMapping.

**Casos de uso:**
- Obtener datos (sin modificar)
- Listar recursos
- Buscar por ID

---

### 5. @PostMapping
**Ubicación:** Método en @RestController

```java
@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Badge badge) {
        // POST /api/badges
        try {
            Badge created = service.create(badge);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
```

**¿Qué hace?**
- Marca el método para manejar peticiones POST
- Crea nuevos recursos

**¿Por qué?**
POST se usa para crear datos nuevos.

**Ejemplo de petición:**
```
POST /api/badges
Content-Type: application/json

{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

---

### 6. @PutMapping
**Ubicación:** Método en @RestController

```java
@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Badge badge) {
        // PUT /api/badges/1
        try {
            Badge updated = service.update(id, badge);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
```

**¿Qué hace?**
- Marca el método para manejar peticiones PUT
- Actualiza recursos existentes

**¿Por qué?**
PUT se usa para modificar datos completos.

**Ejemplo de petición:**
```
PUT /api/badges/1
Content-Type: application/json

{
  "name": "EcoMaster Pro",
  "description": "Updated description",
  "requiredPoints": 6000
}
```

---

### 7. @DeleteMapping
**Ubicación:** Método en @RestController

```java
@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        // DELETE /api/badges/1
        boolean deleted = service.delete(id);
        return deleted
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
```

**¿Qué hace?**
- Marca el método para manejar peticiones DELETE
- Elimina recursos

**¿Por qué?**
DELETE se usa para borrar datos.

**Ejemplo de petición:**
```
DELETE /api/badges/1
```

---

### 8. @PathVariable
**Ubicación:** Parámetro de método

```java
@GetMapping("/{id}")
public ResponseEntity<Badge> get(@PathVariable long id) {
    // Si URL es /api/badges/5, entonces id = 5
    Badge badge = service.getById(id);
    return badge != null 
        ? ResponseEntity.ok(badge)
        : ResponseEntity.notFound().build();
}
```

**¿Qué hace?**
- Extrae valores de la URL y los pasa como parámetros
- El nombre debe coincidir con el de la ruta: `{id}` → `@PathVariable long id`

**¿Por qué?**
Permite pasar datos dinámicos en la ruta.

**Ejemplos:**
```
GET /api/badges/1           → id = 1
GET /api/badges/999         → id = 999
GET /api/users/email/juan   → email = "juan"
```

---

### 9. @RequestBody
**Ubicación:** Parámetro de método

```java
@PostMapping
public ResponseEntity<?> create(@RequestBody Badge badge) {
    // badge contiene los datos JSON del cliente
    // Spring convierte automáticamente JSON → Badge
    
    Badge created = service.create(badge);
    return ResponseEntity.ok(created);
}
```

**¿Qué hace?**
- Convierte el JSON de la petición en un objeto Java
- Deserialización automática

**¿Por qué?**
Permite recibir datos complejos desde el cliente.

**Ejemplo de JSON recibido:**
```json
{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

**Conversión automática:**
```
JSON → @RequestBody → Badge object
{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
↓
Badge(
  id=0,
  name="EcoMaster",
  description="Top recycler",
  requiredPoints=5000
)
```

---

## Anotaciones de Servicios y Repositorios

### 10. @Service
**Ubicación:** Clase que contiene lógica de negocio

```java
@Service
public class BadgeService {
    private final BadgeCsvRepository repo;
    
    public BadgeService(BadgeCsvRepository repo) {
        this.repo = repo;
    }
    
    public Badge create(Badge badge) {
        // Validar reglas de negocio
        if (badge.getRequiredPoints() <= 0) {
            throw new IllegalArgumentException("Required points must be > 0");
        }
        // Guardar
        return repo.save(badge);
    }
}
```

**¿Qué hace?**
- Marca la clase como servicio
- Spring la gestiona como singleton (una sola instancia)
- Permite inyección de dependencias

**¿Por qué?**
Centraliza la lógica de negocio y validaciones.

**Responsabilidades:**
- Validar datos
- Aplicar reglas de negocio
- Coordinar entre repositorios
- Lanzar excepciones si hay errores

---

### 11. @Repository
**Ubicación:** Clase que accede a datos

```java
@Repository
public class BadgeCsvRepository {
    private final Path path = Paths.get("src/main/resources/data/badges.csv");
    private final BadgeCsvMapper mapper = new BadgeCsvMapper();
    
    public List<Badge> findAll() {
        // Leer CSV
        // Convertir a objetos Badge
        // Devolver lista
    }
    
    public Badge save(Badge entity) {
        // Guardar en CSV
    }
}
```

**¿Qué hace?**
- Marca la clase como repositorio
- Spring la gestiona como singleton
- Permite inyección de dependencias

**¿Por qué?**
Centraliza el acceso a datos (CSV en este caso).

**Responsabilidades:**
- Leer archivos CSV
- Escribir archivos CSV
- Convertir filas CSV a objetos Java
- Implementar CRUD

---

## Anotaciones de Lombok

### 12. @Getter
**Ubicación:** Clase o campo

```java
@Getter
public class Badge {
    private long id;
    private String name;
    private String description;
    private long requiredPoints;
}

// Genera automáticamente:
// public long getId() { return id; }
// public String getName() { return name; }
// public String getDescription() { return description; }
// public long getRequiredPoints() { return requiredPoints; }
```

**¿Qué hace?**
- Genera automáticamente métodos getter
- Acceso de lectura a los campos

**¿Por qué?**
Evita escribir 100+ líneas de código repetitivo.

---

### 13. @Setter
**Ubicación:** Clase o campo

```java
@Setter
public class Badge {
    private long id;
    private String name;
    private String description;
    private long requiredPoints;
}

// Genera automáticamente:
// public void setId(long id) { this.id = id; }
// public void setName(String name) { this.name = name; }
// public void setDescription(String description) { this.description = description; }
// public void setRequiredPoints(long requiredPoints) { this.requiredPoints = requiredPoints; }
```

**¿Qué hace?**
- Genera automáticamente métodos setter
- Acceso de escritura a los campos

**¿Por qué?**
Evita código repetitivo.

---

### 14. @NoArgsConstructor
**Ubicación:** Clase

```java
@NoArgsConstructor
public class Badge {
    private long id;
    private String name;
    private String description;
    private long requiredPoints;
}

// Genera automáticamente:
// public Badge() { }

// Uso:
Badge badge = new Badge();
```

**¿Qué hace?**
- Genera constructor sin parámetros
- Inicializa campos con valores por defecto

**¿Por qué?**
Necesario para que Spring pueda crear instancias.

---

### 15. @AllArgsConstructor
**Ubicación:** Clase

```java
@AllArgsConstructor
public class Badge {
    private long id;
    private String name;
    private String description;
    private long requiredPoints;
}

// Genera automáticamente:
// public Badge(long id, String name, String description, long requiredPoints) {
//     this.id = id;
//     this.name = name;
//     this.description = description;
//     this.requiredPoints = requiredPoints;
// }

// Uso:
Badge badge = new Badge(1, "EcoMaster", "Top recycler", 5000);
```

**¿Qué hace?**
- Genera constructor con todos los parámetros
- Inicializa todos los campos

**¿Por qué?**
Facilita crear objetos con todos los datos.

---

## Ejemplos Prácticos

### Ejemplo 1: Crear un Badge (Flujo Completo)

**Petición Postman:**
```
POST http://localhost:8080/api/badges
Content-Type: application/json

{
  "name": "EcoMaster",
  "description": "Top recycler in the neighborhood",
  "requiredPoints": 5000
}
```

**Paso 1: Controlador recibe**
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
        // @RequestBody convierte JSON → Badge object
        // badge = Badge(0, "EcoMaster", "Top recycler...", 5000)
        
        try {
            Badge created = service.create(badge);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
```

**Paso 2: Servicio valida**
```java
@Service
public class BadgeService {
    
    private final BadgeCsvRepository repo;
    
    public BadgeService(BadgeCsvRepository repo) {
        this.repo = repo;
    }
    
    public Badge create(Badge badge) {
        // Validar regla de negocio
        if (badge.getRequiredPoints() <= 0) {
            throw new IllegalArgumentException("Required points must be > 0");
        }
        // ✅ Pasa validación, guardar
        return repo.save(badge);
    }
}
```

**Paso 3: Repositorio guarda**
```java
@Repository
public class BadgeCsvRepository {
    
    private final Path path = Paths.get("src/main/resources/data/badges.csv");
    private final BadgeCsvMapper mapper = new BadgeCsvMapper();
    
    public Badge save(Badge entity) {
        List<Badge> all = new ArrayList<>(findAll());
        
        // Asignar ID automático
        if (entity.getId() == 0) {
            entity.setId(nextId());  // ID = 2
        }
        
        all.add(entity);
        writeAll(all);  // Escribir en CSV
        return entity;
    }
    
    public long nextId() {
        long max = 0;
        for (Badge b : findAll()) {
            if (b.getId() > max) {
                max = b.getId();
            }
        }
        return max + 1;
    }
}
```

**Paso 4: Mapper convierte a CSV**
```java
public class BadgeCsvMapper {
    
    public String[] toCsv(Badge b) {
        return new String[]{
            String.valueOf(b.getId()),           // "2"
            b.getName(),                          // "EcoMaster"
            b.getDescription(),                   // "Top recycler..."
            String.valueOf(b.getRequiredPoints()) // "5000"
        };
    }
}
```

**Paso 5: Se escribe en badges.csv**
```csv
id,name,description,requiredPoints
1,OldBadge,Old description,3000
2,EcoMaster,Top recycler in the neighborhood,5000
```

**Respuesta al cliente:**
```json
{
  "id": 2,
  "name": "EcoMaster",
  "description": "Top recycler in the neighborhood",
  "requiredPoints": 5000
}
```

---

### Ejemplo 2: Obtener un Badge por ID

**Petición:**
```
GET http://localhost:8080/api/badges/1
```

**Controlador:**
```java
@GetMapping("/{id}")
public ResponseEntity<Badge> get(@PathVariable long id) {
    // @PathVariable extrae id de la URL
    // id = 1
    
    Badge badge = service.getById(id);
    return badge != null
        ? ResponseEntity.ok(badge)
        : ResponseEntity.notFound().build();
}
```

**Servicio:**
```java
public Badge getById(long id) {
    return repo.findById(id).orElse(null);
}
```

**Repositorio:**
```java
public Optional<Badge> findById(long id) {
    List<Badge> all = findAll();
    for (Badge b : all) {
        if (b.getId() == id) {
            return Optional.of(b);
        }
    }
    return Optional.empty();
}
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "OldBadge",
  "description": "Old description",
  "requiredPoints": 3000
}
```

---

### Ejemplo 3: Actualizar un Badge

**Petición:**
```
PUT http://localhost:8080/api/badges/1
Content-Type: application/json

{
  "name": "EcoMaster Updated",
  "description": "Updated description",
  "requiredPoints": 6000
}
```

**Controlador:**
```java
@PutMapping("/{id}")
public ResponseEntity<?> update(@PathVariable long id, @RequestBody Badge badge) {
    // @PathVariable id = 1
    // @RequestBody badge = Badge(0, "EcoMaster Updated", "Updated...", 6000)
    
    try {
        Badge updated = service.update(id, badge);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
```

**Servicio:**
```java
public Badge update(long id, Badge badge) {
    Badge existing = getById(id);
    if (existing == null) {
        return null;
    }
    
    existing.setName(badge.getName());
    existing.setDescription(badge.getDescription());
    existing.setRequiredPoints(badge.getRequiredPoints());
    
    return repo.save(existing);
}
```

---

### Ejemplo 4: Eliminar un Badge

**Petición:**
```
DELETE http://localhost:8080/api/badges/1
```

**Controlador:**
```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable long id) {
    // @PathVariable id = 1
    
    boolean deleted = service.delete(id);
    return deleted
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
}
```

**Servicio:**
```java
public boolean delete(long id) {
    return repo.deleteById(id);
}
```

**Repositorio:**
```java
public boolean deleteById(long id) {
    List<Badge> all = new ArrayList<>(findAll());
    boolean removed = false;
    
    for (int i = 0; i < all.size(); i++) {
        if (all.get(i).getId() == id) {
            all.remove(i);
            removed = true;
            break;
        }
    }
    
    if (removed) {
        writeAll(all);
    }
    return removed;
}
```

**Respuesta:**
```
204 No Content
```

---

## Resumen Visual

```
┌─────────────────────────────────────────────────────────────┐
│                    PETICIÓN HTTP                             │
│  POST /api/badges                                            │
│  { "name": "EcoMaster", "requiredPoints": 5000 }            │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│         @RestController (BadgeController)                    │
│  @PostMapping + @RequestBody                                │
│  ↓ Recibe JSON y lo convierte a Badge                       │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│         @Service (BadgeService)                              │
│  Valida: requiredPoints > 0                                 │
│  ↓ Si es válido, llama a repo.save()                        │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│         @Repository (BadgeCsvRepository)                     │
│  Asigna ID automático                                        │
│  ↓ Convierte Badge a CSV                                    │
│  ↓ Escribe en badges.csv                                    │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│              badges.csv (Persistencia)                       │
│  id,name,description,requiredPoints                         │
│  2,EcoMaster,Top recycler,5000                              │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                    RESPUESTA JSON                            │
│  { "id": 2, "name": "EcoMaster", "requiredPoints": 5000 }  │
└─────────────────────────────────────────────────────────────┘
```

---

## Conclusión

Las anotaciones (@) en Spring Boot son marcadores que le dicen al framework:
- **Qué es cada clase** (@Service, @Repository, @RestController)
- **Cómo manejar peticiones HTTP** (@GetMapping, @PostMapping, etc.)
- **Cómo convertir datos** (@RequestBody, @PathVariable)
- **Cómo generar código** (@Getter, @Setter, @NoArgsConstructor)

Sin anotaciones, tendrías que escribir miles de líneas de código manualmente.
Con anotaciones, Spring hace todo automáticamente.
