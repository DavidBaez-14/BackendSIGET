INSERT INTO tipos_proyecto (id, tipo, descripcion) VALUES
(1, 'INVESTIGACION', 'Proyecto de Investigación: Generación o aplicación de conocimientos'),
(2, 'EXTENSION', 'Proyecto de Extensión: Trabajo con comunidades e instituciones externas');

-- Insertar modalidades de Investigación
INSERT INTO modalidades_proyecto (tipo_proyecto_id, nombre, descripcion) VALUES
(1, 'MONOGRAFIA', 
 'Trato sistemático, especial y completo de determinada parte de una ciencia o asunto en particular. Puede ser descriptiva o explicativa.'),

(1, 'TRABAJO_INVESTIGACION', 
 'Actividad intelectual encaminada a la construcción de conocimientos utilizando instrumentos racionales y materiales dentro del rigor científico.'),

(1, 'SISTEMATIZACION_CONOCIMIENTO', 
 'Organización y/o reorganización de saberes de una ciencia o disciplina, presentados en forma novedosa y didáctica.');

-- Insertar modalidades de Extensión
INSERT INTO modalidades_proyecto (tipo_proyecto_id, nombre, descripcion) VALUES
(2, 'TRABAJO_SOCIAL', 
 'Desarrollo de programas diseñados para instituciones o comunidades produciendo optimización en aspectos como educación, salud, recreación, medio ambiente, producción, etc.'),

(2, 'CONSULTORIA', 
 'Ejercicio profesional de concepción, elaboración y presentación de proyectos de inversión, infraestructura, ingeniería o desarrollo comunitario.'),

(2, 'PASANTIA', 
 'Rotación o permanencia del estudiante en una institución, realizando actividades propias de la profesión bajo dirección de un profesional experto.'),

(2, 'TRABAJO_DIRIGIDO', 
 'Desarrollo de un proyecto específico bajo dirección de un profesional, siguiendo plan establecido en anteproyecto aprobado.');
