# ✅ GUÍA DE VALIDACIONES - ECOBIN CSV API

## 🎯 Validaciones Implementadas

Se han agregado validaciones de **campos obligatorios** en los controladores usando `@Valid` y `@NotBlank`.

---

## 📋 Campos Obligatorios por Entidad

### CITIZEN (Ciudadano)
- ✅ `document` - Documento de identidad (obligatorio)
- ✅ `firstName` - Nombre (obligatorio)
- ✅ `lastName` - Apellido (obligatorio)
- ✅ `email` - Email (obligatorio)
- ✅ `user` - Usuario (obligatorio)
- ✅ `points` - Puntos (puede ser 0)

### OPERATOR (Operador)
- ✅ `document` - Documento de identidad (obligatorio)
- ✅ `firstName` - Nombre (obligatorio)
- ✅ `lastName` - Apellido (obligatorio)
- ✅ `email` - Email (obligatorio)
- ✅ `user` - Usuario (obligatorio)

---

## 🧪 Ejemplos de Prueba en Postman

### ✅ Crear Citizen CORRECTO

**Petición:**
```
POST http://localhost:8080/api/citizens
Content-Type: application/json

{
  "document": "123456789",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "user": { "id": 1 },
  "points": 0
}
```

**Respuesta (200 OK):**
```json
{
  "id": 1,
  "document": "123456789",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "user": { "id": 1 },
  "points": 0,
  "badges": [],
  "readings": []
}
```

---

### ❌ Crear Citizen SIN NOMBRE

**Petición:**
```
POST http://localhost:8080/api/citizens
Content-Type: application/json

{
  "document": "123456789",
  "firstName": "",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "user": { "id": 1 },
  "points": 0
}
```

**Respuesta (400 Bad Request):**
```
First name is required; 
```

---

### ❌ Crear Citizen SIN DOCUMENTO

**Petición:**
```
POST http://localhost:8080/api/citizens
Content-Type: application/json

{
  "document": "",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "user": { "id": 1 },
  "points": 0
}
```

**Respuesta (400 Bad Request):**
```
Document is required; 
```

---

### ❌ Crear Citizen SIN APELLIDO

**Petición:**
```
POST http://localhost:8080/api/citizens
Content-Type: application/json

{
  "document": "123456789",
  "firstName": "Juan",
  "lastName": "",
  "email": "juan@example.com",
  "user": { "id": 1 },
  "points": 0
}
```

**Respuesta (400 Bad Request):**
```
Last name is required; 
```

---

### ❌ Crear Citizen SIN EMAIL

**Petición:**
```
POST http://localhost:8080/api/citizens
Content-Type: application/json

{
  "document": "123456789",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "",
  "user": { "id": 1 },
  "points": 0
}
```

**Respuesta (400 Bad Request):**
```
Email is required; 
```

---

### ❌ Crear Citizen CON MÚLTIPLES CAMPOS VACÍOS

**Petición:**
```
POST http://localhost:8080/api/citizens
Content-Type: application/json

{
  "document": "",
  "firstName": "",
  "lastName": "",
  "email": "",
  "user": { "id": 1 },
  "points": 0
}
```

**Respuesta (400 Bad Request):**
```
Document is required; First name is required; Last name is required; Email is required; 
```

---

## 🔧 Cómo Funcionan las Validaciones

### 1. Anotación en el Modelo
```java
@NotBlank(message = "First name is required")
protected String firstName;
```

### 2. Anotación en el Controlador
```java
@PostMapping
public ResponseEntity<?> create(@Valid @RequestBody Citizen citizen, BindingResult bindingResult) {
    // @Valid activa las validaciones
    // BindingResult captura los errores
    
    if (bindingResult.hasErrors()) {
        // Construir mensaje de error
        StringBuilder errors = new StringBuilder();
        bindingResult.getFieldErrors().forEach(error -> 
            errors.append(error.getDefaultMessage()).append("; ")
        );
        return ResponseEntity.badRequest().body(errors.toString());
    }
    
    // Si pasa validación, crear
    return ResponseEntity.ok(service.create(citizen));
}
```

