# SIGET UFPS - Backend

## Sistema Integral de Gestión de Tesis
**Universidad Francisco de Paula Santander**

---

## 📋 Descripción

Backend (API REST) del Sistema Integral de Gestión de Tesis (SIGET) de la UFPS. Proporciona los servicios necesarios para gestionar proyectos de grado, usuarios, historial de eventos y catálogos institucionales.

---

## 🚀 Tecnologías

- **Java 21** - Lenguaje de programación
- **Spring Boot 4.0** - Framework backend
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Seguridad (configuración básica)
- **PostgreSQL** - Base de datos relacional
- **Supabase** - Hosting de base de datos en la nube
- **Maven** - Gestión de dependencias

---

## 📦 Módulos

| Módulo | Descripción |
|--------|-------------|
| **proyectos** | Gestión de proyectos de grado (CRUD, consultas por rol) |
| **historial** | Registro de eventos y cambios de estado |
| **usuarios** | Gestión de estudiantes, profesores y administradores |
| **config** | Configuración de CORS y seguridad |

---

## 🔗 Endpoints Principales

### Proyectos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/proyectos` | Listar todos los proyectos |
| GET | `/proyectos/{id}` | Obtener proyecto por ID |
| GET | `/proyectos/estudiante/{cedula}` | Proyectos de un estudiante |
| GET | `/proyectos/director/{cedula}` | Proyectos de un director |
| GET | `/proyectos/admin/{cedula}` | Proyectos filtrados por programa |
| POST | `/proyectos` | Crear nuevo proyecto |

### Historial
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/historial/proyecto/{id}` | Historial de un proyecto |
| POST | `/historial/proyecto/{id}/cambiar-estado` | Cambiar estado del proyecto |
| GET | `/historial/eventos-cambio-estado` | Catálogo de tipos de evento |

### Catálogos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/catalogos/modalidades` | Modalidades de proyecto |
| GET | `/catalogos/estados` | Estados de proyecto |
| GET | `/catalogos/areas-investigacion` | Áreas de investigación |
| GET | `/catalogos/lineas-investigacion` | Líneas de investigación |

---

## ⚙️ Instalación

### Requisitos previos
- Java JDK 21 o superior
- Maven 3.9 o superior
- IDE recomendado: IntelliJ IDEA

### Pasos

```bash
# Clonar el repositorio
git clone https://github.com/DavidBaez-14/BackendSIGET.git

# Entrar a la carpeta
cd BackendSIGET

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

El servidor estará disponible en `http://localhost:8080`

### Alternativa: Ejecutar desde IDE
1. Abrir el proyecto en IntelliJ IDEA
2. Ejecutar la clase `ProyectosDeGradoUfpsApplication.java`

---

## 🗄️ Base de Datos

La base de datos está alojada en **Supabase** (PostgreSQL).

### Configuración
El archivo `src/main/resources/application.properties` contiene la configuración de conexión:

```properties
spring.datasource.url=jdbc:postgresql://[HOST]:5432/postgres
spring.datasource.username=[USUARIO]
spring.datasource.password=[CONTRASEÑA]
```

> ⚠️ Las credenciales de producción se proporcionan por separado por seguridad.

---

## 📁 Estructura del Proyecto

```
src/main/java/co/edu/ufps/proyectosdegradoufps/
├── config/              # Configuración CORS y Security
├── historial/           # Módulo de historial
│   ├── controllers/
│   ├── dtos/
│   ├── models/
│   ├── repositories/
│   └── services/
├── proyectos/           # Módulo de proyectos
│   ├── controllers/
│   ├── dtos/
│   ├── models/
│   ├── repositories/
│   └── services/
└── usuarios/            # Módulo de usuarios
    ├── controllers/
    ├── dtos/
    ├── models/
    ├── repositories/
    └── services/
```

---

## 🔗 Frontend

Este backend se complementa con el frontend de SIGET.

**Repositorio Frontend:** [FrontendSIGET](https://github.com/DavidBaez-14/FrontendSIGET)

---

## 📄 Licencia

Proyecto académico - Universidad Francisco de Paula Santander

---

**Desarrollado por:** David Báez  
**Programa:** Ingeniería de Sistemas  
**Año:** 2024
