
-- =====================================================
-- TABLA: ESTADOS_PROYECTO
-- =====================================================

INSERT INTO estados_proyecto (estado, fase, descripcion, orden) VALUES
('REGISTRADO', 'FORMULACION', 'Proyecto registrado por el estudiante, pendiente de revisión', 1),
('EN_REVISION_FORMATO', 'FORMULACION', 'En revisión de formato y requisitos mínimos', 2),
('CON_CORRECCIONES_FORMATO', 'FORMULACION', 'Devuelto con correcciones de formato al estudiante', 3),

('EN_REVISION_CONTENIDO', 'EVALUACION', 'En evaluación de contenido por evaluadores', 4),
('CON_CORRECCIONES_CONTENIDO', 'EVALUACION', 'Devuelto con observaciones de contenido', 5),
('EN_REVISION_COMITE', 'EVALUACION', 'En revisión por el Comité Curricular', 6),
('CON_OBSERVACIONES_COMITE', 'EVALUACION', 'Devuelto con observaciones del comité', 7),
('APROBADO_INICIO', 'APROBACION', 'Aprobado para iniciar desarrollo del proyecto', 8),

('EN_DESARROLLO', 'EJECUCION', 'Proyecto en etapa de desarrollo y ejecución', 9),
('PRORROGA_SOLICITADA', 'EJECUCION', 'Solicitud de prórroga en evaluación', 10),
('INFORME_PARCIAL_PRESENTADO', 'EJECUCION', 'Informe parcial de avance presentado', 11),
('INFORME_FINAL_PRESENTADO', 'EJECUCION', 'Informe final de resultados presentado', 12),

('EN_REVISION_FINAL', 'CIERRE', 'En revisión de documentación final', 13),
('SUSTENTACION_PROGRAMADA', 'CIERRE', 'Fecha de sustentación asignada', 14),
('EN_SUSTENTACION', 'CIERRE', 'Proyecto en proceso de sustentación', 15),
('CERRADO', 'CIERRE', 'Proyecto cerrado, pendiente de calificación final', 16),

('FINALIZADO', 'FINALIZACION', 'Proyecto finalizado con calificación registrada', 17),
('ARCHIVADO', 'FINALIZACION', 'Proyecto archivado históricamente', 18),
('CANCELADO', 'FINALIZACION', 'Proyecto cancelado por el estudiante', 19),
('RECHAZADO', 'FINALIZACION', 'Proyecto rechazado por el comité', 20);

-- =====================================================
-- TABLA: TIPOS_EVENTO_HISTORIAL
-- =====================================================

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('REGISTRO_INICIAL', 'Proyecto Registrado', 'REGISTRO', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'REGISTRADO'), 
    'Estudiante registra propuesta inicial del proyecto', 1);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('ASIGNACION_DIRECTOR', 'Director Asignado', 'GESTION', false, NULL, 
    'Se asigna director interno o externo al proyecto', 2),
('CAMBIO_DIRECTOR', 'Cambio de Director', 'GESTION', false, NULL, 
    'Se cambia el director del proyecto', 3),
('VINCULAR_ESTUDIANTE', 'Estudiante Vinculado', 'GESTION', false, NULL, 
    'Se vincula un estudiante adicional al proyecto', 4),
('RETIRAR_ESTUDIANTE', 'Estudiante Retirado', 'GESTION', false, NULL, 
    'Se retira un estudiante del proyecto', 5),
('ACTUALIZACION_AVANCE', 'Porcentaje de Avance Actualizado', 'GESTION', false, NULL, 
    'Se actualiza el porcentaje de avance del proyecto', 6),
('DOCUMENTO_ACTUALIZADO', 'Documento Actualizado', 'GESTION', false, NULL, 
    'Se actualiza documento del proyecto (propuesta o final)', 7);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('INICIO_REVISION_FORMATO', 'Inicio de Revisión de Formato', 'REVISION_FORMATO', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_REVISION_FORMATO'), 
    'Coordinación inicia revisión de requisitos mínimos', 8),
