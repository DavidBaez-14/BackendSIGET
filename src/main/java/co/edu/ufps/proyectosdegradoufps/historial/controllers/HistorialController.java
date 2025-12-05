package co.edu.ufps.proyectosdegradoufps.historial.controllers;

import co.edu.ufps.proyectosdegradoufps.historial.dtos.*;
import co.edu.ufps.proyectosdegradoufps.historial.services.HistorialService;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.EstadoProyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historial")
@CrossOrigin(origins = "*")
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    /**
     * Obtiene todos los tipos de evento que cambian estado
     * GET /historial/eventos-cambio-estado
     */
    @GetMapping("/eventos-cambio-estado")
    public ResponseEntity<List<TipoEventoDTO>> obtenerEventosCambianEstado() {
        return ResponseEntity.ok(historialService.obtenerEventosCambianEstado());
    }

    /**
     * Obtiene todos los tipos de evento
     * GET /historial/eventos
     */
    @GetMapping("/eventos")
    public ResponseEntity<List<TipoEventoDTO>> obtenerTodosLosEventos() {
        return ResponseEntity.ok(historialService.obtenerTodosLosEventos());
    }

    /**
     * Obtiene todos los estados de proyecto
     * GET /historial/estados
     */
    @GetMapping("/estados")
    public ResponseEntity<List<EstadoProyecto>> obtenerTodosLosEstados() {
        return ResponseEntity.ok(historialService.obtenerTodosLosEstados());
    }

    /**
     * Obtiene el historial de un proyecto
     * GET /historial/proyecto/{proyectoId}
     */
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<HistorialDTO>> obtenerHistorialPorProyecto(@PathVariable Integer proyectoId) {
        return ResponseEntity.ok(historialService.obtenerHistorialPorProyecto(proyectoId));
    }

    /**
     * Cambia el estado de un proyecto mediante un evento
     * POST /historial/proyecto/{proyectoId}/cambiar-estado
     */
    @PostMapping("/proyecto/{proyectoId}/cambiar-estado")
    public ResponseEntity<HistorialDTO> cambiarEstadoProyecto(
            @PathVariable Integer proyectoId,
            @RequestBody CambioEstadoRequest request) {
        return ResponseEntity.ok(historialService.cambiarEstadoProyecto(proyectoId, request));
    }

    /**
     * Registra un evento en el historial sin cambiar el estado
     * POST /historial/proyecto/{proyectoId}/registrar-evento
     */
    @PostMapping("/proyecto/{proyectoId}/registrar-evento")
    public ResponseEntity<HistorialDTO> registrarEvento(
            @PathVariable Integer proyectoId,
            @RequestBody CambioEstadoRequest request) {
        return ResponseEntity.ok(historialService.registrarEvento(proyectoId, request));
    }
}
