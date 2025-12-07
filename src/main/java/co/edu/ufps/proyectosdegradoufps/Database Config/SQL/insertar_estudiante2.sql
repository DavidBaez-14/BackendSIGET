-- ==========================================
-- INSERTAR ESTUDIANTE 2 (Sin proyecto)
-- ==========================================
-- Este estudiante será usado para probar las invitaciones

-- 1. Insertar en tabla usuarios
INSERT INTO public.usuarios (cedula, email, password_hash, nombres, apellidos, telefono, pais_id, tipo_usuario, activo, fecha_registro)
VALUES (
    '1000055555',                                    -- Cédula
    'estudiante2@ufps.edu.co',                       -- Email
    '$2b$10$defaultHash',                            -- Password (temporal)
    'Laura',                                         -- Nombres
    'Martínez',                                      -- Apellidos
    '3105551234',                                    -- Teléfono
    1,                                               -- País (Colombia)
    'ESTUDIANTE',                                    -- Tipo usuario
    true,                                            -- Activo
    CURRENT_TIMESTAMP                                -- Fecha registro
);

-- 2. Insertar en tabla estudiantes
INSERT INTO public.estudiantes (cedula, codigo_estudiantil, programa_codigo, fecha_ingreso)
VALUES (
    '1000055555',                                    -- Cédula
    '1151555',                                       -- Código estudiantil
    '115',                                           -- Programa (Ing. Sistemas)
    '2020-01-15'                                     -- Fecha ingreso
);

-- ==========================================
-- VERIFICACIÓN
-- ==========================================
-- Puedes ejecutar esto para confirmar que se creó correctamente:

SELECT 
    u.cedula,
    u.email,
    u.tipo_usuario,
    e.codigo_estudiantil,
    p.nombre as programa
FROM usuarios u
INNER JOIN estudiantes e ON u.cedula = e.cedula
INNER JOIN programas p ON e.programa_codigo = p.codigo
WHERE u.cedula = '1000055555';
