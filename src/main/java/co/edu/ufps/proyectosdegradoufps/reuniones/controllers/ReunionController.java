package co.edu.ufps.proyectosdegradoufps.reuniones.controllers;

import co.edu.ufps.proyectosdegradoufps.reuniones.dtos.*;
import co.edu.ufps.proyectosdegradoufps.reuniones.services.ReunionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reuniones")
@CrossOrigin(origins = "http://localhost:5173")
public class ReunionController {

    @Autowired
    private ReunionService reunionService;

    /**
     * Solicitar una reunión
     */
    @PostMapping("/solicitar")
    public ResponseEntity<Map<String, Object>> solicitarReunion(@RequestBody SolicitarReunionRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            ReunionDTO reunion = reunionService.solicitarReunion(request);
            response.put("success", true);
            response.put("message", "Solicitud de reunión enviada exitosamente");
            response.put("data", reunion);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al solicitar reunión: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Responder a una solicitud de reunión (aceptar o rechazar)
     */
    @PutMapping("/{reunionId}/responder")
    public ResponseEntity<Map<String, Object>> responderSolicitudReunion(
            @PathVariable Integer reunionId,
            @RequestParam String respondenteCedula,
            @RequestBody ResponderReunionRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            ReunionDTO reunion = reunionService.responderSolicitudReunion(reunionId, respondenteCedula, request);
            response.put("success", true);
            
            if (request.getAceptada()) {
                response.put("message", "Reunión aceptada exitosamente");
                response.put("data", reunion);
            } else {
                response.put("message", "Reunión rechazada exitosamente");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al responder solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Registrar detalles de la reunión (después de realizarla)
     */
    @PutMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarDetallesReunion(@RequestBody RegistrarReunionRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            ReunionDTO reunion = reunionService.registrarDetallesReunion(request);
            response.put("success", true);
            response.put("message", "Detalles de reunión registrados exitosamente");
            response.put("data", reunion);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al registrar detalles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Marcar asistencia a una reunión
     */
    @PutMapping("/{reunionId}/asistencia")
    public ResponseEntity<Map<String, Object>> marcarAsistencia(
            @PathVariable Integer reunionId,
            @RequestBody MarcarAsistenciaRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            ReunionDTO reunion = reunionService.marcarAsistencia(reunionId, request);
            response.put("success", true);
            response.put("message", "Asistencia marcada exitosamente");
            response.put("data", reunion);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al marcar asistencia: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener todas las reuniones de un proyecto
     */
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<Map<String, Object>> obtenerReunionesProyecto(@PathVariable Integer proyectoId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ReunionDTO> reuniones = reunionService.obtenerReunionesProyecto(proyectoId);
            response.put("success", true);
            response.put("data", reuniones);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reuniones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener reuniones de un director
     */
    @GetMapping("/director/{cedulaDirector}")
    public ResponseEntity<Map<String, Object>> obtenerReunionesDirector(@PathVariable String cedulaDirector) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ReunionDTO> reuniones = reunionService.obtenerReunionesDirector(cedulaDirector);
            response.put("success", true);
            response.put("data", reuniones);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reuniones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener reuniones de un estudiante
     */
    @GetMapping("/estudiante/{cedulaEstudiante}")
    public ResponseEntity<Map<String, Object>> obtenerReunionesEstudiante(@PathVariable String cedulaEstudiante) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ReunionDTO> reuniones = reunionService.obtenerReunionesEstudiante(cedulaEstudiante);
            response.put("success", true);
            response.put("data", reuniones);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reuniones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Cancelar una reunión
     */
    @DeleteMapping("/{reunionId}/cancelar")
    public ResponseEntity<Map<String, Object>> cancelarReunion(
            @PathVariable Integer reunionId,
            @RequestParam String usuarioCedula) {
        Map<String, Object> response = new HashMap<>();
        try {
            reunionService.cancelarReunion(reunionId, usuarioCedula);
            response.put("success", true);
            response.put("message", "Reunión cancelada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al cancelar reunión: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
