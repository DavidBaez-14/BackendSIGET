# Módulo de Usuarios - API Endpoints

## Descripción
Este módulo gestiona todos los usuarios del sistema de proyectos de grado de la UFPS, incluyendo estudiantes, profesores, directores externos y administradores.

## Estructura del Módulo

```
usuarios/
├── models/          # Entidades JPA
│   ├── Usuario.java
│   ├── Estudiante.java
│   ├── Profesor.java
│   ├── DirectorExterno.java
│   └── Administrador.java
├── repositories/    # Repositorios Spring Data JPA
│   ├── UsuarioRepository.java
│   ├── EstudianteRepository.java
│   ├── ProfesorRepository.java
│   ├── DirectorExternoRepository.java
│   └── AdministradorRepository.java
├── dtos/           # Data Transfer Objects
│   ├── UsuarioDTO.java
│   ├── EstudianteDTO.java
│   ├── ProfesorDTO.java
│   ├── DirectorExternoDTO.java
│   └── AdministradorDTO.java
├── services/       # Lógica de negocio
│   └── UsuarioService.java
└── controllers/    # Controladores REST
    └── UsuarioController.java
```

## Endpoints API

### Base URL: `/api/usuarios`

---

## 👥 Endpoints Generales de Usuarios

### Obtener todos los usuarios
```http
GET /api/usuarios
```
**Acceso:** ADMIN

**Response:**
```json
[
  {
    "cedula": "1098765432",
    "email": "usuario@ufps.edu.co",
    "nombres": "Juan",
    "apellidos": "Pérez",
    "telefono": "3001234567",
    "direccion": "Calle 1 #2-3",
    "paisCodigo": "CO",
    "fechaNacimiento": "2000-01-15",
    "tipoUsuario": "ESTUDIANTE"
  }
]
```

### Obtener usuario por cédula
```http
GET /api/usuarios/{cedula}
```
**Acceso:** ADMIN, Usuario propietario

**Response:** DTO del usuario específico

### Obtener usuario por email
```http
GET /api/usuarios/email/{email}
```
**Acceso:** ADMIN

---

## 🎓 Endpoints de Estudiantes

### Listar todos los estudiantes
```http
GET /api/usuarios/estudiantes
```
**Acceso:** ADMIN

### Obtener estudiante por cédula
```http
GET /api/usuarios/estudiantes/{cedula}
```
**Acceso:** ADMIN, Estudiante propietario

### Obtener estudiante por código estudiantil
```http
GET /api/usuarios/estudiantes/codigo/{codigoEstudiantil}
```
**Acceso:** ADMIN

**Ejemplo:**
```http
GET /api/usuarios/estudiantes/codigo/1151829
```

### Crear estudiante
```http
POST /api/usuarios/estudiantes
```
**Acceso:** ADMIN

**Request Body:**
```json
{
  "cedula": "1098765432",
  "email": "estudiante@ufps.edu.co",
  "nombres": "María",
  "apellidos": "González",
  "telefono": "3001234567",
  "direccion": "Calle 10 #20-30",
  "paisCodigo": "CO",
  "fechaNacimiento": "2002-05-20",
  "codigoEstudiantil": "1151829",
  "programaCodigo": "223",
  "fechaIngreso": "2020-02-01"
}
```

**Response:** `201 Created` con el DTO del estudiante creado

### Actualizar estudiante
```http
PUT /api/usuarios/estudiantes/{cedula}
```
**Acceso:** ADMIN, Estudiante propietario

**Request Body:** Mismo formato que creación

### Eliminar estudiante
```http
DELETE /api/usuarios/estudiantes/{cedula}
```
**Acceso:** ADMIN

**Response:** `204 No Content`

---

## 👨‍🏫 Endpoints de Profesores

### Listar todos los profesores
```http
GET /api/usuarios/profesores
```
**Acceso:** ADMIN, ESTUDIANTE (para selección de director)

### Obtener profesor por cédula
```http
GET /api/usuarios/profesores/{cedula}
```
**Acceso:** ADMIN

### Obtener profesores por tipo
```http
GET /api/usuarios/profesores/tipo/{tipoProfesor}
```
**Acceso:** ADMIN

**Tipos válidos:**
- `PLANTA`
- `CATEDRA`
- `OCASIONAL`

**Ejemplo:**
```http
GET /api/usuarios/profesores/tipo/PLANTA
```

### Crear profesor
```http
POST /api/usuarios/profesores
```
**Acceso:** ADMIN

**Request Body:**
```json
{
  "cedula": "13456789",
  "email": "profesor@ufps.edu.co",
  "nombres": "Carlos",
  "apellidos": "Martínez",
  "telefono": "3157654321",
  "direccion": "Av. Gran Colombia #5-67",
  "paisCodigo": "CO",
  "fechaNacimiento": "1975-08-15",
  "tituloAcademico": "Doctor en Ingeniería de Sistemas",
  "especialidad": "Inteligencia Artificial",
  "tipoProfesor": "PLANTA"
}
```

### Actualizar profesor
```http
PUT /api/usuarios/profesores/{cedula}
```
**Acceso:** ADMIN

### Eliminar profesor
```http
DELETE /api/usuarios/profesores/{cedula}
```
**Acceso:** ADMIN

---

