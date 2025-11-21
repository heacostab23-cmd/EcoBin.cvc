# ⚡ GUÍA RÁPIDA - ECOBIN CSV API

## 🚀 Iniciar Proyecto

```bash
# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run

# Servidor en http://localhost:8080
```

---

## 📌 Anotaciones Más Importantes

| Anotación | Dónde | Qué Hace |
|-----------|-------|---------|
| `@RestController` | Clase | Marca como controlador REST |
| `@RequestMapping` | Clase | Define ruta base |
| `@GetMapping` | Método | GET HTTP |
| `@PostMapping` | Método | POST HTTP |
| `@PutMapping` | Método | PUT HTTP |
| `@DeleteMapping` | Método | DELETE HTTP |
| `@PathVariable` | Parámetro | Extrae de URL |
| `@RequestBody` | Parámetro | Convierte JSON |
| `@Service` | Clase | Servicio de negocio |
| `@Repository` | Clase | Repositorio de datos |
| `@Getter` | Clase | Genera getters |
| `@Setter` | Clase | Genera setters |

---

## 🔄 Flujo Típico

```
1. Cliente envía petición HTTP
   ↓
2. @RestController recibe en @GetMapping/@PostMapping/etc
   ↓
3. @RequestBody convierte JSON a objeto (si aplica)
   ↓
4. @Service valida lógica de negocio
   ↓
5. @Repository guarda/lee en CSV
   ↓
6. Mapper convierte objeto a CSV (o CSV a objeto)
   ↓
7. Respuesta JSON al cliente
```

---

## 📡 Endpoints Básicos

### Crear
```
POST /api/badges
{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

### Listar
```
GET /api/badges
```

### Obtener uno
```
GET /api/badges/1
```

### Actualizar
```
PUT /api/badges/1
{
  "name": "EcoMaster Pro",
  "description": "Updated",
  "requiredPoints": 6000
}
```

### Eliminar
```
DELETE /api/badges/1
```

---

## 🎯 Estructura de Carpetas

```
controller/     → Manejan peticiones HTTP
service/        → Validan lógica de negocio
repository/     → Leen/escriben CSV
mapper/         → Convierten CSV ↔ Objetos
model/          → Definen entidades
resources/data/ → Archivos CSV
```

---

## 💡 Conceptos Clave

### Inyección de Dependencias
```java
@Service
public class BadgeService {
    private final BadgeCsvRepository repo;
    
    public BadgeService(BadgeCsvRepository repo) {
        this.repo = repo;  // Spring proporciona automáticamente
    }
}
```

### Validación en Servicio
```java
public Badge create(Badge badge) {
    if (badge.getRequiredPoints() <= 0) {
        throw new IllegalArgumentException("Must be > 0");
    }
    return repo.save(badge);
}
```

### Respuesta HTTP
```java
@GetMapping("/{id}")
public ResponseEntity<Badge> get(@PathVariable long id) {
    Badge badge = service.getById(id);
    return badge != null
        ? ResponseEntity.ok(badge)
        : ResponseEntity.notFound().build();
}
```

---

## 📊 Las 11 Entidades

1. **Badge** - Insignias
2. **Citizen** - Ciudadanos
3. **EcoBinPoint** - Puntos de reciclaje
4. **Mission** - Misiones
5. **MissionRule** - Reglas de misión
6. **Operator** - Operadores
7. **Reading** - Lecturas
8. **Redemption** - Canjes
9. **Reward** - Recompensas
10. **User** - Usuarios
11. **WasteType** - Tipos de residuo

---

## 🔍 Validaciones Comunes

```java
// Número positivo
if (value <= 0) throw new IllegalArgumentException("Must be > 0");

// String no vacío
if (str == null || str.isBlank()) throw new IllegalArgumentException("Required");

// Único
if (repo.findByDocument(doc).isPresent()) throw new IllegalArgumentException("Already exists");

// Rango
if (value < 0 || value > 100) throw new IllegalArgumentException("Out of range");
```

---

## 📝 Ejemplo Completo: Crear Badge

### 1. Petición Postman
```
POST http://localhost:8080/api/badges
Content-Type: application/json

