package co.edu.ufps.proyectosdegradoufps.notificaciones.services;

import co.edu.ufps.proyectosdegradoufps.historial.models.Historial;
import co.edu.ufps.proyectosdegradoufps.historial.models.TipoEventoHistorial;
import co.edu.ufps.proyectosdegradoufps.historial.repositories.HistorialRepository;
import co.edu.ufps.proyectosdegradoufps.historial.repositories.TipoEventoHistorialRepository;
import co.edu.ufps.proyectosdegradoufps.notificaciones.dtos.*;
import co.edu.ufps.proyectosdegradoufps.notificaciones.models.Notificacion;
import co.edu.ufps.proyectosdegradoufps.notificaciones.repositories.NotificacionRepository;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.EstudianteProyecto;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.Proyecto;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.EstudianteProyectoRepository;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.ProyectoRepository;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.*;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificacionService {
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    @Autowired
    private ProyectoRepository proyectoRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private EstudianteProyectoRepository estudianteProyectoRepository;
    
    @Autowired
    private HistorialRepository historialRepository;
    
    @Autowired
    private TipoEventoHistorialRepository tipoEventoHistorialRepository;
    
    @Autowired
    private ProfesorRepository profesorRepository;
    
    @Autowired
    private DirectorExternoRepository directorExternoRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // ========================================
    // OBTENER NOTIFICACIONES
    // ========================================
    
    public List<NotificacionDTO> obtenerNotificacionesUsuario(String cedula) {
        return notificacionRepository.findByUsuarioCedulaOrderByFechaCreacionDesc(cedula)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<NotificacionDTO> obtenerNotificacionesNoLeidas(String cedula) {
        return notificacionRepository.findByUsuarioCedulaAndLeidaOrderByFechaCreacionDesc(cedula, false)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public Long contarNotificacionesNoLeidas(String cedula) {
        return notificacionRepository.countByUsuarioCedulaAndLeida(cedula, false);
    }
    
    // ========================================
    // BUSCAR ESTUDIANTE
    // ========================================
    
    public EstudianteDTO buscarEstudiante(String cedula) {
        Estudiante estudiante = estudianteRepository.findById(cedula)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        
        // Verificar que no tenga proyecto activo
        Optional<EstudianteProyecto> proyectoActivo = estudianteProyectoRepository
                .findByEstudianteCedulaAndActivoTrue(cedula);
        if (proyectoActivo.isPresent()) {
            throw new RuntimeException("El estudiante ya tiene un proyecto activo");
        }
        
        // Verificar que no tenga invitación pendiente
        List<Notificacion> invitacionesPendientes = notificacionRepository
                .findByUsuarioCedulaAndTipoOrderByFechaCreacionDesc(cedula, "INVITACION_PROYECTO")
                .stream()
                .filter(n -> !n.getLeida())
                .collect(Collectors.toList());
        
        if (!invitacionesPendientes.isEmpty()) {
            throw new RuntimeException("El estudiante ya tiene una invitación pendiente");
        }
        
        // Crear DTO con información del estudiante
        EstudianteDTO dto = new EstudianteDTO();
        dto.setCedula(estudiante.getCedula());
        dto.setNombre(estudiante.getNombres() + " " + estudiante.getApellidos());
        dto.setCodigo(estudiante.getCodigoEstudiantil());
        dto.setProgramaId(estudiante.getProgramaCodigo());
        
        return dto;
    }
    
    // ========================================
    // BUSCAR ESTUDIANTE POR CÓDIGO
    // ========================================
    
    public EstudianteDTO buscarEstudiantePorCodigo(String codigo) {
        Estudiante estudiante = estudianteRepository.findByCodigoEstudiantil(codigo);
        if (estudiante == null) {
            throw new RuntimeException("Estudiante no encontrado");
        }
        
        // Verificar que no tenga proyecto activo
        Optional<EstudianteProyecto> proyectoActivo = estudianteProyectoRepository
                .findByEstudianteCedulaAndActivoTrue(estudiante.getCedula());
        if (proyectoActivo.isPresent()) {
            throw new RuntimeException("El estudiante ya tiene un proyecto activo");
        }
        
        // Verificar que no tenga invitación pendiente
        List<Notificacion> invitacionesPendientes = notificacionRepository
                .findByUsuarioCedulaAndTipoOrderByFechaCreacionDesc(estudiante.getCedula(), "INVITACION_PROYECTO")
                .stream()
                .filter(n -> !n.getLeida())
                .collect(Collectors.toList());
        
        if (!invitacionesPendientes.isEmpty()) {
            throw new RuntimeException("El estudiante ya tiene una invitación pendiente");
        }
        
        // Crear DTO con información del estudiante
        EstudianteDTO dto = new EstudianteDTO();
        dto.setCedula(estudiante.getCedula());
        dto.setNombre(estudiante.getNombres() + " " + estudiante.getApellidos());
        dto.setCodigo(estudiante.getCodigoEstudiantil());
        dto.setProgramaId(estudiante.getProgramaCodigo());
        
        return dto;
    }
    
    // ========================================
    // ENVIAR INVITACIÓN
    // ========================================
    
    public NotificacionDTO enviarInvitacion(InvitacionRequest request) {
        // 1. Validar que el proyecto existe
        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        // 2. Validar que el estudiante invitado existe
        Estudiante invitado = estudianteRepository.findById(request.getEstudianteInvitadoCedula())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        
        // 3. Validar que el invitado no tenga proyecto activo
        Optional<EstudianteProyecto> proyectoActivo = estudianteProyectoRepository
                .findByEstudianteCedulaAndActivoTrue(invitado.getCedula());
        if (proyectoActivo.isPresent()) {
            throw new RuntimeException("El estudiante ya tiene un proyecto activo");
        }
        
        // 4. Validar que el proyecto no tenga más de 3 integrantes
        List<EstudianteProyecto> integrantes = estudianteProyectoRepository.findByProyectoId(proyecto.getId());
        long integrantesActivos = integrantes.stream().filter(EstudianteProyecto::getActivo).count();
        if (integrantesActivos >= 3) {
            throw new RuntimeException("El proyecto ya tiene el máximo de integrantes (3)");
        }
        
        // 5. Validar que no haya invitación pendiente
        Optional<Notificacion> invitacionExistente = notificacionRepository
                .findInvitacionPendiente(invitado.getCedula(), String.valueOf(proyecto.getId()));
        if (invitacionExistente.isPresent()) {
            throw new RuntimeException("Ya existe una invitación pendiente para este proyecto");
        }
        
        // 6. Crear notificación
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioCedula(invitado.getCedula());
        notificacion.setTipo("INVITACION_PROYECTO");
        notificacion.setTitulo("Invitación a proyecto de grado");
        notificacion.setMensaje(request.getInvitanteNombre() + 
                " te ha invitado a unirte a su proyecto: " + request.getTituloProyecto());
        notificacion.setEnlace("/proyectos/" + proyecto.getId());
        notificacion.setLeida(false);
        notificacion.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        
        // 7. Crear metadata JSON
        try {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("proyectoId", proyecto.getId());
            metadata.put("tituloProyecto", request.getTituloProyecto());
            metadata.put("invitanteCedula", request.getInvitanteCedula());
            metadata.put("invitanteNombre", request.getInvitanteNombre());
            metadata.put("estado", "PENDIENTE");
            notificacion.setMetadata(objectMapper.writeValueAsString(metadata));
        } catch (Exception e) {
            throw new RuntimeException("Error al crear metadata de invitación", e);
        }
        
        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);
        return convertirADTO(notificacionGuardada);
    }
    
    // ========================================
    // RESPONDER INVITACIÓN
    // ========================================
    
    public NotificacionDTO responderInvitacion(Integer notificacionId, String respuesta) {
        // 1. Obtener notificación
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        
        // 2. Validar que sea una invitación
        if (!"INVITACION_PROYECTO".equals(notificacion.getTipo())) {
            throw new RuntimeException("Esta notificación no es una invitación");
        }
        
        // 3. Parsear metadata
        Map<String, Object> metadata;
        try {
            metadata = objectMapper.readValue(notificacion.getMetadata(), 
                    new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error al leer metadata de invitación", e);
        }
        
        Integer proyectoId = (Integer) metadata.get("proyectoId");
        String invitanteCedula = (String) metadata.get("invitanteCedula");
        
        if ("ACEPTADA".equals(respuesta)) {
            // 4. Validar nuevamente que el estudiante no tenga proyecto activo
            Optional<EstudianteProyecto> proyectoActivo = estudianteProyectoRepository
                    .findByEstudianteCedulaAndActivoTrue(notificacion.getUsuarioCedula());
            if (proyectoActivo.isPresent()) {
                throw new RuntimeException("Ya tienes un proyecto activo");
            }
            
            // 5. Obtener proyecto y estudiante
            Proyecto proyecto = proyectoRepository.findById(proyectoId)
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
            Estudiante estudiante = estudianteRepository.findById(notificacion.getUsuarioCedula())
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
            
            // 6. Verificar si ya existe un registro (activo o inactivo) - UPSERT
            Optional<EstudianteProyecto> registroExistente = estudianteProyectoRepository
                    .findAll()
                    .stream()
                    .filter(ep -> ep.getProyecto().getId().equals(proyectoId) && 
                                  ep.getEstudiante().getCedula().equals(estudiante.getCedula()))
                    .findFirst();
            
            EstudianteProyecto ep;
            if (registroExistente.isPresent()) {
                // Si existe, reactivarlo
                ep = registroExistente.get();
                ep.setActivo(true);
            } else {
                // Si no existe, crear uno nuevo
                ep = new EstudianteProyecto();
                ep.setProyecto(proyecto);
                ep.setEstudiante(estudiante);
                ep.setActivo(true);
            }
            estudianteProyectoRepository.save(ep);
            
            // 7. Registrar en historial
            TipoEventoHistorial tipoEvento = tipoEventoHistorialRepository.findById(23)
                    .orElse(null); // ESTUDIANTE_AGREGADO
            
            Historial historial = new Historial();
            historial.setProyecto(proyecto);
            historial.setTipoEvento(tipoEvento);
            historial.setDescripcion("Se unió el estudiante " + estudiante.getNombres() + " " + 
                    estudiante.getApellidos() + " al equipo de trabajo");
            historial.setFechaEvento(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            historial.setUsuarioResponsable(estudiante);
            historialRepository.save(historial);
            
            // 8. Notificar al invitante
            Notificacion notifInvitante = new Notificacion();
            notifInvitante.setUsuarioCedula(invitanteCedula);
            notifInvitante.setTipo("GENERAL");
            notifInvitante.setTitulo("Invitación aceptada ✓");
            notifInvitante.setMensaje("Tu compañero ha aceptado unirse al proyecto");
            notifInvitante.setEnlace("/proyectos/" + proyectoId);
            notifInvitante.setLeida(false);
            notifInvitante.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            notificacionRepository.save(notifInvitante);
        } else {
            // Si rechaza, notificar al invitante
            Notificacion notifInvitante = new Notificacion();
            notifInvitante.setUsuarioCedula(invitanteCedula);
            notifInvitante.setTipo("GENERAL");
            notifInvitante.setTitulo("Invitación rechazada");
            notifInvitante.setMensaje("Tu compañero ha rechazado la invitación al proyecto");
            notifInvitante.setEnlace("/proyectos/" + proyectoId);
            notifInvitante.setLeida(false);
            notifInvitante.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            notificacionRepository.save(notifInvitante);
        }
        
        // 9. Actualizar metadata de la notificación original
        metadata.put("estado", respuesta);
        try {
            notificacion.setMetadata(objectMapper.writeValueAsString(metadata));
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar metadata", e);
        }
        
        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        
        Notificacion notificacionActualizada = notificacionRepository.save(notificacion);
        return convertirADTO(notificacionActualizada);
    }
    
    // ========================================
    // MARCAR COMO LEÍDA
    // ========================================
    
    public NotificacionDTO marcarComoLeida(Integer notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        
        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        
        Notificacion notificacionActualizada = notificacionRepository.save(notificacion);
        return convertirADTO(notificacionActualizada);
    }
    
    // ========================================
    // CONVERSIÓN A DTO
    // ========================================
    
    private NotificacionDTO convertirADTO(Notificacion notificacion) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(notificacion.getId());
        dto.setUsuarioCedula(notificacion.getUsuarioCedula());
        dto.setTitulo(notificacion.getTitulo());
        dto.setMensaje(notificacion.getMensaje());
        dto.setEnlace(notificacion.getEnlace());
        dto.setLeida(notificacion.getLeida());
        dto.setFechaCreacion(notificacion.getFechaCreacion());
        dto.setFechaLectura(notificacion.getFechaLectura());
        dto.setTipo(notificacion.getTipo());
        
        // Parsear metadata JSON
        if (notificacion.getMetadata() != null) {
            try {
                Map<String, Object> metadata = objectMapper.readValue(
                        notificacion.getMetadata(), 
                        new TypeReference<Map<String, Object>>() {});
                dto.setMetadata(metadata);
            } catch (Exception e) {
                dto.setMetadata(new HashMap<>());
            }
        }
        
        return dto;
    }
    
    // ========================================
    // BÚSQUEDA DE DIRECTORES
    // ========================================
    
    public List<DirectorDTO> buscarDirectores(String busqueda) {
        List<DirectorDTO> directores = new ArrayList<>();
        
        // Buscar profesores
        List<Profesor> profesores = busqueda == null || busqueda.trim().isEmpty() 
            ? profesorRepository.findAll() 
            : profesorRepository.buscarPorNombreOEspecialidad(busqueda);
        
        for (Profesor profesor : profesores) {
            DirectorDTO dto = new DirectorDTO();
            dto.setCedula(profesor.getCedula());
            dto.setNombre(profesor.getNombres() + " " + profesor.getApellidos());
            dto.setTituloAcademico(profesor.getTituloAcademico());
            dto.setEspecialidad(profesor.getEspecialidad());
            dto.setTipoDirector("PROFESOR");
            dto.setTipoProfesor(profesor.getTipoProfesor() != null ? profesor.getTipoProfesor().name() : null);
            directores.add(dto);
        }
        
        // Buscar directores externos aprobados
        List<DirectorExterno> externos = busqueda == null || busqueda.trim().isEmpty() 
            ? directorExternoRepository.findByAprobadoTrue() 
            : directorExternoRepository.buscarPorNombreOEspecialidad(busqueda);
        
        for (DirectorExterno externo : externos) {
            DirectorDTO dto = new DirectorDTO();
            dto.setCedula(externo.getCedula());
            dto.setNombre(externo.getNombres() + " " + externo.getApellidos());
            dto.setTituloAcademico(externo.getTituloAcademico());
            dto.setEspecialidad(externo.getEspecialidad());
            dto.setTipoDirector("EXTERNO");
            dto.setInstitucion(externo.getInstitucion());
            directores.add(dto);
        }
        
        return directores;
    }
    
    public List<DirectorDTO> listarTodosLosDirectores() {
        return buscarDirectores(null);
    }
    
    // ========================================
    // ENVIAR INVITACIÓN A DIRECTOR
    // ========================================
    
    public NotificacionDTO enviarInvitacionDirector(InvitacionDirectorRequest request) {
        // 1. Validar que el proyecto existe
        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        // 2. Validar que el proyecto no tenga ya un director
        if (proyecto.getDirectorProfesor() != null || proyecto.getDirectorExterno() != null) {
            throw new RuntimeException("El proyecto ya tiene un director asignado");
        }
        
        // 3. Validar que el director existe
        Usuario director;
        String nombreDirector;
        
        if ("PROFESOR".equals(request.getTipoDirector())) {
            Profesor profesor = profesorRepository.findById(request.getDirectorCedula())
                    .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
            director = profesor;
            nombreDirector = profesor.getNombres() + " " + profesor.getApellidos();
        } else if ("EXTERNO".equals(request.getTipoDirector())) {
            DirectorExterno externo = directorExternoRepository.findById(request.getDirectorCedula())
                    .orElseThrow(() -> new RuntimeException("Director externo no encontrado"));
            
            if (!externo.getAprobado()) {
                throw new RuntimeException("El director externo no está aprobado por el comité");
            }
            
            director = externo;
            nombreDirector = externo.getNombres() + " " + externo.getApellidos();
        } else {
            throw new RuntimeException("Tipo de director inválido");
        }
        
        // 4. Validar que no haya invitación pendiente PARA ESTE PROYECTO (buscamos en TODAS las notificaciones de tipo INVITACION_DIRECCION)
        // Obtenemos todas las invitaciones de dirección no leídas de todos los usuarios
        List<Notificacion> todasInvitaciones = notificacionRepository.findAll().stream()
                .filter(n -> "INVITACION_DIRECCION".equals(n.getTipo()))
                .filter(n -> !n.getLeida())
                .collect(Collectors.toList());
        
        List<Notificacion> invitacionesPendientes = todasInvitaciones.stream()
                .filter(n -> {
                    try {
                        Map<String, Object> meta = objectMapper.readValue(
                                n.getMetadata(), new TypeReference<Map<String, Object>>() {});
                        Integer proyectoIdMeta = (Integer) meta.get("proyectoId");
                        String estadoMeta = (String) meta.get("estado");
                        return proyectoIdMeta != null && proyectoIdMeta.equals(proyecto.getId()) && 
                               "PENDIENTE".equals(estadoMeta);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
        
        if (!invitacionesPendientes.isEmpty()) {
            // Obtener nombre del director que tiene la invitación pendiente
            String cedulaDirectorPendiente = invitacionesPendientes.get(0).getUsuarioCedula();
            Usuario directorPendiente = profesorRepository.findById(cedulaDirectorPendiente)
                    .map(p -> (Usuario)p)
                    .orElseGet(() -> directorExternoRepository.findById(cedulaDirectorPendiente).orElse(null));
            
            String nombreDirectorPendiente = directorPendiente != null ? 
                directorPendiente.getNombres() + " " + directorPendiente.getApellidos() : 
                "un director";
            
            throw new RuntimeException("Ya tienes una invitación pendiente con " + nombreDirectorPendiente + 
                ". Espera su respuesta o cancela la invitación desde el dashboard antes de invitar a otro director.");
        }
        
        // 5. Crear notificación
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioCedula(director.getCedula());
        notificacion.setTipo("INVITACION_DIRECCION");
        notificacion.setTitulo("Solicitud de dirección de proyecto");
        notificacion.setMensaje("Has sido invitado a dirigir el proyecto: " + proyecto.getTitulo());
        notificacion.setEnlace("/proyectos/" + proyecto.getId());
        notificacion.setLeida(false);
        notificacion.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        
        // 6. Obtener estudiantes del proyecto
        List<EstudianteProyecto> integrantes = estudianteProyectoRepository.findByProyectoId(proyecto.getId());
        List<String> nombresEstudiantes = integrantes.stream()
                .filter(EstudianteProyecto::getActivo)
                .map(ep -> ep.getEstudiante().getNombres() + " " + ep.getEstudiante().getApellidos())
                .collect(Collectors.toList());
        
        // 7. Metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("proyectoId", proyecto.getId());
        metadata.put("tituloProyecto", proyecto.getTitulo());
        metadata.put("tipoDirector", request.getTipoDirector());
        metadata.put("directorNombre", nombreDirector);
        metadata.put("directorCedula", director.getCedula());
        metadata.put("estudiantes", nombresEstudiantes);
        metadata.put("cantidadEstudiantes", nombresEstudiantes.size());
        metadata.put("cedulaEstudiante", request.getCedulaEstudiante());
        metadata.put("estado", "PENDIENTE");
        
        try {
            notificacion.setMetadata(objectMapper.writeValueAsString(metadata));
        } catch (Exception e) {
            throw new RuntimeException("Error al crear metadata de notificación");
        }
        
        notificacion = notificacionRepository.save(notificacion);
        
        return convertirADTO(notificacion);
    }
    
    // ========================================
    // RESPONDER INVITACIÓN DE DIRECCIÓN
    // ========================================
    
    public NotificacionDTO responderInvitacionDirector(Integer notificacionId, String respuesta) {
        // 1. Obtener notificación
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        
        if (!"INVITACION_DIRECCION".equals(notificacion.getTipo())) {
            throw new RuntimeException("Esta notificación no es una invitación de dirección");
        }
        
        if (notificacion.getLeida()) {
            throw new RuntimeException("Esta invitación ya fue respondida");
        }
        
        // 2. Extraer metadata
        Map<String, Object> metadata;
        try {
            metadata = objectMapper.readValue(
                    notificacion.getMetadata(),
                    new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error al leer metadata de notificación");
        }
        
        Integer proyectoId = (Integer) metadata.get("proyectoId");
        String tipoDirector = (String) metadata.get("tipoDirector");
        String estadoInvitacion = (String) metadata.get("estado");
        
        // Validar que la invitación no esté cancelada
        if ("CANCELADA".equals(estadoInvitacion)) {
            throw new RuntimeException("Esta invitación fue cancelada por el estudiante");
        }
        
        // 3. Obtener proyecto
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        // 4. Procesar respuesta
        if ("ACEPTADA".equals(respuesta)) {
            // Validar que el proyecto siga sin director
            if (proyecto.getDirectorProfesor() != null || proyecto.getDirectorExterno() != null) {
                throw new RuntimeException("El proyecto ya tiene un director asignado");
            }
            
            // Asignar director según tipo - IMPORTANTE: Verificar que el director existe en la tabla correcta
            // Primero intentar buscar como profesor (tiene prioridad)
            Optional<Profesor> profesorOpt = profesorRepository.findById(notificacion.getUsuarioCedula());
            Optional<DirectorExterno> externoOpt = directorExternoRepository.findById(notificacion.getUsuarioCedula());
            
            if (profesorOpt.isPresent()) {
                // Es un profesor, ignorar el tipoDirector del metadata
                proyecto.setDirectorProfesor(profesorOpt.get());
            } else if (externoOpt.isPresent() && externoOpt.get().getAprobado()) {
                // Es un director externo aprobado
                proyecto.setDirectorExterno(externoOpt.get());
            } else {
                throw new RuntimeException("Director no encontrado o no aprobado");
            }
            
            proyecto.setFechaUltimaActualizacion(LocalDateTime.now());
            proyectoRepository.save(proyecto);
            
            // Registrar en historial
            TipoEventoHistorial tipoEvento = tipoEventoHistorialRepository.findByEvento("ASIGNACION_DIRECTOR").orElse(null);
            if (tipoEvento != null) {
                Historial historial = new Historial();
                historial.setProyecto(proyecto);
                historial.setTipoEvento(tipoEvento);
                
                Usuario directorUsuario = null;
                if ("PROFESOR".equals(tipoDirector)) {
                    directorUsuario = profesorRepository.findById(notificacion.getUsuarioCedula()).orElse(null);
                } else {
                    directorUsuario = directorExternoRepository.findById(notificacion.getUsuarioCedula()).orElse(null);
                }
                
                String nombreDirector = directorUsuario != null ? 
                    directorUsuario.getNombres() + " " + directorUsuario.getApellidos() : 
                    notificacion.getUsuarioCedula();
                
                historial.setDescripcion("Director asignado: " + nombreDirector);
                historial.setFechaEvento(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                historial.setUsuarioResponsable(directorUsuario);
                historialRepository.save(historial);
            }
            
            // Notificar a los estudiantes
            List<EstudianteProyecto> integrantes = estudianteProyectoRepository.findByProyectoId(proyecto.getId());
            Usuario director = null;
            if ("PROFESOR".equals(tipoDirector)) {
                director = profesorRepository.findById(notificacion.getUsuarioCedula()).orElse(null);
            } else {
                director = directorExternoRepository.findById(notificacion.getUsuarioCedula()).orElse(null);
            }
            
            if (director != null) {
                String nombreDirector = director.getNombres() + " " + director.getApellidos();
                for (EstudianteProyecto ep : integrantes) {
                    if (ep.getActivo()) {
                        Notificacion notifEstudiante = new Notificacion();
                        notifEstudiante.setUsuarioCedula(ep.getEstudiante().getCedula());
                        notifEstudiante.setTipo("GENERAL");
                        notifEstudiante.setTitulo("Director asignado");
                        notifEstudiante.setMensaje(nombreDirector + " ha aceptado dirigir tu proyecto: " + proyecto.getTitulo());
                        notifEstudiante.setEnlace("/proyectos/" + proyecto.getId());
                        notifEstudiante.setLeida(false);
                        notifEstudiante.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                        notificacionRepository.save(notifEstudiante);
                    }
                }
            }
            
        } else if ("RECHAZADA".equals(respuesta)) {
            // Notificar a los estudiantes del rechazo
            List<EstudianteProyecto> integrantes = estudianteProyectoRepository.findByProyectoId(proyecto.getId());
            Usuario director = null;
            if ("PROFESOR".equals(tipoDirector)) {
                director = profesorRepository.findById(notificacion.getUsuarioCedula()).orElse(null);
            } else {
                director = directorExternoRepository.findById(notificacion.getUsuarioCedula()).orElse(null);
            }
            
            if (director != null) {
                String nombreDirector = director.getNombres() + " " + director.getApellidos();
                for (EstudianteProyecto ep : integrantes) {
                    if (ep.getActivo()) {
                        Notificacion notifEstudiante = new Notificacion();
                        notifEstudiante.setUsuarioCedula(ep.getEstudiante().getCedula());
                        notifEstudiante.setTipo("GENERAL");
                        notifEstudiante.setTitulo("Invitación rechazada");
                        notifEstudiante.setMensaje(nombreDirector + " ha rechazado la invitación para dirigir tu proyecto: " + proyecto.getTitulo());
                        notifEstudiante.setEnlace("/proyectos/" + proyecto.getId());
                        notifEstudiante.setLeida(false);
                        notifEstudiante.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                        notificacionRepository.save(notifEstudiante);
                    }
                }
            }
        }
        
        // 5. Marcar notificación como leída
        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        notificacion = notificacionRepository.save(notificacion);
        
        return convertirADTO(notificacion);
    }
    
    // ========================================
    // OBTENER INVITACIÓN PENDIENTE DE PROYECTO
    // ========================================
    
    public NotificacionDTO obtenerInvitacionPendienteProyecto(Integer proyectoId) {
        // Buscar todas las invitaciones de dirección no leídas
        List<Notificacion> todasInvitaciones = notificacionRepository.findAll().stream()
                .filter(n -> "INVITACION_DIRECCION".equals(n.getTipo()))
                .filter(n -> !n.getLeida())
                .collect(Collectors.toList());
        
        // Buscar la invitación PENDIENTE para este proyecto
        Optional<Notificacion> invitacionPendiente = todasInvitaciones.stream()
                .filter(n -> {
                    try {
                        Map<String, Object> meta = objectMapper.readValue(
                                n.getMetadata(), new TypeReference<Map<String, Object>>() {});
                        Integer proyectoIdMeta = (Integer) meta.get("proyectoId");
                        String estadoMeta = (String) meta.get("estado");
                        return proyectoIdMeta != null && proyectoIdMeta.equals(proyectoId) && 
                               "PENDIENTE".equals(estadoMeta);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst();
        
        return invitacionPendiente.map(this::convertirADTO).orElse(null);
    }
    
    // ========================================
    // CANCELAR INVITACIÓN DE DIRECCIÓN
    // ========================================
    
    public NotificacionDTO cancelarInvitacionDirector(Integer notificacionId) {
        // 1. Obtener notificación
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        
        if (!"INVITACION_DIRECCION".equals(notificacion.getTipo())) {
            throw new RuntimeException("Esta notificación no es una invitación de dirección");
        }
        
        if (notificacion.getLeida()) {
            throw new RuntimeException("Esta invitación ya fue respondida o cancelada");
        }
        
        // 2. Actualizar metadata para marcar como cancelada
        try {
            Map<String, Object> metadata = objectMapper.readValue(
                    notificacion.getMetadata(),
                    new TypeReference<Map<String, Object>>() {});
            metadata.put("estado", "CANCELADA");
            notificacion.setMetadata(objectMapper.writeValueAsString(metadata));
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar metadata de notificación");
        }
        
        // 3. Marcar como leída (para que desaparezca del dropdown del director)
        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        notificacion = notificacionRepository.save(notificacion);
        
        return convertirADTO(notificacion);
    }
}
