-- =====================================================
-- SISTEMA DE GESTIÓN DE PROYECTOS DE GRADO - UFPS
-- 21 TABLAS
-- =====================================================

-- =====================================================
-- CATEGORÍA: CLASIFICACIÓN (5 tablas)
-- =====================================================

CREATE TABLE paises (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE facultades (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    codigo VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE programas (
    codigo INTEGER PRIMARY KEY,
    snies VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    facultad_id INTEGER NOT NULL,
    FOREIGN KEY (facultad_id) REFERENCES facultades(id)
);

CREATE TABLE areas_investigacion (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT true
);

CREATE TABLE lineas_investigacion (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    area_id INTEGER NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT true,
    FOREIGN KEY (area_id) REFERENCES areas_investigacion(id)
);

-- =====================================================
-- CATEGORÍA: USUARIOS (4 tablas)
-- =====================================================

CREATE TABLE usuarios (
    cedula VARCHAR(20) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    pais_id INTEGER NOT NULL,
    tipo_usuario VARCHAR(20) NOT NULL CHECK (tipo_usuario IN ('ESTUDIANTE', 'PROFESOR', 'DIRECTOR_EXTERNO')),
    activo BOOLEAN DEFAULT true,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pais_id) REFERENCES paises(id)
);

CREATE TABLE estudiantes (
    cedula VARCHAR(20) PRIMARY KEY,
    codigo_estudiantil VARCHAR(20) NOT NULL UNIQUE,
    programa_codigo INTEGER NOT NULL,
    fecha_ingreso DATE,
    FOREIGN KEY (cedula) REFERENCES usuarios(cedula) ON DELETE CASCADE,
    FOREIGN KEY (programa_codigo) REFERENCES programas(codigo)
);

CREATE TABLE profesores (
    cedula VARCHAR(20) PRIMARY KEY,
    titulo_academico VARCHAR(50),
    especialidad VARCHAR(200),
    tipo_profesor VARCHAR(20) CHECK (tipo_profesor IN ('PLANTA', 'CATEDRATICO', 'OCASIONAL')),
    fecha_vinculacion DATE,
    FOREIGN KEY (cedula) REFERENCES usuarios(cedula) ON DELETE CASCADE
);

CREATE TABLE directores_externos (
    cedula VARCHAR(20) PRIMARY KEY,
    titulo_academico VARCHAR(50),
    institucion_procedencia VARCHAR(200),
    especialidad VARCHAR(200),
    aprobado_comite BOOLEAN DEFAULT false,
    fecha_aprobacion DATE,
    FOREIGN KEY (cedula) REFERENCES usuarios(cedula) ON DELETE CASCADE
);

-- =====================================================
-- CATEGORÍA: GESTIÓN - CATÁLOGOS (4 tablas)
-- =====================================================

CREATE TABLE comites (
    id SERIAL PRIMARY KEY,
    programa_codigo INTEGER NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    fecha_creacion DATE,
    FOREIGN KEY (programa_codigo) REFERENCES programas(codigo)
);

CREATE TABLE tipos_proyecto (
    id SERIAL PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL UNIQUE CHECK (tipo IN ('INVESTIGACION', 'EXTENSION')),
    descripcion TEXT
);

CREATE TABLE modalidades_proyecto (
    id SERIAL PRIMARY KEY,
    tipo_proyecto_id INTEGER NOT NULL,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    FOREIGN KEY (tipo_proyecto_id) REFERENCES tipos_proyecto(id),
    CHECK (
        (tipo_proyecto_id = 1 AND nombre IN ('MONOGRAFIA', 'TRABAJO_INVESTIGACION', 'SISTEMATIZACION_CONOCIMIENTO')) OR
        (tipo_proyecto_id = 2 AND nombre IN ('TRABAJO_SOCIAL', 'CONSULTORIA', 'PASANTIA', 'TRABAJO_DIRIGIDO'))
    )
);

CREATE TABLE estados_proyecto (
    id SERIAL PRIMARY KEY,
    estado VARCHAR(50) NOT NULL UNIQUE,
    fase VARCHAR(30) NOT NULL,
    descripcion TEXT,
    orden INTEGER NOT NULL
);

-- =====================================================
-- CATEGORÍA: CORE PROYECTO (5 tablas)
-- =====================================================

CREATE TABLE proyectos (
    id SERIAL PRIMARY KEY,
    codigo_proyecto VARCHAR(50) UNIQUE,
    
    -- Participantes
    director_profesor_cedula VARCHAR(20),
    director_externo_cedula VARCHAR(20),
    
    -- Clasificación
    modalidad_id INTEGER NOT NULL,
    linea_investigacion_id INTEGER NOT NULL,
    
    -- Información básica
    titulo VARCHAR(500) NOT NULL,
    descripcion TEXT,
    objetivo_general TEXT,
    
    -- Estado
    estado_id INTEGER NOT NULL,
    
    -- Fechas
    fecha_presentacion DATE,
    fecha_inicio_desarrollo DATE,
    fecha_fin_estimada DATE,
    fecha_sustentacion DATE,
    
    -- Seguimiento
    porcentaje_avance INTEGER DEFAULT 0 CHECK (porcentaje_avance >= 0 AND porcentaje_avance <= 100),
    
    -- Resultados
    nota_final DECIMAL(2,1) CHECK (nota_final >= 0.0 AND nota_final <= 5.0),
    resultado_final VARCHAR(20) CHECK (resultado_final IN ('APROBADA', 'MERITORIA', 'LAUREADA', 'RECHAZADA', 'A_CORREGIR')),
    
    -- Documentos
    documento_propuesta_url VARCHAR(500),
    documento_final_url VARCHAR(500),
    acta_sustentacion VARCHAR(100),
    
    -- Auditoría
    fecha_ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (director_profesor_cedula) REFERENCES profesores(cedula),
    FOREIGN KEY (director_externo_cedula) REFERENCES directores_externos(cedula),
    FOREIGN KEY (modalidad_id) REFERENCES modalidades_proyecto(id),
    FOREIGN KEY (linea_investigacion_id) REFERENCES lineas_investigacion(id),
    FOREIGN KEY (estado_id) REFERENCES estados_proyecto(id),
    
    CHECK (
        (director_profesor_cedula IS NOT NULL AND director_externo_cedula IS NULL) OR
        (director_profesor_cedula IS NULL AND director_externo_cedula IS NOT NULL)
    )
);

CREATE TABLE estudiantes_proyecto (
    id SERIAL PRIMARY KEY,
    proyecto_id INTEGER NOT NULL,
    estudiante_cedula VARCHAR(20) NOT NULL,
    fecha_vinculacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT true,
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id) ON DELETE CASCADE,
    FOREIGN KEY (estudiante_cedula) REFERENCES estudiantes(cedula) ON DELETE CASCADE,
    UNIQUE (proyecto_id, estudiante_cedula)
);