{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

### 2. Controlador
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
        try {
            Badge created = service.create(badge);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
```

### 3. Servicio
```java
@Service
public class BadgeService {
    private final BadgeCsvRepository repo;
    
    public BadgeService(BadgeCsvRepository repo) {
        this.repo = repo;
    }
    
    public Badge create(Badge badge) {
        if (badge.getRequiredPoints() <= 0) {
            throw new IllegalArgumentException("Required points must be > 0");
        }
        return repo.save(badge);
    }
}
```

### 4. Repositorio
```java
@Repository
public class BadgeCsvRepository {
    public Badge save(Badge entity) {
        List<Badge> all = new ArrayList<>(findAll());
        if (entity.getId() == 0) {
            entity.setId(nextId());
        }
        all.add(entity);
        writeAll(all);
        return entity;
    }
}
```

### 5. Respuesta
```json
{
  "id": 2,
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

---

## 🧪 Pruebas Rápidas

### Crear Badge
```
POST /api/badges
{ "name": "Test", "description": "Test", "requiredPoints": 1000 }
```

### Crear Ciudadano
```
POST /api/citizens
{ "document": "123", "firstName": "Juan", "lastName": "Pérez", "email": "juan@test.com", "user": {"id": 1}, "points": 0 }
```

### Crear Lectura
```
POST /api/readings
{ "point": {"id": 1}, "wasteType": {"id": 1}, "citizen": {"id": 1}, "grams": 500, "date": "2025-11-19" }
```

### Crear Recompensa
```
POST /api/rewards
{ "name": "Eco Bag", "costPoints": 1000, "stock": 50, "description": "Bolsa ecológica" }
```

### Crear Canje
```
POST /api/redemptions
{ "citizen": {"id": 1}, "reward": {"id": 1}, "status": "REQUESTED", "date": "2025-11-19" }
```

---

## 🎓 Conceptos POO

### Herencia
```java
public abstract class Person {
    protected long id;
}

public class Citizen extends Person {
    private User user;
}
```

### Polimorfismo
```java
public interface Authenticable {
    boolean active();
}

public class User implements Authenticable {
    @Override
    public boolean active() { return active; }
}
```

### Composición
```java
public class Mission {
    private MissionRule rule;  // Contiene una regla
}
```

### Encapsulación
```java
@Getter
@Setter
private long requiredPoints;  // Privado, acceso por getter/setter
```

---

## 🔐 Seguridad Básica

### Validar entrada
```java
if (badge.getName() == null || badge.getName().isBlank()) {
    throw new IllegalArgumentException("Name is required");
}
```

### Manejo de excepciones
```java
try {
    Badge created = service.create(badge);
    return ResponseEntity.ok(created);
} catch (IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
}
```

### Respuestas apropiadas
```java
200 OK        → Éxito
201 Created   → Recurso creado
204 No Content → Eliminado
400 Bad Request → Error de validación
404 Not Found → No existe
500 Server Error → Error del servidor
```

---

## 📚 Documentación

- **README.md** - Visión general
- **DOCUMENTACION.md** - Documentación completa
- **GUIA_ANOTACIONES.md** - Anotaciones detalladas
- **LOGICA_PROYECTO.md** - Lógica de negocio
- **RESUMEN_EJECUTIVO.md** - Resumen ejecutivo
- **GUIA_RAPIDA.md** - Esta guía

---

## ⚙️ Configuración

### Puerto
```
http://localhost:8080
```

### Archivos CSV
```
src/main/resources/data/
```

### Java Version
```
Java 17
```

### Spring Boot Version
```
3.5.7
```

---

## 🆘 Solución de Problemas

### Compilación falla
```bash
mvn clean compile
```

### Servidor no inicia
```bash
# Matar proceso anterior
Get-Process -Name java | Stop-Process -Force

# Reiniciar
mvn spring-boot:run
```

### CSV no se guarda
- Verificar permisos de carpeta
- Verificar ruta: `src/main/resources/data/`
- Verificar que el archivo existe

### Validación falla
- Revisar el mensaje de error
- Verificar datos en Postman
- Revisar lógica en servicio

---

## 💻 Comandos Útiles

```bash
# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run

# Compilar y ejecutar
mvn clean compile spring-boot:run

# Ver dependencias
mvn dependency:tree

# Limpiar
mvn clean
```

---

## 🎯 Checklist

- ✅ Compilación exitosa
- ✅ Servidor corriendo en puerto 8080
- ✅ Endpoints funcionan en Postman
- ✅ CSV se guarda correctamente
- ✅ Validaciones funcionan
- ✅ Documentación completa

---

## 📞 Resumen

**EcoBin CSV API** es un proyecto completo que demuestra:
- Spring Boot
- Arquitectura en capas
- Inyección de dependencias
- Conceptos POO
- API REST
- Persistencia en CSV

**¡Listo para usar y aprender!** 🚀
