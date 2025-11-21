# 📊 ESTRUCTURA CORRECTA DE ARCHIVOS CSV

## Resumen de Cambios

Se han corregido todos los archivos CSV para que correspondan exactamente con la estructura de los modelos Java.

---

## 📋 Estructura por Entidad

### 1. BADGE (Insignias)
**Archivo:** `badges.csv`

**Estructura:**
```
id,name,description,requiredPoints
```

**Campos:**
- `id` - Identificador único (long)
- `name` - Nombre de la insignia (String)
- `description` - Descripción (String)
- `requiredPoints` - Puntos requeridos para obtenerla (long)

**Ejemplo:**
```csv
id,name,description,requiredPoints
1,EcoMaster,Top recycler,5000
2,EcoChampion,Champion recycler,10000
```

---

### 2. CITIZEN (Ciudadanos)
**Archivo:** `citizens.csv`

**Estructura:**
```
id,document,firstName,lastName,email,userId,points
```

**Campos:**
- `id` - Identificador único (long)
- `document` - Documento de identidad (String)
- `firstName` - Nombre (String)
- `lastName` - Apellido (String)
- `email` - Email (String)
- `userId` - ID del usuario asociado (long)
- `points` - Puntos acumulados (long)

**Ejemplo:**
```csv
id,document,firstName,lastName,email,userId,points
1,1007706799,Mafe,Serna,mafe@example.com,1,0
2,1007706799,Kevin,Usma,kevinu@gmail.com,2,500
```

---

### 3. ECOBIN POINT (Puntos de Reciclaje)
**Archivo:** `ecobin_points.csv`

**Estructura:**
```
id,name,lat,lon,address
```

**Campos:**
- `id` - Identificador único (long)
- `name` - Nombre del punto (String)
- `lat` - Latitud (double)
- `lon` - Longitud (double)
- `address` - Dirección (String)

**Ejemplo:**
```csv
id,name,lat,lon,address
1,Punto Parque Principal,5.067,-75.518,Parque principal de Manizales
2,Punto Centro,5.070,-75.520,Centro de la ciudad
```

---

### 4. MISSION (Misiones)
**Archivo:** `missions.csv`

**Estructura:**
```
id,name,ruleId,status
```

**Campos:**
- `id` - Identificador único (long)
- `name` - Nombre de la misión (String)
- `ruleId` - ID de la regla asociada (long)
- `status` - Estado (ACTIVE, COMPLETED, FAILED)

**Ejemplo:**
```csv
id,name,ruleId,status
1,Recicla 10kg de Plástico,1,ACTIVE
2,Recicla 5kg de Vidrio,2,COMPLETED
```

---

### 5. MISSION RULE (Reglas de Misión)
**Archivo:** `mission_rules.csv`

**Estructura:**
```
id,wasteTypeId,targetKg,isoPeriod
```

**Campos:**
- `id` - Identificador único (long)
- `wasteTypeId` - ID del tipo de residuo (long)
- `targetKg` - Kilogramos objetivo (long)
- `isoPeriod` - Período ISO (String, ej: P7D, P30D)

**Ejemplo:**
```csv
id,wasteTypeId,targetKg,isoPeriod
1,1,10,P7D
2,2,5,P7D
```

---

### 6. OPERATOR (Operadores)
**Archivo:** `operators.csv`

**Estructura:**
```
id,document,firstName,lastName,email,userId
```

**Campos:**
- `id` - Identificador único (long)
- `document` - Documento de identidad (String)
- `firstName` - Nombre (String)
- `lastName` - Apellido (String)
- `email` - Email (String)
- `userId` - ID del usuario asociado (long)

**Ejemplo:**
```csv
id,document,firstName,lastName,email,userId
1,1234567890,Juan,Pérez,juan@example.com,1
2,0987654321,María,García,maria@example.com,2
```

---

### 7. READING (Lecturas)
**Archivo:** `readings.csv`

**Estructura:**
```
id,pointId,wasteTypeId,citizenId,grams,date
```

**Campos:**
- `id` - Identificador único (long)
- `pointId` - ID del punto EcoBin (long)
- `wasteTypeId` - ID del tipo de residuo (long)
- `citizenId` - ID del ciudadano (long)
- `grams` - Peso en gramos (long)
- `date` - Fecha (LocalDate, formato: YYYY-MM-DD)

**Ejemplo:**
```csv
id,pointId,wasteTypeId,citizenId,grams,date
1,1,1,1,500,2025-11-20
2,1,2,2,300,2025-11-20
```

---

### 8. REDEMPTION (Canjes)
**Archivo:** `redemptions.csv`

**Estructura:**
```
id,citizenId,rewardId,status,date
```

**Campos:**
- `id` - Identificador único (long)
- `citizenId` - ID del ciudadano (long)
- `rewardId` - ID de la recompensa (long)
- `status` - Estado (REQUESTED, APPROVED, DELIVERED, REJECTED)
- `date` - Fecha (LocalDate, formato: YYYY-MM-DD)

**Ejemplo:**
```csv
id,citizenId,rewardId,status,date
1,1,1,REQUESTED,2025-11-20
2,2,2,APPROVED,2025-11-20
```

---

### 9. REWARD (Recompensas)
**Archivo:** `rewards.csv`

**Estructura:**
```
id,name,costPoints,stock,description
```

**Campos:**
- `id` - Identificador único (long)
- `name` - Nombre de la recompensa (String)
- `costPoints` - Puntos que cuesta (long)
- `stock` - Cantidad disponible (int)
- `description` - Descripción (String)

**Ejemplo:**
```csv
id,name,costPoints,stock,description
1,EcoBottle,500,10,Reusable bottle for eco citizens
2,EcoBag,1000,5,Eco-friendly bag
```

