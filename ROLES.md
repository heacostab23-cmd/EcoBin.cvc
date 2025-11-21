# 👥 ROLES DEL SISTEMA - ECOBIN CSV API

## 🎯 Descripción

El sistema EcoBin tiene **3 roles** que definen los permisos y responsabilidades de cada usuario.

---

## 📋 Roles Disponibles

### 1. CITIZEN (Ciudadano)
**Descripción:** Persona que recicla y participa en el sistema gamificado.

**Responsabilidades:**
- ♻️ Reciclar residuos en EcoBinPoints
- 📊 Registrar lecturas de peso
- 🎯 Completar misiones
- 🏆 Ganar insignias
- 💰 Canjear puntos por recompensas

**Permisos:**
- ✅ Crear readings (lecturas)
- ✅ Ver sus propias readings
- ✅ Crear redemptions (canjes)
- ✅ Ver sus propias redemptions
- ✅ Ver misiones activas
- ✅ Ver insignias disponibles

**Ejemplo de Usuario:**
```csv
id,email,passwordHash,active,roles
1,mafe@example.com,1234,true,CITIZEN
```

---

### 2. OPERATOR (Operador)
**Descripción:** Persona que supervisa y gestiona el sistema de reciclaje.

**Responsabilidades:**
- 👁️ Supervisar lecturas de ciudadanos
- ✅ Aprobar/rechazar canjes de recompensas
- 📊 Generar reportes
- 🛠️ Mantener el sistema

**Permisos:**
- ✅ Ver todas las readings
- ✅ Ver todos los ciudadanos
- ✅ Aprobar/rechazar redemptions
- ✅ Ver todas las redemptions
- ✅ Gestionar misiones
- ✅ Ver reportes

**Ejemplo de Usuario:**
```csv
id,email,passwordHash,active,roles
2,operator@example.com,5678,true,OPERATOR
```

---

### 3. ADMIN (Administrador)
**Descripción:** Persona con acceso total al sistema.

**Responsabilidades:**
- 🔧 Administrar usuarios
- 🎛️ Configurar el sistema
- 📈 Gestionar todos los datos
- 🔐 Gestionar seguridad

**Permisos:**
- ✅ Acceso total a todas las entidades
- ✅ Crear/editar/eliminar usuarios
- ✅ Crear/editar/eliminar roles
- ✅ Ver todos los datos
- ✅ Generar reportes avanzados

**Ejemplo de Usuario:**
```csv
id,email,passwordHash,active,roles
3,admin@example.com,9012,true,ADMIN
```

---

## 🔄 Cómo Asignar Roles

### En el CSV (users.csv)

**Un solo rol:**
```csv
id,email,passwordHash,active,roles
1,user@example.com,hash,true,CITIZEN
```

**Múltiples roles (separados por coma):**
```csv
id,email,passwordHash,active,roles
2,user@example.com,hash,true,CITIZEN,OPERATOR
3,admin@example.com,hash,true,ADMIN,OPERATOR,CITIZEN
```

### Mediante API

**Crear usuario con rol CITIZEN:**
```json
POST /api/users
{
  "email": "citizen@example.com",
  "passwordHash": "hash123",
  "active": true,
  "roles": ["CITIZEN"]
}
```

**Crear usuario con múltiples roles:**
```json
POST /api/users
{
  "email": "operator@example.com",
  "passwordHash": "hash456",
  "active": true,
  "roles": ["OPERATOR", "CITIZEN"]
}
```

---

## 📊 Matriz de Permisos

| Acción | CITIZEN | OPERATOR | ADMIN |
|--------|---------|----------|-------|
| Ver propias readings | ✅ | ✅ | ✅ |
| Ver todas las readings | ❌ | ✅ | ✅ |
| Crear reading | ✅ | ✅ | ✅ |
| Ver propios canjes | ✅ | ✅ | ✅ |
| Ver todos los canjes | ❌ | ✅ | ✅ |
| Crear canje | ✅ | ✅ | ✅ |
| Aprobar canje | ❌ | ✅ | ✅ |
| Ver ciudadanos | ❌ | ✅ | ✅ |
| Crear ciudadano | ❌ | ❌ | ✅ |
| Editar ciudadano | ❌ | ❌ | ✅ |
| Eliminar ciudadano | ❌ | ❌ | ✅ |
| Gestionar usuarios | ❌ | ❌ | ✅ |
| Gestionar roles | ❌ | ❌ | ✅ |

