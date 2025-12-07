package co.edu.ufps.proyectosdegradoufps.notificaciones.services;

import co.edu.ufps.proyectosdegradoufps.historial.models.Historial;
import co.edu.ufps.proyectosdegradoufps.historial.models.TipoEventoHistorial;
import co.edu.ufps.proyectosdegradoufps.historial.repositories.HistorialRepository;
import co.edu.ufps.proyectosdegradoufps.historial.repositories.TipoEventoHistorialRepository;
import co.edu.ufps.proyectosdegradoufps.notificaciones.dtos.InvitacionRequest;
import co.edu.ufps.proyectosdegradoufps.notificaciones.dtos.NotificacionDTO;
import co.edu.ufps.proyectosdegradoufps.notificaciones.dtos.EstudianteDTO;
import co.edu.ufps.proyectosdegradoufps.notificaciones.models.Notificacion;
import co.edu.ufps.proyectosdegradoufps.notificaciones.repositories.NotificacionRepository;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.EstudianteProyecto;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.Proyecto;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.EstudianteProyectoRepository;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.ProyectoRepository;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.Estudiante;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.EstudianteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        notificacion.setFechaCreacion(LocalDateTime.now());
        
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
            
            // 6. Crear registro en estudiantes_proyecto
            EstudianteProyecto ep = new EstudianteProyecto();
            ep.setProyecto(proyecto);
            ep.setEstudiante(estudiante);
            ep.setActivo(true);
            estudianteProyectoRepository.save(ep);
            
            // 7. Registrar en historial
            TipoEventoHistorial tipoEvento = tipoEventoHistorialRepository.findById(23)
                    .orElse(null); // ESTUDIANTE_AGREGADO
            
            Historial historial = new Historial();
            historial.setProyecto(proyecto);
            historial.setTipoEvento(tipoEvento);
            historial.setDescripcion("Se unió el estudiante " + estudiante.getNombres() + " " + 
                    estudiante.getApellidos() + " al equipo de trabajo");
            historial.setFechaEvento(LocalDateTime.now());
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
            notifInvitante.setFechaCreacion(LocalDateTime.now());
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
            notifInvitante.setFechaCreacion(LocalDateTime.now());
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
        notificacion.setFechaLectura(LocalDateTime.now());
        
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
        notificacion.setFechaLectura(LocalDateTime.now());
        
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
}
