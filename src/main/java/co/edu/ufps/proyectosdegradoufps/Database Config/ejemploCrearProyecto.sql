
-- 1. Crear proyecto
INSERT INTO public.proyectos (
    id, codigo_proyecto, director_profesor_cedula, modalidad_id, linea_investigacion_id,
    titulo, descripcion, objetivo_general, estado_id,
    fecha_presentacion, porcentaje_avance, fecha_ultima_actualizacion
) VALUES (
    1,
    'PROY-2024-001',
    '2000000751', -- Prof. Roberto Villamizar
    1, -- MONOGRAFIA
    57, -- IA para Detección de Enfermedades en Cultivos
    'Detección Automática de Plagas en Cultivos de Tomate Mediante Visión por Computador',
    'Desarrollo de un sistema de clasificación de imágenes usando redes neuronales convolucionales para identificar plagas en cultivos de tomate en la región de Cúcuta.',
    'Implementar un sistema de detección temprana de plagas utilizando técnicas de deep learning que permita a los agricultores tomar decisiones preventivas.',
    20, -- RECHAZADO
    '2024-03-15',
    35,
    '2024-08-20 15:30:00'
);

-- 2. Vincular 2 estudiantes
INSERT INTO public.estudiantes_proyecto (proyecto_id, estudiante_cedula, fecha_vinculacion, activo) VALUES
(1, '1000002101', '2024-03-15 10:00:00', true), -- Carlos Martínez
(1, '1000002102', '2024-03-15 10:00:00', true); -- María López

-- 3. HISTORIAL COMPLETO del proyecto rechazado

-- Registro inicial
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 1, 'Estudiantes Carlos Martínez y María López registran propuesta de monografía sobre detección de plagas con IA', '2024-03-15 10:30:00', '1000002101');

-- Asignación de director
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 2, 'Se asigna como director al profesor Roberto Villamizar, experto en Ingeniería y Tecnología', '2024-03-16 14:00:00', '2000000751');

-- Inicio revisión de formato
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 8, 'Coordinación de Ingeniería de Sistemas inicia revisión de requisitos mínimos y formato del documento', '2024-03-20 09:00:00', '2000000751');

-- Formato aprobado
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 11, 'Formato aprobado. Cumple con normas APA 7ma edición y estructura requerida', '2024-03-25 11:00:00', '2000000751');

-- Asignación de evaluadores
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 12, 'Comité de Ingeniería de Sistemas asigna evaluadores: Dra. Patricia Durán y Dr. Fernando Acevedo', '2024-04-01 10:00:00', '2000000751');

-- Inicio revisión de contenido
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 13, 'Evaluadores inician análisis de contenido técnico y metodológico', '2024-04-05 08:00:00', '2000000752');

-- Observaciones de contenido
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 14, 'Evaluadores devuelven proyecto con observaciones críticas: metodología incompleta, ausencia de validación estadística, dataset insuficiente', '2024-04-22 16:00:00', '2000000752');

-- Correcciones enviadas
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 15, 'Estudiantes reenvían propuesta con correcciones: se amplía metodología y se incluye análisis de metricas', '2024-05-10 14:30:00', '1000002101');

-- Segunda revisión - siguen problemas
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 14, 'Evaluadores mantienen observaciones: el dataset propuesto sigue siendo insuficiente (solo 200 imágenes), no hay plan de aumento de datos', '2024-05-28 10:00:00', '2000000755');

-- Envío a comité
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 17, 'Proyecto enviado al Comité Curricular de Ingeniería de Sistemas con concepto negativo de evaluadores', '2024-06-15 09:00:00', '2000000751');

-- Acta de comité - rechazo
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 20, 'Acta No. 015-2024: Comité rechaza proyecto por no cumplir con rigor científico requerido en metodología y validación', '2024-08-05 15:00:00', '2000000751');

-- Rechazo final
INSERT INTO public.historiales (proyecto_id, tipo_evento_id, descripcion, fecha_evento, usuario_responsable_cedula) VALUES
(1, 22, 'Proyecto rechazado definitivamente por el Comité. Se recomienda reformular completamente la metodología', '2024-08-20 15:30:00', '2000000751');