('OBSERVACIONES_FORMATO', 'Observaciones de Formato', 'REVISION_FORMATO', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'CON_CORRECCIONES_FORMATO'), 
    'Proyecto devuelto con observaciones de formato', 9),
('CORRECCIONES_FORMATO_ENVIADAS', 'Correcciones de Formato Enviadas', 'REVISION_FORMATO', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_REVISION_FORMATO'), 
    'Estudiante envía correcciones de formato', 10),
('FORMATO_APROBADO', 'Formato Aprobado', 'REVISION_FORMATO', false, NULL, 
    'Coordinación aprueba formato del proyecto', 11);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('ASIGNACION_EVALUADORES', 'Evaluadores Asignados', 'EVALUACION_CONTENIDO', false, NULL, 
    'Comité asigna evaluadores al proyecto', 12),
('INICIO_REVISION_CONTENIDO', 'Inicio de Revisión de Contenido', 'EVALUACION_CONTENIDO', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_REVISION_CONTENIDO'), 
    'Evaluadores inician análisis de contenido', 13),
('OBSERVACIONES_CONTENIDO', 'Observaciones de Contenido', 'EVALUACION_CONTENIDO', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'CON_CORRECCIONES_CONTENIDO'), 
    'Proyecto devuelto con observaciones de contenido', 14),
('CORRECCIONES_CONTENIDO_ENVIADAS', 'Correcciones de Contenido Enviadas', 'EVALUACION_CONTENIDO', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_REVISION_CONTENIDO'), 
    'Estudiante envía correcciones de contenido', 15),
('CONTENIDO_APROBADO', 'Contenido Aprobado', 'EVALUACION_CONTENIDO', false, NULL, 
    'Evaluadores aprueban contenido del proyecto', 16);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('ENVIO_A_COMITE', 'Enviado a Comité', 'COMITE', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_REVISION_COMITE'), 
    'Proyecto enviado al Comité Curricular', 17),
('OBSERVACIONES_COMITE', 'Observaciones del Comité', 'COMITE', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'CON_OBSERVACIONES_COMITE'), 
    'Comité devuelve proyecto con observaciones', 18),
('CORRECCIONES_COMITE_ENVIADAS', 'Correcciones de Comité Enviadas', 'COMITE', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_REVISION_COMITE'), 
    'Estudiante reenvía proyecto con correcciones al comité', 19),
('ACTA_COMITE_GENERADA', 'Acta de Comité Generada', 'COMITE', false, NULL, 
    'Se genera acta de reunión del comité', 20);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('APROBACION_COMITE', 'Proyecto Aprobado por Comité', 'APROBACION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'APROBADO_INICIO'), 
    'Comité aprueba proyecto para iniciar desarrollo', 21),
('RECHAZO_COMITE', 'Proyecto Rechazado por Comité', 'APROBACION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'RECHAZADO'), 
    'Comité rechaza proyecto definitivamente', 22);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('INICIO_DESARROLLO', 'Inicio de Desarrollo', 'EJECUCION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_DESARROLLO'), 
    'Proyecto inicia etapa de desarrollo', 23),
('REUNION_SEGUIMIENTO', 'Reunión de Seguimiento Realizada', 'EJECUCION', false, NULL, 
    'Director y estudiante realizan reunión de seguimiento', 24),
('SOLICITUD_PRORROGA', 'Solicitud de Prórroga', 'EJECUCION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'PRORROGA_SOLICITADA'), 
    'Estudiante solicita prórroga de tiempo', 25),
('PRORROGA_APROBADA', 'Prórroga Aprobada', 'EJECUCION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_DESARROLLO'), 
    'Comité aprueba solicitud de prórroga', 26),
('PRORROGA_RECHAZADA', 'Prórroga Rechazada', 'EJECUCION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_DESARROLLO'), 
    'Comité rechaza solicitud de prórroga', 27);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('ENTREGA_INFORME_PARCIAL', 'Informe Parcial Entregado', 'INFORME', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'INFORME_PARCIAL_PRESENTADO'), 
    'Estudiante entrega informe de avance parcial', 28),