CREATE TABLE jurados (
    id SERIAL PRIMARY KEY,
    proyecto_id INTEGER NOT NULL,
    profesor_cedula VARCHAR(20) NOT NULL,
    calificacion_individual DECIMAL(2,1) CHECK (calificacion_individual >= 0.0 AND calificacion_individual <= 5.0),
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id) ON DELETE CASCADE,
    FOREIGN KEY (profesor_cedula) REFERENCES profesores(cedula),
    UNIQUE (proyecto_id, profesor_cedula)
);

CREATE TABLE reuniones (
    id SERIAL PRIMARY KEY,
    proyecto_id INTEGER NOT NULL,
    fecha_reunion TIMESTAMP NOT NULL,
    duracion_minutos INTEGER NOT NULL CHECK (duracion_minutos > 0),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('PRESENCIAL', 'VIRTUAL', 'CORREO', 'TELEFONICA')),
    temas_tratados TEXT NOT NULL,
    acuerdos TEXT NOT NULL,
    proxima_reunion DATE,
    asistio_estudiante BOOLEAN NOT NULL,
    observaciones TEXT,
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id) ON DELETE CASCADE
);

CREATE TABLE actas_comite (
    id SERIAL PRIMARY KEY,
    comite_id INTEGER NOT NULL,
    proyecto_id INTEGER NOT NULL,
    tipo_acta VARCHAR(50) NOT NULL,
    numero_acta VARCHAR(50) NOT NULL UNIQUE,
    fecha_acta DATE NOT NULL,
    concepto VARCHAR(20) CHECK (concepto IN ('APROBADO', 'CORRECCIONES', 'RECHAZADO')),
    observaciones TEXT,
    documento_acta_url VARCHAR(500),
    mencion_otorgada VARCHAR(20) CHECK (mencion_otorgada IN ('MERITORIA', 'LAUREADA')),
    FOREIGN KEY (comite_id) REFERENCES comites(id),
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id) ON DELETE CASCADE
);

