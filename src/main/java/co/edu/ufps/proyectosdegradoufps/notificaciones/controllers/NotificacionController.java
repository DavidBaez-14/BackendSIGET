package co.edu.ufps.proyectosdegradoufps.notificaciones.controllers;

import co.edu.ufps.proyectosdegradoufps.notificaciones.dtos.InvitacionRequest;
import co.edu.ufps.proyectosdegradoufps.notificaciones.dtos.NotificacionDTO;
import co.edu.ufps.proyectosdegradoufps.notificaciones.dtos.ResponderInvitacionRequest;
import co.edu.ufps.proyectosdegradoufps.notificaciones.dtos.EstudianteDTO;
import co.edu.ufps.proyectosdegradoufps.notificaciones.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