### 3. Flujo de Validación
```
1. Cliente envía JSON
   ↓
2. @RequestBody convierte JSON a objeto
   ↓
3. @Valid activa validaciones
   ↓
4. Si hay errores → BindingResult los captura
   ↓
5. Si bindingResult.hasErrors() → Devolver 400 Bad Request
   ↓
6. Si no hay errores → Continuar con la lógica
```

---

## 📊 Códigos de Respuesta HTTP

| Código | Significado | Ejemplo |
|--------|-------------|---------|
| **200 OK** | Éxito | Citizen creado correctamente |
| **400 Bad Request** | Validación fallida | Campo obligatorio vacío |
| **404 Not Found** | Recurso no existe | Citizen con ID 999 no existe |
| **500 Server Error** | Error del servidor | Excepción no manejada |

---

## 🎯 Casos de Prueba

### Caso 1: Crear Citizen con todos los datos
```
✅ DEBE FUNCIONAR
```

### Caso 2: Crear Citizen sin nombre
```
❌ DEBE FALLAR con "First name is required"
```

### Caso 3: Crear Citizen sin documento
```
❌ DEBE FALLAR con "Document is required"
```

### Caso 4: Crear Citizen sin apellido
```
❌ DEBE FALLAR con "Last name is required"
```

### Caso 5: Crear Citizen sin email
```
❌ DEBE FALLAR con "Email is required"
```

### Caso 6: Crear Citizen con múltiples campos vacíos
```
❌ DEBE FALLAR con todos los mensajes de error
```

### Caso 7: Actualizar Citizen sin nombre
```
❌ DEBE FALLAR con "First name is required"
```

### Caso 8: Crear Operator con todos los datos
```
✅ DEBE FUNCIONAR
```

### Caso 9: Crear Operator sin nombre
```
❌ DEBE FALLAR con "First name is required"
```

---

## 🔍 Validaciones Adicionales en Servicios

Además de las validaciones de campos obligatorios, los servicios validan:

### CitizenService
```java
// Document único
if (repo.findByDocument(citizen.getDocument()).isPresent()) {
    throw new IllegalArgumentException("Document already exists");
}
```

### OperatorService
```java
// Document único
if (repo.findByDocument(operator.getDocument()).isPresent()) {
    throw new IllegalArgumentException("Document already exists");
}
```

### UserService
```java
// Email único
if (repo.findByEmail(user.getEmail()).isPresent()) {
    throw new IllegalArgumentException("Email already exists");
}

// Password no vacío
if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
    throw new IllegalArgumentException("Password hash is required");
}
```

---

## 📝 Anotaciones de Validación Utilizadas

### @NotBlank
```java
@NotBlank(message = "First name is required")
protected String firstName;
```
**¿Qué valida?**
- No puede ser null
- No puede estar vacío
- No puede ser solo espacios en blanco

**Ejemplo de rechazo:**
```
"firstName": ""      ❌
"firstName": "   "   ❌
"firstName": null    ❌
"firstName": "Juan"  ✅
```

---

## 🚀 Próximas Mejoras (Opcional)

1. **@Email** - Validar formato de email
```java
@Email(message = "Email must be valid")
protected String email;
```

2. **@Min, @Max** - Validar rangos
```java
@Min(value = 0, message = "Points cannot be negative")
private long points;
```

3. **@Pattern** - Validar con expresiones regulares
```java
@Pattern(regexp = "^[0-9]{10}$", message = "Document must be 10 digits")
protected String document;
```

4. **@Size** - Validar longitud
```java
@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
protected String firstName;
```

---

## 📞 Resumen

✅ **Validaciones implementadas:**
- Campos obligatorios en Citizen
- Campos obligatorios en Operator
- Mensajes de error claros
- Respuestas HTTP apropiadas (400 Bad Request)
- Validaciones en servicios (documento único, email único)

✅ **Cómo funciona:**
1. Cliente envía JSON sin campos obligatorios
2. Spring valida usando `@Valid`
3. Si hay errores, devuelve 400 Bad Request con mensaje
4. Si no hay errores, continúa con la lógica de negocio

✅ **Listo para producción:**
- Validaciones robustas
- Mensajes de error descriptivos
- Manejo de excepciones
- Respuestas HTTP estándar