---

## 🎯 Casos de Uso

### Caso 1: Ciudadano Recicla
```
1. Usuario CITIZEN recicla en EcoBinPoint
2. Crea una Reading
3. Sistema registra el reciclaje
4. Ciudadano gana puntos
```

### Caso 2: Operador Aprueba Canje
```
1. Ciudadano crea Redemption (canje)
2. Status: REQUESTED
3. Operador revisa la solicitud
4. Operador aprueba (status: APPROVED)
5. Ciudadano recibe recompensa
```

### Caso 3: Admin Gestiona Sistema
```
1. Admin crea nuevos usuarios
2. Admin asigna roles
3. Admin configura misiones
4. Admin genera reportes
```

---

## 🔐 Seguridad

### Buenas Prácticas

1. **No guardar contraseñas en texto plano**
   - Usar `passwordHash` en lugar de contraseña
   - Ejemplo: `hash123` (en producción usar bcrypt, SHA-256, etc.)

2. **Validar roles en el servidor**
   - No confiar en roles del cliente
   - Verificar permisos en cada endpoint

3. **Auditar cambios de roles**
   - Registrar quién cambió los roles
   - Registrar cuándo se hizo el cambio

4. **Desactivar usuarios en lugar de eliminar**
   - Usar campo `active: false`
   - Mantener historial de datos

---

## 📝 Estructura en CSV

**Archivo:** `users.csv`

**Estructura:**
```
id,email,passwordHash,active,roles
```

**Ejemplo completo:**
```csv
id,email,passwordHash,active,roles
1,mafe@example.com,1234,true,CITIZEN
2,operator@example.com,5678,true,OPERATOR
3,admin@example.com,9012,true,ADMIN
4,multi@example.com,3456,true,CITIZEN,OPERATOR
5,inactive@example.com,7890,false,CITIZEN
```

---

## 🔄 Cómo Funciona el Mapper

### De CSV a Objeto Java

```java
// CSV: 1,mafe@example.com,1234,true,CITIZEN,OPERATOR
String[] c = {"1", "mafe@example.com", "1234", "true", "CITIZEN,OPERATOR"};

User u = new User();
u.setId(1);
u.setEmail("mafe@example.com");
u.setPasswordHash("1234");
u.setActive(true);

// Parsear roles
String[] roleNames = "CITIZEN,OPERATOR".split(",");
for (String roleName : roleNames) {
    u.addRole(Role.valueOf(roleName.trim()));
}
// Resultado: User con roles [CITIZEN, OPERATOR]
```

### De Objeto Java a CSV

```java
User u = new User();
u.setId(1);
u.setEmail("mafe@example.com");
u.setPasswordHash("1234");
u.setActive(true);
u.addRole(Role.CITIZEN);
u.addRole(Role.OPERATOR);

// Convertir a CSV
String[] csv = mapper.toCsv(u);
// Resultado: ["1", "mafe@example.com", "1234", "true", "CITIZEN,OPERATOR"]
```

---

## ✅ Validaciones

### En el Modelo
```java
@NotBlank(message = "Email is required")
private String email;

@NotBlank(message = "Password hash is required")
private String passwordHash;

private Set<Role> roles = new HashSet<>();
```

### En el Servicio
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

## 🎉 Conclusión

**Roles en EcoBin:**
- **CITIZEN** - Recicla y participa
- **OPERATOR** - Supervisa y aprueba
- **ADMIN** - Administra el sistema

**Características:**
- ✅ Múltiples roles por usuario
- ✅ Separación de responsabilidades
- ✅ Control de permisos
- ✅ Auditoría de cambios
