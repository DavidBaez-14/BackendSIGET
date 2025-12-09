-- Insertar estudiante 3 (Kevin Arias) - Para crear nuevo proyecto
-- Usuario base
INSERT INTO usuarios (cedula, email, password_hash, nombres, apellidos, telefono, pais_id, tipo_usuario, activo, fecha_registro)
VALUES ('1000077777', 'kevindavidav@ufps.edu.co', 'password123', 'Kevin', 'Arias', '3155551234', 1, 'ESTUDIANTE', true, CURRENT_TIMESTAMP);

-- Estudiante
INSERT INTO estudiantes (cedula, codigo_estudiantil, programa_codigo, fecha_ingreso)
VALUES ('1000077777', '1151777', '115', '2023-02-01');