-- 4. ACTA DE COMITÉ - RECHAZO
INSERT INTO public.actas_comite (comite_id, proyecto_id, tipo_acta, numero_acta, fecha_acta, concepto, observaciones) VALUES
(3, -- COM-SIST (Ingeniería de Sistemas)
 1,
 'EVALUACION_ANTEPROYECTO',
 '015-2024',
 '2024-08-05',
 'RECHAZADO',
 'El comité curricular, después de analizar los conceptos de los evaluadores Dra. Patricia Durán y Dr. Fernando Acevedo, resuelve RECHAZAR el anteproyecto por las siguientes razones: 1) Metodología insuficiente para garantizar validez científica. 2) Dataset propuesto no cumple con estándares mínimos (se requieren al menos 5000 imágenes etiquetadas). 3) Ausencia de métricas de evaluación apropiadas (precisión, recall, F1-score). 4) No se contempla validación cruzada ni conjunto de prueba independiente. Se recomienda a los estudiantes reformular completamente el proyecto o seleccionar una nueva temática.');

-- 5. EVALUADORES ASIGNADOS
INSERT INTO public.evaluadores_asignados (acta_nombramiento_id, profesor_cedula, fecha_entrega_evaluacion) VALUES
(1, '2000000752', '2024-04-22'), -- Dra. Patricia Durán
(1, '2000000755', '2024-04-22'); -- Dr. Fernando Acevedo

-- 6. REUNIONES DE SEGUIMIENTO (intentos de mejorar)
INSERT INTO public.reuniones (proyecto_id, fecha_reunion, duracion_minutos, tipo, temas_tratados, acuerdos, proxima_reunion, asistio_estudiante, observaciones) VALUES
(1, '2024-03-18 10:00:00', 60, 'PRESENCIAL', 
 'Definición de alcance del proyecto, revisión de dataset disponible, metodología propuesta',
 'Los estudiantes presentarán un dataset preliminar de 500 imágenes en 2 semanas. El director verificará la calidad de las etiquetas',
 '2024-04-02',
 true,
 'Reunión inicial muy productiva. Los estudiantes muestran entusiasmo pero carecen de experiencia en proyectos de IA'),

(1, '2024-04-02 14:00:00', 45, 'VIRTUAL',
 'Revisión del dataset preliminar, discusión sobre arquitectura CNN a utilizar',
 'Se decide usar ResNet50 como arquitectura base. Los estudiantes investigarán sobre transfer learning',
 '2024-04-16',
 true,
 'El dataset presentado tiene problemas de calidad en el etiquetado. Se observa que muchas imágenes están mal clasificadas'),

(1, '2024-04-16 15:30:00', 90, 'PRESENCIAL',
 'Análisis de observaciones de evaluadores, estrategia para correcciones',
 'Replantear metodología: incluir data augmentation, validación cruzada, métricas estándar. Ampliar dataset a 2000 imágenes',
 '2024-05-07',
 true,
 'Los estudiantes se muestran preocupados por el tiempo requerido. El director insiste en la importancia del rigor metodológico'),

(1, '2024-05-07 10:00:00', 75, 'PRESENCIAL',
 'Revisión de correcciones antes de reenviar a evaluadores',
 'Documento mejorado significativamente pero el dataset sigue siendo limitado. Se decide enviar así por presión de tiempos',
 '2024-05-20',
 true,
 'Director expresa dudas sobre la viabilidad del proyecto en el tiempo restante'),

(1, '2024-06-10 16:00:00', 50, 'VIRTUAL',
 'Análisis de conceptos negativos de evaluadores, opciones disponibles',
 'Director recomienda reformular completamente el proyecto o cambiar de tema. Los estudiantes deciden continuar',
 null,
 true,
 'Última reunión antes del comité. Ambiente tenso. Los estudiantes esperan que el comité les dé una oportunidad');

-- 7. NOTIFICACIONES
INSERT INTO public.notificaciones (usuario_cedula, titulo, mensaje, enlace, leida, fecha_creacion) VALUES
('1000002101', '❌ Proyecto Rechazado', 'Tu proyecto "Detección de Plagas con IA" ha sido rechazado por el Comité Curricular. Revisa el acta 015-2024 para conocer las razones.', '/proyectos/1/actas', true, '2024-08-20 16:00:00'),
('1000002102', '❌ Proyecto Rechazado', 'Tu proyecto "Detección de Plagas con IA" ha sido rechazado por el Comité Curricular. Revisa el acta 015-2024 para conocer las razones.', '/proyectos/1/actas', true, '2024-08-20 16:00:00'),
('2000000751', 'Proyecto Rechazado - Acción Requerida', 'El proyecto PROY-2024-001 que diriges ha sido rechazado. Debes reunirte con los estudiantes para orientarlos.', '/proyectos/1', true, '2024-08-20 16:05:00');