## 🏢 Endpoints de Directores Externos

### Listar todos los directores externos
```http
GET /api/usuarios/directores-externos
```
**Acceso:** ADMIN

### Obtener director externo por cédula
```http
GET /api/usuarios/directores-externos/{cedula}
```
**Acceso:** ADMIN

### Obtener directores externos por estado de aprobación
```http
GET /api/usuarios/directores-externos/aprobados/{aprobado}
```
**Acceso:** ADMIN

**Valores válidos:**
- `true` - Directores aprobados por el comité
- `false` - Directores pendientes de aprobación

**Ejemplo:**
```http
GET /api/usuarios/directores-externos/aprobados/true
```

### Crear director externo
```http
POST /api/usuarios/directores-externos
```
**Acceso:** ADMIN, ESTUDIANTE (solicitud pendiente de aprobación)

**Request Body:**
```json
{
  "cedula": "80123456",
  "email": "director.externo@empresa.com",
  "nombres": "Laura",
  "apellidos": "Ramírez",
  "telefono": "3209876543",
  "direccion": "Calle 50 #30-40",
  "paisCodigo": "CO",
  "fechaNacimiento": "1970-03-12",
  "institucionProcedencia": "Ecopetrol S.A.",
  "aprobadoComite": false
}
```

### Actualizar director externo
```http
PUT /api/usuarios/directores-externos/{cedula}
```
**Acceso:** ADMIN

**Caso de uso:** Aprobar director externo por parte del comité
```json
{
  "aprobadoComite": true
}
```

### Eliminar director externo
```http
DELETE /api/usuarios/directores-externos/{cedula}
```
**Acceso:** ADMIN

---

## 👔 Endpoints de Administradores

### Listar todos los administradores
```http
GET /api/usuarios/administradores
```
**Acceso:** ADMIN

### Obtener administrador por cédula
```http
GET /api/usuarios/administradores/{cedula}
```
**Acceso:** ADMIN

### Obtener administradores por cargo
```http
GET /api/usuarios/administradores/cargo/{cargo}
```
**Acceso:** ADMIN

**Cargos comunes:**
- `Director de Departamento`
- `Coordinador de Trabajos de Grado`
- `Decano`

**Ejemplo:**
```http
GET /api/usuarios/administradores/cargo/Director%20de%20Departamento
```

### Crear administrador
```http
POST /api/usuarios/administradores
```
**Acceso:** ADMIN

**Request Body:**
```json
{
  "cedula": "5678901",
  "email": "director@ufps.edu.co",
  "nombres": "Roberto",
  "apellidos": "Silva",
  "telefono": "3186543210",
  "direccion": "Calle 100 #20-10",
  "paisCodigo": "CO",
  "fechaNacimiento": "1965-11-25",
  "profesorCedula": "13456789",
  "cargo": "Director de Departamento",
  "fechaInicioCargo": "2023-01-01",
  "fechaFinCargo": "2025-12-31"
}
```

### Actualizar administrador
```http
PUT /api/usuarios/administradores/{cedula}
```
**Acceso:** ADMIN

### Eliminar administrador
```http
DELETE /api/usuarios/administradores/{cedula}
```
**Acceso:** ADMIN

---

## Control de Acceso por Rol

### ADMIN (Administrador)
- ✅ Acceso completo a todos los endpoints
- ✅ CRUD de todos los tipos de usuario
- ✅ Aprobación de directores externos

### ESTUDIANTE
- ✅ Ver su propio perfil (`GET /api/usuarios/estudiantes/{suCedula}`)
- ✅ Actualizar su propio perfil (`PUT /api/usuarios/estudiantes/{suCedula}`)
- ✅ Listar profesores para selección de director
- ✅ Crear solicitud de director externo (requiere aprobación)
- ❌ Ver información de otros estudiantes
- ❌ Acceso a endpoints administrativos

### PROFESOR/DIRECTOR
- ✅ Ver su propio perfil
- ✅ Ver estudiantes de proyectos que dirige
- ✅ Actualizar su propio perfil
- ❌ Ver información de otros profesores
- ❌ Acceso a endpoints administrativos

---

## Códigos de Estado HTTP

- `200 OK` - Operación exitosa (GET, PUT)
- `201 Created` - Recurso creado exitosamente (POST)
- `204 No Content` - Eliminación exitosa (DELETE)
- `400 Bad Request` - Datos inválidos o conflicto de negocio
- `404 Not Found` - Recurso no encontrado
- `403 Forbidden` - Acceso denegado por permisos

---

## Validaciones de Negocio

1. **Cédula única:** No puede haber dos usuarios con la misma cédula
2. **Email único:** No puede haber dos usuarios con el mismo email
3. **Código estudiantil único:** No puede haber dos estudiantes con el mismo código
4. **Director externo:** Debe ser aprobado por el comité antes de dirigir proyectos
5. **Administrador:** Debe estar vinculado a un profesor existente

---

## Notas de Implementación

- Todos los endpoints usan `@CrossOrigin(origins = "*")` para desarrollo
- La herencia JOINED permite consultar usuarios por tipo específico
- Los DTOs evitan exponer `passwordHash` en las respuestas
- Las validaciones de existencia previenen duplicados
- Los métodos de conversión entity↔DTO están centralizados en el servicio