CREATE TABLE evaluadores_asignados (
    id SERIAL PRIMARY KEY,
    acta_nombramiento_id INTEGER NOT NULL,
    profesor_cedula VARCHAR(20) NOT NULL,
    fecha_entrega_evaluacion DATE,
    FOREIGN KEY (acta_nombramiento_id) REFERENCES actas_comite(id) ON DELETE CASCADE,
    FOREIGN KEY (profesor_cedula) REFERENCES profesores(cedula),
    UNIQUE (acta_nombramiento_id, profesor_cedula)
);

-- =====================================================
-- CATEGORÍA: HISTORIAL (2 tablas)
-- =====================================================

CREATE TABLE tipos_evento_historial (
    id SERIAL PRIMARY KEY,
    evento VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    categoria VARCHAR(30),
    cambia_estado BOOLEAN DEFAULT false,
    estado_resultante_id INTEGER,
    descripcion TEXT,
    orden INTEGER,
    FOREIGN KEY (estado_resultante_id) REFERENCES estados_proyecto(id)
);

CREATE TABLE historiales (
    id SERIAL PRIMARY KEY,
    proyecto_id INTEGER NOT NULL,
    tipo_evento_id INTEGER NOT NULL,
    descripcion TEXT,
    fecha_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_responsable_cedula VARCHAR(20),
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id) ON DELETE CASCADE,
    FOREIGN KEY (tipo_evento_id) REFERENCES tipos_evento_historial(id),
    FOREIGN KEY (usuario_responsable_cedula) REFERENCES usuarios(cedula)
);

-- =====================================================
-- CATEGORÍA: COMUNICACIÓN (1 tabla)
-- =====================================================

CREATE TABLE notificaciones (
    id SERIAL PRIMARY KEY,
    usuario_cedula VARCHAR(20) NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    enlace VARCHAR(500),
    leida BOOLEAN DEFAULT false,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_lectura TIMESTAMP,
    FOREIGN KEY (usuario_cedula) REFERENCES usuarios(cedula) ON DELETE CASCADE
);

-- =====================================================
-- CATEGORÍA: ROLES (1 tabla)
-- =====================================================

CREATE TABLE administradores (
    id SERIAL PRIMARY KEY,
    profesor_cedula VARCHAR(20) NOT NULL UNIQUE,
    cargo VARCHAR(200) NOT NULL,
    fecha_inicio_cargo DATE NOT NULL,
    fecha_fin_cargo DATE,
    activo BOOLEAN DEFAULT true,
    FOREIGN KEY (profesor_cedula) REFERENCES profesores(cedula) ON DELETE CASCADE
);

-- =====================================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================

CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_tipo ON usuarios(tipo_usuario);
CREATE INDEX idx_usuarios_activo ON usuarios(activo);

CREATE INDEX idx_estudiantes_programa ON estudiantes(programa_codigo);
CREATE INDEX idx_estudiantes_codigo ON estudiantes(codigo_estudiantil);

CREATE INDEX idx_profesores_tipo ON profesores(tipo_profesor);

CREATE INDEX idx_lineas_area ON lineas_investigacion(area_id);

CREATE INDEX idx_proyectos_director_profesor ON proyectos(director_profesor_cedula);
CREATE INDEX idx_proyectos_director_externo ON proyectos(director_externo_cedula);
CREATE INDEX idx_proyectos_modalidad ON proyectos(modalidad_id);
CREATE INDEX idx_proyectos_linea ON proyectos(linea_investigacion_id);
CREATE INDEX idx_proyectos_estado ON proyectos(estado_id);
CREATE INDEX idx_proyectos_codigo ON proyectos(codigo_proyecto);