---

### 10. USER (Usuarios)
**Archivo:** `users.csv`

**Estructura:**
```
id,email,passwordHash,active,roles
```

**Campos:**
- `id` - Identificador único (long)
- `email` - Email (String)
- `passwordHash` - Hash de contraseña (String)
- `active` - Activo (true/false)
- `roles` - Roles separados por comas (String, ej: "ADMIN,OPERATOR,CITIZEN")

**Roles disponibles:**
- `ADMIN` - Administrador del sistema
- `OPERATOR` - Operador supervisor
- `CITIZEN` - Ciudadano reciclador

**Ejemplo:**
```csv
id,email,passwordHash,active,roles
1,user1@example.com,hash123,true,CITIZEN
2,user2@example.com,hash456,true,OPERATOR
3,user3@example.com,hash789,true,ADMIN
```

---

### 11. WASTE TYPE (Tipos de Residuo)
**Archivo:** `waste_types.csv`

**Estructura:**
```
id,name,description
```

**Campos:**
- `id` - Identificador único (long)
- `name` - Nombre del tipo (String)
- `description` - Descripción (String)

**Ejemplo:**
```csv
id,name,description
1,Plástico,Botellas y envases
2,Vidrio,Botellas y frascos
3,Metal,Latas y metales
```

---

## 📝 Cambios Realizados

### ✅ CITIZENS.CSV
**Antes:**
```
id,document,firstName,lastName,email,points,badgeIds,userId
```

**Después:**
```
id,document,firstName,lastName,email,userId,points
```

**Cambios:**
- ❌ Removido: `badgeIds` (no es campo del modelo)
- ✅ Agregado: `userId` (relación con User)
- ✅ Reordenado: `userId` antes de `points`

---

### ✅ READINGS.CSV
**Antes:**
```
id,citizenId,ecoBinPointId,wasteTypeId,grams,dateTimeISO,pointsAwarded
```

**Después:**
```
id,pointId,wasteTypeId,citizenId,grams,date
```

**Cambios:**
- ❌ Removido: `dateTimeISO` (usar `date` en formato LocalDate)
- ❌ Removido: `pointsAwarded` (no es campo del modelo)
- ✅ Renombrado: `ecoBinPointId` → `pointId`
- ✅ Reordenado: campos en orden lógico

---

### ✅ MISSIONS.CSV
**Antes:**
```
id,name,description,wasteTypeId,targetKg,startDateISO,endDateISO,status,bonusPoints
```

**Después:**
```
id,name,ruleId,status
```

**Cambios:**
- ❌ Removido: `description`, `wasteTypeId`, `targetKg`, `startDateISO`, `endDateISO`, `bonusPoints`
- ✅ Agregado: `ruleId` (referencia a MissionRule que contiene wasteTypeId y targetKg)
- ✅ Simplificado: Mission solo tiene name, rule, status

---

### ✅ OPERATORS.CSV
**Antes:**
```
id,document,firstName,lastName,email
```

**Después:**
```
id,document,firstName,lastName,email,userId
```

**Cambios:**
- ✅ Agregado: `userId` (relación con User)

---

## 🎯 Validaciones por CSV

### CITIZENS
- ✅ `document` - Obligatorio, único
- ✅ `firstName` - Obligatorio
- ✅ `lastName` - Obligatorio
- ✅ `email` - Obligatorio
- ✅ `userId` - Debe existir en users.csv

### OPERATORS
- ✅ `document` - Obligatorio, único
- ✅ `firstName` - Obligatorio
- ✅ `lastName` - Obligatorio
- ✅ `email` - Obligatorio
- ✅ `userId` - Debe existir en users.csv

### READINGS
- ✅ `grams` - Debe ser > 0
- ✅ `pointId` - Debe existir en ecobin_points.csv
- ✅ `wasteTypeId` - Debe existir en waste_types.csv
- ✅ `citizenId` - Debe existir en citizens.csv

### MISSIONS
- ✅ `ruleId` - Debe existir en mission_rules.csv
- ✅ `status` - ACTIVE, COMPLETED, o FAILED

### REDEMPTIONS
- ✅ `citizenId` - Debe existir en citizens.csv
- ✅ `rewardId` - Debe existir en rewards.csv
- ✅ `status` - REQUESTED, APPROVED, DELIVERED, o REJECTED

---

## 📊 Relaciones entre CSV

```
users.csv
    ↓
citizens.csv (userId)
    ↓
readings.csv (citizenId)

ecobin_points.csv
    ↓
readings.csv (pointId)

waste_types.csv
    ↓
readings.csv (wasteTypeId)
    ↓
mission_rules.csv (wasteTypeId)
    ↓
missions.csv (ruleId)

rewards.csv
    ↓
redemptions.csv (rewardId)

citizens.csv
    ↓
redemptions.csv (citizenId)

operators.csv (userId)
    ↓
users.csv
```

---

## ✅ Estado Actual

- ✅ **badges.csv** - Correcto
- ✅ **citizens.csv** - Actualizado
- ✅ **ecobin_points.csv** - Correcto
- ✅ **missions.csv** - Actualizado
- ✅ **mission_rules.csv** - Correcto
- ✅ **operators.csv** - Actualizado
- ✅ **readings.csv** - Actualizado
- ✅ **redemptions.csv** - Correcto
- ✅ **rewards.csv** - Correcto
- ✅ **users.csv** - Correcto
- ✅ **waste_types.csv** - Correcto

---

## 🎉 Conclusión

Todos los archivos CSV ahora tienen la estructura correcta que corresponde exactamente con los modelos Java. Los mappers pueden leer y escribir correctamente en cada CSV.
