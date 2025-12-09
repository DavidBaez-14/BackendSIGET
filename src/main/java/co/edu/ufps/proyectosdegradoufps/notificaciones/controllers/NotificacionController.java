package co.edu.ufps.proyectosdegradoufps.notificaciones.controllers;

import co.edu.ufps.proyectosdegradoufps.notificaciones.dtos.*;
import co.edu.ufps.proyectosdegradoufps.notificaciones.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {
    
    @Autowired
    private NotificacionService notificacionService;
    
    /**
     * Obtener todas las notificaciones de un usuario
     */
    @GetMapping("/usuario/{cedula}")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificaciones(@PathVariable String cedula) {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesUsuario(cedula));
    }
    
    /**
     * Obtener solo notificaciones no leídas
     */
    @GetMapping("/usuario/{cedula}/pendientes")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificacionesPendientes(@PathVariable String cedula) {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesNoLeidas(cedula));
    }
    
    /**
     * Contar notificaciones no leídas
     */
    @GetMapping("/usuario/{cedula}/contador")
    public ResponseEntity<Map<String, Long>> contarNotificacionesNoLeidas(@PathVariable String cedula) {
        Long contador = notificacionService.contarNotificacionesNoLeidas(cedula);
        return ResponseEntity.ok(Map.of("noLeidas", contador));
    }
    
    /**
     * Buscar estudiante por cédula para invitar
     */
    @GetMapping("/buscar-estudiante/{cedula}")
    public ResponseEntity<?> buscarEstudiante(@PathVariable String cedula) {
        try {
            EstudianteDTO estudiante = notificacionService.buscarEstudiante(cedula);
            return ResponseEntity.ok(estudiante);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    /**
     * Buscar estudiante por código estudiantil para invitar
     */
    @GetMapping("/buscar-estudiante-codigo/{codigo}")
    public ResponseEntity<?> buscarEstudiantePorCodigo(@PathVariable String codigo) {
        try {
            EstudianteDTO estudiante = notificacionService.buscarEstudiantePorCodigo(codigo);
            return ResponseEntity.ok(estudiante);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    /**
     * Enviar invitación a un compañero
     */
    @PostMapping("/invitar-companero")
    public ResponseEntity<?> enviarInvitacion(@RequestBody InvitacionRequest request) {
        try {
            NotificacionDTO notificacion = notificacionService.enviarInvitacion(request);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Invitación enviada correctamente",
                    "notificacion", notificacion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Responder invitación (Aceptar/Rechazar)
     */
    @PostMapping("/{id}/responder")
    public ResponseEntity<?> responderInvitacion(
            @PathVariable Integer id,
            @RequestBody ResponderInvitacionRequest request) {
        try {
            NotificacionDTO notificacion = notificacionService.responderInvitacion(id, request.getRespuesta());
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Invitación " + request.getRespuesta().toLowerCase(),
                    "notificacion", notificacion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Marcar notificación como leída
     */
    @PatchMapping("/{id}/marcar-leida")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Integer id) {
        try {
            NotificacionDTO notificacion = notificacionService.marcarComoLeida(id);
            return ResponseEntity.ok(notificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // ========================================
    // ENDPOINTS PARA DIRECTORES
    // ========================================
    
    /**
     * Buscar directores (profesores y directores externos) por nombre o especialidad
     */
    @GetMapping("/buscar-directores")
    public ResponseEntity<List<DirectorDTO>> buscarDirectores(@RequestParam(required = false) String busqueda) {
        return ResponseEntity.ok(notificacionService.buscarDirectores(busqueda));
    }
    
    /**
     * Listar todos los directores disponibles
     */
    @GetMapping("/listar-directores")
    public ResponseEntity<List<DirectorDTO>> listarTodosLosDirectores() {
        return ResponseEntity.ok(notificacionService.listarTodosLosDirectores());
    }
    
    /**
     * Enviar invitación a un director
     */
    @PostMapping("/invitar-director")
    public ResponseEntity<?> enviarInvitacionDirector(@RequestBody InvitacionDirectorRequest request) {
        try {
            NotificacionDTO notificacion = notificacionService.enviarInvitacionDirector(request);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Invitación enviada correctamente al director",
                    "notificacion", notificacion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Responder invitación de dirección (Aceptar/Rechazar)
     */
    @PostMapping("/{id}/responder-direccion")
    public ResponseEntity<?> responderInvitacionDirector(
            @PathVariable Integer id,
            @RequestBody ResponderInvitacionRequest request) {
        try {
            NotificacionDTO notificacion = notificacionService.responderInvitacionDirector(id, request.getRespuesta());
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Invitación de dirección " + request.getRespuesta().toLowerCase(),
                    "notificacion", notificacion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Cancelar invitación de dirección
     */
    @PostMapping("/{id}/cancelar-invitacion-director")
    public ResponseEntity<?> cancelarInvitacionDirector(@PathVariable Integer id) {
        try {
            NotificacionDTO notificacion = notificacionService.cancelarInvitacionDirector(id);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Invitación cancelada correctamente",
                    "notificacion", notificacion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Obtener invitación pendiente de un proyecto
     */
    @GetMapping("/proyecto/{proyectoId}/invitacion-pendiente")
    public ResponseEntity<?> obtenerInvitacionPendiente(@PathVariable Integer proyectoId) {
        try {
            NotificacionDTO invitacion = notificacionService.obtenerInvitacionPendienteProyecto(proyectoId);
            if (invitacion == null) {
                return ResponseEntity.ok(Collections.singletonMap("invitacion", null));
            }
            return ResponseEntity.ok(Map.of("invitacion", invitacion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