('APROBACION_INFORME_PARCIAL', 'Informe Parcial Aprobado', 'INFORME', false, NULL, 
    'Director aprueba informe parcial', 29),
('OBSERVACIONES_INFORME_PARCIAL', 'Observaciones en Informe Parcial', 'INFORME', false, NULL, 
    'Director solicita correcciones en informe parcial', 30),
('ENTREGA_INFORME_FINAL', 'Informe Final Entregado', 'INFORME', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'INFORME_FINAL_PRESENTADO'), 
    'Estudiante entrega informe final de resultados', 31),
('APROBACION_INFORME_FINAL', 'Informe Final Aprobado', 'INFORME', false, NULL, 
    'Director aprueba informe final', 32);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('VERIFICACION_DOCUMENTACION', 'Verificación de Documentación', 'SUSTENTACION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_REVISION_FINAL'), 
    'Se verifica documentación para sustentación', 33),
('ASIGNACION_JURADOS', 'Jurados Asignados', 'SUSTENTACION', false, NULL, 
    'Se asignan jurados para sustentación', 34),
('PROGRAMACION_SUSTENTACION', 'Sustentación Programada', 'SUSTENTACION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'SUSTENTACION_PROGRAMADA'), 
    'Se asigna fecha y hora de sustentación', 35),
('INICIO_SUSTENTACION', 'Inicio de Sustentación', 'SUSTENTACION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'EN_SUSTENTACION'), 
    'Inicia proceso de sustentación ante jurados', 36),
('CALIFICACION_JURADOS', 'Calificaciones de Jurados Registradas', 'SUSTENTACION', false, NULL, 
    'Se registran calificaciones individuales de jurados', 37),
('FIN_SUSTENTACION', 'Sustentación Finalizada', 'SUSTENTACION', false, NULL, 
    'Finaliza proceso de sustentación', 38);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('CIERRE_PROYECTO', 'Proyecto Cerrado', 'FINALIZACION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'CERRADO'), 
    'Proyecto cerrado, pendiente de calificación', 39),
('REGISTRO_NOTA_FINAL', 'Nota Final Registrada', 'FINALIZACION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'FINALIZADO'), 
    'Se registra calificación final del proyecto', 40),
('ASIGNACION_MENCION', 'Mención Honorífica Asignada', 'FINALIZACION', false, NULL, 
    'Se asigna mención Meritoria o Laureada', 41),
('PROYECTO_ARCHIVADO', 'Proyecto Archivado', 'FINALIZACION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'ARCHIVADO'), 
    'Proyecto archivado históricamente', 42),
('PROYECTO_CANCELADO', 'Proyecto Cancelado', 'FINALIZACION', true, 
    (SELECT id FROM estados_proyecto WHERE estado = 'CANCELADO'), 
    'Estudiante cancela el proyecto', 43);

INSERT INTO tipos_evento_historial (evento, nombre, categoria, cambia_estado, estado_resultante_id, descripcion, orden) VALUES
('NOTIFICACION_ENVIADA', 'Notificación Enviada', 'NOTIFICACION', false, NULL, 
    'Se envía notificación al usuario', 44);

-- Verificar que todos los eventos con cambio de estado tienen un estado válido
DO $$
DECLARE
    eventos_invalidos INTEGER;
BEGIN
    SELECT COUNT(*) INTO eventos_invalidos
    FROM tipos_evento_historial
    WHERE cambia_estado = true 
      AND estado_resultante_id IS NULL;
    
    IF eventos_invalidos > 0 THEN
        RAISE EXCEPTION 'Existen % eventos con cambia_estado=true pero sin estado_resultante_id', eventos_invalidos;
    END IF;
    
    RAISE NOTICE 'Verificación exitosa: Todos los eventos que cambian estado tienen un estado resultante válido';
END $$;

-- =====================================================