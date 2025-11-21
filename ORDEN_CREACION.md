# 📋 ORDEN CORRECTO PARA CREAR DATOS - ECOBIN CSV API

## 🎯 Principio Fundamental

**Crear primero las entidades independientes, luego las que dependen de ellas.**

---

## 📊 Diagrama de Dependencias

```
┌─────────────────────────────────────────────────────────────┐
│ NIVEL 1: ENTIDADES INDEPENDIENTES (Sin dependencias)       │
├─────────────────────────────────────────────────────────────┤
│ • Badge                                                     │
│ • WasteType                                                 │
│ • Reward                                                    │
│ • User                                                      │
│ • EcoBinPoint                                               │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ NIVEL 2: ENTIDADES QUE DEPENDEN DE NIVEL 1                │
├─────────────────────────────────────────────────────────────┤
│ • Citizen (depende de: User)                               │
│ • Operator (depende de: User)                              │
│ • MissionRule (depende de: WasteType)                      │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ NIVEL 3: ENTIDADES QUE DEPENDEN DE NIVEL 2                │
├─────────────────────────────────────────────────────────────┤
│ • Mission (depende de: MissionRule)                        │
│ • Reading (depende de: EcoBinPoint, WasteType, Citizen)   │
│ • Redemption (depende de: Citizen, Reward)                │
└─────────────────────────────────────────────────────────────┘
```

---

## ✅ ORDEN PASO A PASO

### PASO 1️⃣: CREAR ENTIDADES INDEPENDIENTES

#### 1.1 Crear USERS
```
POST http://localhost:8080/api/users
{
  "email": "user1@example.com",
  "passwordHash": "hash123",
  "active": true
}
```

**Por qué primero:** Citizen y Operator necesitan un User.

---

#### 1.2 Crear WASTE TYPES
```
POST http://localhost:8080/api/waste-types
{
  "name": "Plástico",
  "description": "Botellas y envases"
}
```

**Por qué:** MissionRule necesita WasteType, y Reading también.

---

#### 1.3 Crear ECOBIN POINTS
```
POST http://localhost:8080/api/ecobin-points
{
  "name": "Punto Parque Principal",
  "location": {
    "lat": 5.067,
    "lon": -75.518,
    "address": "Parque principal de Manizales"
  }
}
```

**Por qué:** Reading necesita EcoBinPoint.

---

#### 1.4 Crear REWARDS
```
POST http://localhost:8080/api/rewards
{
  "name": "EcoBottle",
  "costPoints": 500,
  "stock": 10,
  "description": "Reusable bottle for eco citizens"
}
```

**Por qué:** Redemption necesita Reward.

---

#### 1.5 Crear BADGES
```
POST http://localhost:8080/api/badges
{
  "name": "EcoMaster",
  "description": "Top recycler",
  "requiredPoints": 5000
}
```

**Por qué:** Badge es independiente (opcional).

---

### PASO 2️⃣: CREAR ENTIDADES QUE DEPENDEN DE PASO 1

#### 2.1 Crear CITIZENS
```
POST http://localhost:8080/api/citizens
{
  "document": "1007706799",
  "firstName": "Mafe",
  "lastName": "Serna",
  "email": "mafe@example.com",
  "user": { "id": 1 },
  "points": 0
}
```

**Dependencias:**
- ✅ User con id=1 debe existir (creado en 1.1)

**Por qué:** Reading y Redemption necesitan Citizen.

---

#### 2.2 Crear OPERATORS
```
POST http://localhost:8080/api/operators
{
  "document": "1234567890",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "user": { "id": 2 }
}
```

**Dependencias:**
- ✅ User con id=2 debe existir (creado en 1.1)

**Por qué:** Operator supervisa el sistema (opcional).

---

#### 2.3 Crear MISSION RULES
```
POST http://localhost:8080/api/mission-rules
{
  "wasteType": { "id": 1 },
  "targetKg": 10,
  "isoPeriod": "P7D"
}
```