CREATE INDEX idx_estudiantes_proyecto_estudiante ON estudiantes_proyecto(estudiante_cedula, activo);
CREATE INDEX idx_estudiantes_proyecto_proyecto ON estudiantes_proyecto(proyecto_id, activo);

CREATE INDEX idx_jurados_proyecto ON jurados(proyecto_id);
CREATE INDEX idx_jurados_profesor ON jurados(profesor_cedula);

CREATE INDEX idx_reuniones_proyecto ON reuniones(proyecto_id);
CREATE INDEX idx_reuniones_fecha ON reuniones(fecha_reunion DESC);

CREATE INDEX idx_historiales_proyecto_fecha ON historiales(proyecto_id, fecha_evento DESC);
CREATE INDEX idx_historiales_tipo ON historiales(tipo_evento_id);
CREATE INDEX idx_historiales_usuario ON historiales(usuario_responsable_cedula);

CREATE INDEX idx_notificaciones_usuario_leida ON notificaciones(usuario_cedula, leida);
CREATE INDEX idx_notificaciones_fecha ON notificaciones(fecha_creacion DESC);

CREATE INDEX idx_actas_comite ON actas_comite(comite_id);
CREATE INDEX idx_actas_proyecto ON actas_comite(proyecto_id);
CREATE INDEX idx_actas_numero ON actas_comite(numero_acta);

CREATE INDEX idx_modalidades_tipo ON modalidades_proyecto(tipo_proyecto_id);

-- =====================================================
-- COMENTARIOS EN TABLAS
-- =====================================================

COMMENT ON TABLE usuarios IS 'Tabla principal de usuarios del sistema (estudiantes, profesores, directores externos)';
COMMENT ON TABLE estudiantes IS 'Especialización de usuarios tipo estudiante';
COMMENT ON TABLE profesores IS 'Especialización de usuarios tipo profesor';
COMMENT ON TABLE directores_externos IS 'Especialización de usuarios tipo director externo';
COMMENT ON TABLE areas_investigacion IS 'Áreas de investigación que agrupan líneas relacionadas';
COMMENT ON TABLE lineas_investigacion IS 'Líneas de investigación específicas asociadas a áreas';
COMMENT ON TABLE proyectos IS 'Proyectos de grado desde propuesta hasta finalización';
COMMENT ON TABLE estudiantes_proyecto IS 'Relación muchos-a-muchos entre estudiantes y proyectos. Máximo 3 estudiantes por proyecto. Mantiene historial completo';
COMMENT ON TABLE estados_proyecto IS 'Catálogo de estados posibles de un proyecto';
COMMENT ON TABLE tipos_proyecto IS 'Catálogo de tipos: Investigación, Extensión';
COMMENT ON TABLE modalidades_proyecto IS 'Catálogo de modalidades por tipo de proyecto';
COMMENT ON TABLE jurados IS 'Jurados asignados para sustentación de proyectos';
COMMENT ON TABLE reuniones IS 'Reuniones entre directores y estudiantes';
COMMENT ON TABLE tipos_evento_historial IS 'Catálogo de eventos que pueden ocurrir en un proyecto';
COMMENT ON TABLE historiales IS 'Registro cronológico de todos los eventos de proyectos';
COMMENT ON TABLE actas_comite IS 'Actas oficiales emitidas por comités curriculares';
COMMENT ON TABLE notificaciones IS 'Notificaciones para usuarios del sistema';
COMMENT ON TABLE administradores IS 'Profesores con rol administrativo (directora de programa, etc)';

COMMENT ON COLUMN proyectos.estado_id IS 'Estado actual del proyecto';
COMMENT ON COLUMN proyectos.resultado_final IS 'Resultado de sustentación: APROBADA (3.0-4.4), MERITORIA (4.5-4.9), LAUREADA (5.0), A_CORREGIR (2.0-2.9), RECHAZADA (<2.0)';
COMMENT ON COLUMN tipos_evento_historial.cambia_estado IS 'Indica si este evento modifica el estado del proyecto';
COMMENT ON COLUMN tipos_evento_historial.estado_resultante_id IS 'Estado al que transiciona el proyecto cuando ocurre este evento';

-- =====================================================
-- FIN DEL DDL
-- Total: 21 TABLAS
-- =====================================================