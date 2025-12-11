package co.edu.ufps.proyectosdegradoufps.reuniones.services;

import co.edu.ufps.proyectosdegradoufps.historial.models.Historial;
import co.edu.ufps.proyectosdegradoufps.historial.repositories.HistorialRepository;
import co.edu.ufps.proyectosdegradoufps.historial.repositories.TipoEventoHistorialRepository;
import co.edu.ufps.proyectosdegradoufps.notificaciones.models.Notificacion;
import co.edu.ufps.proyectosdegradoufps.notificaciones.repositories.NotificacionRepository;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.Proyecto;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.ProyectoRepository;
import co.edu.ufps.proyectosdegradoufps.reuniones.dtos.*;
import co.edu.ufps.proyectosdegradoufps.reuniones.models.Reunion;
import co.edu.ufps.proyectosdegradoufps.reuniones.repositories.ReunionRepository;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.Usuario;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReunionService {

    @Autowired
    private ReunionRepository reunionRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private TipoEventoHistorialRepository tipoEventoHistorialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Solicitar una reunión (crea la reunión no confirmada y envía notificación)
     */
    @Transactional
    public ReunionDTO solicitarReunion(SolicitarReunionRequest request) {
        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Usuario solicitante = usuarioRepository.findById(request.getSolicitanteCedula())
                .orElseThrow(() -> new RuntimeException("Solicitante no encontrado"));

        Usuario receptor = usuarioRepository.findById(request.getReceptorCedula())
                .orElseThrow(() -> new RuntimeException("Receptor no encontrado"));

        // Crear la reunión (sin confirmar)
        Reunion reunion = new Reunion();
        reunion.setProyecto(proyecto);
        reunion.setFechaReunion(request.getFechaReunion().truncatedTo(ChronoUnit.SECONDS));
        reunion.setDuracionMinutos(request.getDuracionMinutos());
        reunion.setTipo(request.getTipo());
        reunion.setTemasTratados(request.getTemasPropuestos());
        reunion.setAcuerdos(""); // Se llenará después
        // reunion.setSolicitanteCedula(request.getSolicitanteCedula()); // Campo no existe en BD
        // reunion.setConfirmada(false); // Campo no existe en BD
        reunion.setObservaciones(request.getObservaciones());
        reunion.setAsistioEstudiante(true);

        reunion = reunionRepository.save(reunion);

        // Crear notificación para el receptor
        Notificacion notif = new Notificacion();
        notif.setUsuarioCedula(receptor.getCedula());
        notif.setTipo("REUNION");
        notif.setTitulo("Nueva solicitud de reunión");
        notif.setMensaje(String.format("%s %s ha solicitado una reunión para el proyecto '%s'", 
            solicitante.getNombres(), solicitante.getApellidos(), proyecto.getTitulo()));
        notif.setLeida(false);
        // notif.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)); // Ya tiene default

        notificacionRepository.save(notif);

        // Registrar en historial
        Historial historial = new Historial();
        historial.setProyecto(proyecto);
        historial.setTipoEvento(tipoEventoHistorialRepository.findByEvento("REUNION_SEGUIMIENTO")
                .orElseThrow(() -> new RuntimeException("Tipo de evento REUNION_SEGUIMIENTO no encontrado")));
        historial.setDescripcion(String.format("Solicitud de reunión para el %s", 
            request.getFechaReunion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        historial.setUsuarioResponsable(solicitante);
        historial.setFechaEvento(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        historialRepository.save(historial);

        return convertToDTO(reunion);
    }

    /**
     * Responder a una solicitud de reunión (aceptar o rechazar)
     */
    @Transactional
    public ReunionDTO responderSolicitudReunion(Integer reunionId, String respondenteCedula, ResponderReunionRequest request) {
        Reunion reunion = reunionRepository.findById(reunionId)
                .orElseThrow(() -> new RuntimeException("Reunión no encontrada"));

        Usuario respondente = usuarioRepository.findById(respondenteCedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Usuario solicitante = usuarioRepository.findById(reunion.getSolicitanteCedula())
        //         .orElseThrow(() -> new RuntimeException("Solicitante no encontrado"));

        if (request.getAceptada()) {
            // Aceptar la reunión - por ahora solo guardamos
            // reunion.setConfirmada(true); // Campo no existe en BD

            // Notificar al solicitante (usamos el director del proyecto)
            String solicitanteCedula = getReceptorCedula(reunion);
            if (solicitanteCedula == null) {
                throw new RuntimeException("No se pudo determinar el solicitante");
            }
            Usuario solicitante = usuarioRepository.findById(solicitanteCedula)
                    .orElseThrow(() -> new RuntimeException("Solicitante no encontrado"));

            Notificacion notif = new Notificacion();
            notif.setUsuarioCedula(solicitante.getCedula());
            notif.setTipo("REUNION_ACEPTADA");
            notif.setTitulo("Reunión aceptada");
            notif.setMensaje(String.format("%s %s ha aceptado la reunión para el %s", 
                respondente.getNombres(), respondente.getApellidos(),
                reunion.getFechaReunion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            notif.setLeida(false);
            // notif.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            notificacionRepository.save(notif);

            // Registrar en historial
            Historial historial = new Historial();
            historial.setProyecto(reunion.getProyecto());
            historial.setDescripcion(String.format("Reunión confirmada para el %s", 
                reunion.getFechaReunion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            historial.setUsuarioResponsable(respondente);
            historial.setFechaEvento(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            historialRepository.save(historial);

        } else {
            // Rechazar la reunión - eliminar
            // Notificar al solicitante (usamos el director del proyecto)
            String solicitanteCedula = getReceptorCedula(reunion);
            if (solicitanteCedula != null) {
                Usuario solicitante = usuarioRepository.findById(solicitanteCedula).orElse(null);
                if (solicitante != null) {
                    Notificacion notif = new Notificacion();
                    notif.setUsuarioCedula(solicitante.getCedula());
                    notif.setTipo("REUNION_RECHAZADA");
                    notif.setTitulo("Reunión rechazada");
                    notif.setMensaje(String.format("%s %s ha rechazado la reunión. Motivo: %s", 
                        respondente.getNombres(), respondente.getApellidos(),
                        request.getMotivoRechazo() != null ? request.getMotivoRechazo() : "No especificado"));
                    notif.setLeida(false);
                    // notif.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

                    notificacionRepository.save(notif);
                }
            }

            reunionRepository.delete(reunion);
            return null; // Reunión eliminada
        }

        return convertToDTO(reunionRepository.save(reunion));
    }

    /**
     * Registrar los detalles de la reunión después de que se realizó
     */
    @Transactional
    public ReunionDTO registrarDetallesReunion(RegistrarReunionRequest request) {
        Reunion reunion = reunionRepository.findById(request.getReunionId())
                .orElseThrow(() -> new RuntimeException("Reunión no encontrada"));

        reunion.setAcuerdos(request.getAcuerdos());
        reunion.setProximaReunion(request.getProximaReunion());
        
        if (request.getObservaciones() != null) {
            reunion.setObservaciones(request.getObservaciones());
        }

        return convertToDTO(reunionRepository.save(reunion));
    }

    /**
     * Marcar asistencia a una reunión
     */
    @Transactional
    public ReunionDTO marcarAsistencia(Integer reunionId, MarcarAsistenciaRequest request) {
        Reunion reunion = reunionRepository.findById(reunionId)
                .orElseThrow(() -> new RuntimeException("Reunión no encontrada"));

        if (request.getAsistioEstudiante() != null) {
            reunion.setAsistioEstudiante(request.getAsistioEstudiante());
        }

        // Solo marcamos asistencia del estudiante
        // if (request.getAsistioDirector() != null) {
        //     reunion.setAsistioDirector(request.getAsistioDirector());
        // }

        if (request.getObservaciones() != null) {
            reunion.setObservaciones(request.getObservaciones());
        }

        // Registrar en historial si el estudiante no asistió
        if (Boolean.FALSE.equals(request.getAsistioEstudiante())) {
            Historial historial = new Historial();
            historial.setProyecto(reunion.getProyecto());
            
            historial.setDescripcion(String.format("Reunión del %s: El estudiante no asistió", 
                reunion.getFechaReunion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            historial.setFechaEvento(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            historialRepository.save(historial);
        }

        return convertToDTO(reunionRepository.save(reunion));
    }

    /**
     * Obtener todas las reuniones de un proyecto
     */
    public List<ReunionDTO> obtenerReunionesProyecto(Integer proyectoId) {
        return reunionRepository.findByProyectoIdOrderByFechaReunionDesc(proyectoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener reuniones de un director
     */
    public List<ReunionDTO> obtenerReunionesDirector(String cedulaDirector) {
        return reunionRepository.findReunionesDeDirector(cedulaDirector)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener reuniones de un estudiante
     */
    public List<ReunionDTO> obtenerReunionesEstudiante(String cedulaEstudiante) {
        return reunionRepository.findReunionesDeEstudiante(cedulaEstudiante)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cancelar una reunión (solo si no está confirmada o antes de que ocurra)
     */
    @Transactional
    public void cancelarReunion(Integer reunionId, String usuarioCedula) {
        Reunion reunion = reunionRepository.findById(reunionId)
                .orElseThrow(() -> new RuntimeException("Reunión no encontrada"));

        Usuario usuario = usuarioRepository.findById(usuarioCedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Notificar a la otra parte (director o estudiante del proyecto)
        String receptorCedula = getReceptorCedula(reunion);
        if (receptorCedula == null) {
            throw new RuntimeException("No se pudo determinar el receptor");
        }

        Usuario receptor = usuarioRepository.findById(receptorCedula)
                .orElseThrow(() -> new RuntimeException("Receptor no encontrado"));

        Notificacion notif = new Notificacion();
        notif.setUsuarioCedula(receptor.getCedula());
        notif.setTipo("REUNION_CANCELADA");
        notif.setTitulo("Reunión cancelada");
        notif.setMensaje(String.format("%s %s ha cancelado la reunión programada para el %s", 
            usuario.getNombres(), usuario.getApellidos(),
            reunion.getFechaReunion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        notif.setLeida(false);
        // notif.setFechaCreacion(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        notificacionRepository.save(notif);

        // Registrar en historial
        Historial historial = new Historial();
        historial.setProyecto(reunion.getProyecto());
        historial.setDescripcion(String.format("Reunión cancelada (programada para el %s)", 
            reunion.getFechaReunion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        historial.setUsuarioResponsable(usuario);
        historial.setFechaEvento(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        historialRepository.save(historial);

        // Eliminar la reunión
        reunionRepository.delete(reunion);
    }

    // Helper para obtener la cédula del receptor
    private String getReceptorCedula(Reunion reunion) {
        Proyecto proyecto = reunion.getProyecto();
        if (proyecto.getDirectorProfesor() != null) {
            return proyecto.getDirectorProfesor().getCedula();
        } else if (proyecto.getDirectorExterno() != null) {
            return proyecto.getDirectorExterno().getCedula();
        }
        return null;
    }

    // Convertir Entity a DTO
    private ReunionDTO convertToDTO(Reunion reunion) {
        ReunionDTO dto = new ReunionDTO();
        dto.setId(reunion.getId());
        dto.setProyectoId(reunion.getProyecto().getId());
        dto.setProyectoTitulo(reunion.getProyecto().getTitulo());
        dto.setFechaReunion(reunion.getFechaReunion());
        dto.setDuracionMinutos(reunion.getDuracionMinutos());
        dto.setTipo(reunion.getTipo());
        dto.setTemasTratados(reunion.getTemasTratados());
        dto.setAcuerdos(reunion.getAcuerdos());
        dto.setProximaReunion(reunion.getProximaReunion());
        dto.setAsistioEstudiante(reunion.getAsistioEstudiante());
        // dto.setAsistioDirector(reunion.getAsistioDirector()); // Campo removido
        dto.setObservaciones(reunion.getObservaciones());
        // Campos que no existen en BD:
        // dto.setConfirmada(reunion.getConfirmada());
        // dto.setSolicitanteCedula(reunion.getSolicitanteCedula());
        // dto.setFechaCreacion(reunion.getFechaCreacion());

        // Nombre del solicitante - no disponible sin campo solicitanteCedula
        // usuarioRepository.findById(reunion.getSolicitanteCedula())
        //         .ifPresent(u -> dto.setSolicitanteNombre(u.getNombres() + " " + u.getApellidos()));

        return dto;
    }
}