**Dependencias:**
- ✅ WasteType con id=1 debe existir (creado en 1.2)

**Por qué:** Mission necesita MissionRule.

---

### PASO 3️⃣: CREAR ENTIDADES QUE DEPENDEN DE PASO 2

#### 3.1 Crear MISSIONS
```
POST http://localhost:8080/api/missions
{
  "name": "Recicla 10kg de Plástico",
  "rule": { "id": 1 },
  "status": "ACTIVE"
}
```

**Dependencias:**
- ✅ MissionRule con id=1 debe existir (creado en 2.3)

**Por qué:** Mission es el objetivo gamificado.

---

#### 3.2 Crear READINGS
```
POST http://localhost:8080/api/readings
{
  "point": { "id": 1 },
  "wasteType": { "id": 1 },
  "citizen": { "id": 1 },
  "grams": 500,
  "date": "2025-11-20"
}
```

**Dependencias:**
- ✅ EcoBinPoint con id=1 debe existir (creado en 1.3)
- ✅ WasteType con id=1 debe existir (creado en 1.2)
- ✅ Citizen con id=1 debe existir (creado en 2.1)

**Por qué:** Reading registra el reciclaje del ciudadano.

---

#### 3.3 Crear REDEMPTIONS
```
POST http://localhost:8080/api/redemptions
{
  "citizen": { "id": 1 },
  "reward": { "id": 1 },
  "status": "REQUESTED",
  "date": "2025-11-20"
}
```

**Dependencias:**
- ✅ Citizen con id=1 debe existir (creado en 2.1)
- ✅ Reward con id=1 debe existir (creado en 1.4)

**Por qué:** Redemption es el canje de puntos por recompensas.

---

## 📊 TABLA RESUMEN

| Paso | Entidad | Dependencias | Descripción |
|------|---------|--------------|-------------|
| 1.1 | **User** | Ninguna | Cuenta de acceso |
| 1.2 | **WasteType** | Ninguna | Tipo de residuo |
| 1.3 | **EcoBinPoint** | Ninguna | Punto de reciclaje |
| 1.4 | **Reward** | Ninguna | Recompensa |
| 1.5 | **Badge** | Ninguna | Insignia |
| 2.1 | **Citizen** | User | Ciudadano reciclador |
| 2.2 | **Operator** | User | Operador supervisor |
| 2.3 | **MissionRule** | WasteType | Regla de misión |
| 3.1 | **Mission** | MissionRule | Misión gamificada |
| 3.2 | **Reading** | EcoBinPoint, WasteType, Citizen | Lectura de reciclaje |
| 3.3 | **Redemption** | Citizen, Reward | Canje de recompensa |

---

## 🔄 FLUJO COMPLETO DE UN CIUDADANO

```
1. CREAR USER
   ↓
2. CREAR CITIZEN (con User)
   ↓
3. CREAR READING (Citizen recicla)
   ↓
4. CREAR MISSION (objetivo)
   ↓
5. VERIFICAR MISSION (¿Completada?)
   ↓
6. CREAR REDEMPTION (canjear puntos)
   ↓
7. APROBAR REDEMPTION (operador)
   ↓
8. CIUDADANO RECIBE RECOMPENSA
```

---

## ⚠️ ERRORES COMUNES

### ❌ Error 1: Crear Citizen sin User
```
POST /api/citizens
{
  "document": "123",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "user": { "id": 999 }  ← User 999 no existe
}

Resultado: Error - User no encontrado
```

**Solución:** Crear User primero (paso 1.1)

---

### ❌ Error 2: Crear Reading sin EcoBinPoint
```
POST /api/readings
{
  "point": { "id": 999 },  ← EcoBinPoint 999 no existe
  "wasteType": { "id": 1 },
  "citizen": { "id": 1 },
  "grams": 500,
  "date": "2025-11-20"
}

Resultado: Error - EcoBinPoint no encontrado
```

