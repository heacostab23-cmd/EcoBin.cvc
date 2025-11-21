# 🌍 ECOBIN CSV API - Sistema de Reciclaje Gamificado

## 📋 Descripción

**EcoBin** es una API REST desarrollada en **Spring Boot** que gestiona un sistema de reciclaje gamificado. Los ciudadanos ganan puntos por reciclar, pueden canjear puntos por recompensas, completar misiones y ganar insignias.

---

## 🎯 Características Principales

- ✅ **11 Entidades** con CRUD completo
- ✅ **Persistencia en CSV** (sin base de datos)
- ✅ **Arquitectura en capas** (Controller → Service → Repository)
- ✅ **Inyección de dependencias** con Spring
- ✅ **Validaciones** en cada capa
- ✅ **API REST** con 11 controladores
- ✅ **62 archivos Java** compilados exitosamente

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────┐
│     CONTROLADORES (REST API)        │
│  @RestController, @RequestMapping   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     SERVICIOS (Lógica Negocio)      │
│         @Service                    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│   REPOSITORIOS (Persistencia)       │
│      @Repository                    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      ARCHIVOS CSV (Datos)           │
│   src/main/resources/data/*.csv     │
└─────────────────────────────────────┘
```

---

## 📊 Entidades

| Entidad | Descripción | Endpoints |
|---------|-------------|-----------|
| **Badge** | Insignias por logros | `/api/badges` |
| **Citizen** | Ciudadanos recicladores | `/api/citizens` |
| **EcoBinPoint** | Puntos de reciclaje | `/api/ecobin-points` |
| **Mission** | Misiones gamificadas | `/api/missions` |
| **MissionRule** | Reglas de misión | `/api/mission-rules` |
| **Operator** | Operadores supervisores | `/api/operators` |
| **Reading** | Lecturas de peso | `/api/readings` |
| **Redemption** | Canjes de recompensas | `/api/redemptions` |
| **Reward** | Recompensas disponibles | `/api/rewards` |
| **User** | Cuentas de usuario | `/api/users` |
| **WasteType** | Tipos de residuo | `/api/waste-types` |

---

## 🚀 Cómo Ejecutar

### Requisitos
- Java 17+
- Maven 3.9+
- Postman (para pruebas)

### Pasos

1. **Compilar el proyecto:**
```bash
mvn clean compile
```

2. **Ejecutar el servidor:**
```bash
mvn spring-boot:run
```

3. **Servidor disponible en:**
```
http://localhost:8080
```

---

## 📡 Endpoints Disponibles

### CRUD Básico (Ejemplo: Badges)

#### Listar todos
```
GET /api/badges
```

#### Obtener uno
```
GET /api/badges/1
```

#### Crear
```
POST /api/badges
Content-Type: application/json

{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

#### Actualizar
```
PUT /api/badges/1
Content-Type: application/json

{
  "name": "EcoMaster Pro",
  "description": "Updated",
  "requiredPoints": 6000
}
```

#### Eliminar
```
DELETE /api/badges/1
```

---

## 🔍 Anotaciones Principales

### @RestController
Marca una clase como controlador REST que maneja peticiones HTTP.

### @RequestMapping
Define la ruta base para los endpoints.

### @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
Define el tipo de petición HTTP y la ruta específica.

### @PathVariable
Extrae parámetros de la URL.

### @RequestBody
Convierte JSON de la petición en un objeto Java.

### @Service
Marca una clase como servicio de lógica de negocio.

### @Repository
Marca una clase como repositorio de acceso a datos.

### @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor
Anotaciones de Lombok que generan automáticamente getters, setters y constructores.

---

## 📁 Estructura del Proyecto

```
ecobin-csv-api/
├── src/main/java/co/edu/umanizales/ecobin_csv_api/
│   ├── EcobinCsvApiApplication.java
│   ├── controller/          (11 controladores)
│   ├── service/             (11 servicios)
│   ├── repository/          (11 repositorios)
│   ├── mapper/              (11 mappers)
│   └── model/               (18 modelos)
├── src/main/resources/data/ (11 archivos CSV)
├── pom.xml
├── DOCUMENTACION.md         (Documentación completa)
├── GUIA_ANOTACIONES.md      (Guía de anotaciones)
├── LOGICA_PROYECTO.md       (Lógica del proyecto)
└── README.md                (Este archivo)
```

---

## 🎓 Conceptos POO Aplicados

### Herencia
```java
public abstract class Person {
    protected long id;
    protected String document;
}

public class Citizen extends Person {
    private User user;
    private long points;
}
```

### Polimorfismo
```java
public interface Authenticable {
    boolean active();
    Set<Role> roles();
}

public class User implements Authenticable {
    // Implementación
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

## 🔐 Inyección de Dependencias

El proyecto usa inyección de dependencias de Spring para desacoplar las clases:

```java
@Service
public class BadgeService {
    private final BadgeCsvRepository repo;
    
    public BadgeService(BadgeCsvRepository repo) {
        this.repo = repo;
    }
}
```

---

## 📊 Flujo de una Petición

```
1. Cliente envía petición HTTP
   POST /api/badges
   { "name": "EcoMaster", "requiredPoints": 5000 }
   
2. @RestController recibe
   BadgeController.create(@RequestBody Badge badge)
   
3. @Service valida
   BadgeService.create(Badge badge)
   - Valida: requiredPoints > 0
   
4. @Repository guarda
   BadgeCsvRepository.save(Badge entity)
   - Asigna ID automático
   - Convierte a CSV
   
5. Mapper convierte
   BadgeCsvMapper.toCsv(Badge b)
   - Badge → String[]
   
6. Se escribe en CSV
   badges.csv
   id,name,description,requiredPoints
   2,EcoMaster,Top recycler,5000
   
7. Respuesta al cliente
   { "id": 2, "name": "EcoMaster", ... }
```

---

## 🧪 Pruebas en Postman

### Crear un Badge
```
POST http://localhost:8080/api/badges
{
  "name": "EcoMaster",
  "description": "Top recycler in the neighborhood",
  "requiredPoints": 5000
}
```

### Crear un Ciudadano
```
POST http://localhost:8080/api/citizens
{
  "document": "123456789",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "user": { "id": 1 },
  "points": 0
}
```

### Crear una Lectura
```
POST http://localhost:8080/api/readings
{
  "point": { "id": 1 },
  "wasteType": { "id": 1 },
  "citizen": { "id": 1 },
  "grams": 500,
  "date": "2025-11-19"
}
```

### Crear una Recompensa
```
POST http://localhost:8080/api/rewards
{
  "name": "Eco Bag",
  "costPoints": 1000,
  "stock": 50,
  "description": "Bolsa ecológica reutilizable"
}
```

### Crear un Canje
```
POST http://localhost:8080/api/redemptions
{
  "citizen": { "id": 1 },
  "reward": { "id": 1 },
  "status": "REQUESTED",
  "date": "2025-11-19"
}
```

---

## 📝 Validaciones

Cada servicio valida reglas de negocio:

- **Reading**: grams > 0
- **Reward**: costPoints > 0, stock >= 0
- **Badge**: requiredPoints > 0
- **MissionRule**: targetKg > 0, isoPeriod no vacío
- **Citizen**: document único
- **Operator**: document único
- **User**: email único, passwordHash no vacío

---

## 🎯 Casos de Uso

### 1. Registrar Lectura
Ciudadano recicla → Sistema registra peso → Se crea Reading

### 2. Canjear Recompensa
Ciudadano solicita canje → Operador aprueba → Se crea Redemption

### 3. Completar Misión
Sistema verifica regla → Ciudadano cumple → Misión completada

### 4. Gestionar Operador
Admin crea operador → Asigna usuario → Operador activo

---

## 📚 Documentación Adicional

- **DOCUMENTACION.md** - Documentación completa del proyecto
- **GUIA_ANOTACIONES.md** - Guía detallada de anotaciones (@)
- **LOGICA_PROYECTO.md** - Lógica y flujos de negocio

---

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.5.7**
- **Maven 3.9**
- **Lombok**
- **CSV (Persistencia)**

---

## 📊 Estadísticas

- **62 archivos Java**
- **11 Controladores REST**
- **11 Servicios**
- **11 Repositorios CSV**
- **11 Mappers**
- **18 Modelos**
- **11 archivos CSV**
- **100% funcional**

---

## 🤝 Contribuciones

Este proyecto fue desarrollado como práctica de:
- Arquitectura en capas
- Spring Boot
- Inyección de dependencias
- Persistencia en CSV
- API REST
- Conceptos POO

---

## 📞 Soporte

Para preguntas o problemas:
1. Revisar DOCUMENTACION.md
2. Revisar GUIA_ANOTACIONES.md
3. Revisar LOGICA_PROYECTO.md

---

## 📄 Licencia

Este proyecto es de código abierto para propósitos educativos.

---

## ✅ Estado

- ✅ Compilación exitosa
- ✅ Servidor corriendo en puerto 8080
- ✅ Todos los endpoints funcionales
- ✅ Persistencia en CSV operativa
- ✅ Validaciones implementadas
- ✅ Documentación completa

**¡Proyecto completado y listo para usar!** 🎉