**Solución:** Crear EcoBinPoint primero (paso 1.3)

---

### ❌ Error 3: Crear Mission sin MissionRule
```
POST /api/missions
{
  "name": "Misión",
  "rule": { "id": 999 },  ← MissionRule 999 no existe
  "status": "ACTIVE"
}

Resultado: Error - MissionRule no encontrada
```

**Solución:** Crear MissionRule primero (paso 2.3)

---

## 📝 DATOS DE EJEMPLO COMPLETOS

### Paso 1: Crear datos independientes

**users.csv:**
```csv
id,email,passwordHash,active,roles
1,user1@example.com,hash123,true,CITIZEN
2,user2@example.com,hash456,true,OPERATOR
3,user3@example.com,hash789,true,ADMIN
```

**waste_types.csv:**
```csv
id,name,description
1,Plástico,Botellas y envases
2,Vidrio,Botellas y frascos
3,Metal,Latas y metales
```

**ecobin_points.csv:**
```csv
id,name,lat,lon,address
1,Punto Parque Principal,5.067,-75.518,Parque principal de Manizales
2,Punto Centro,5.070,-75.520,Centro de la ciudad
```

**rewards.csv:**
```csv
id,name,costPoints,stock,description
1,EcoBottle,500,10,Reusable bottle
2,EcoBag,1000,5,Eco-friendly bag
```

**badges.csv:**
```csv
id,name,description,requiredPoints
1,EcoMaster,Top recycler,5000
2,EcoChampion,Champion recycler,10000
```

---

### Paso 2: Crear datos que dependen de paso 1

**citizens.csv:**
```csv
id,document,firstName,lastName,email,userId,points
1,1007706799,Mafe,Serna,mafe@example.com,1,0
2,1007706799,Kevin,Usma,kevinu@gmail.com,2,500
```

**operators.csv:**
```csv
id,document,firstName,lastName,email,userId
1,1234567890,Juan,Pérez,juan@example.com,3
```

**mission_rules.csv:**
```csv
id,wasteTypeId,targetKg,isoPeriod
1,1,10,P7D
2,2,5,P7D
```

---

### Paso 3: Crear datos que dependen de paso 2

**missions.csv:**
```csv
id,name,ruleId,status
1,Recicla 10kg de Plástico,1,ACTIVE
2,Recicla 5kg de Vidrio,2,ACTIVE
```

**readings.csv:**
```csv
id,pointId,wasteTypeId,citizenId,grams,date
1,1,1,1,500,2025-11-20
2,1,2,2,300,2025-11-20
```

**redemptions.csv:**
```csv
id,citizenId,rewardId,status,date
1,1,1,REQUESTED,2025-11-20
2,2,2,APPROVED,2025-11-20
```

---

## ✅ CHECKLIST DE CREACIÓN

- [ ] **Paso 1.1** - Crear 2 Users
- [ ] **Paso 1.2** - Crear 3 WasteTypes
- [ ] **Paso 1.3** - Crear 2 EcoBinPoints
- [ ] **Paso 1.4** - Crear 2 Rewards
- [ ] **Paso 1.5** - Crear 2 Badges
- [ ] **Paso 2.1** - Crear 2 Citizens
- [ ] **Paso 2.2** - Crear 1 Operator
- [ ] **Paso 2.3** - Crear 2 MissionRules
- [ ] **Paso 3.1** - Crear 2 Missions
- [ ] **Paso 3.2** - Crear 2 Readings
- [ ] **Paso 3.3** - Crear 2 Redemptions

---

## 🎯 Conclusión

**Orden correcto:**
1. **Primero:** Entidades independientes (User, WasteType, EcoBinPoint, Reward, Badge)
2. **Segundo:** Entidades que dependen de nivel 1 (Citizen, Operator, MissionRule)
3. **Tercero:** Entidades que dependen de nivel 2 (Mission, Reading, Redemption)

**Regla de oro:** Siempre crear las dependencias antes de crear la entidad que las necesita.
