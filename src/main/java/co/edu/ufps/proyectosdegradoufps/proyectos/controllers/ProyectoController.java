package co.edu.ufps.proyectosdegradoufps.proyectos.controllers;

import co.edu.ufps.proyectosdegradoufps.proyectos.dtos.ProyectoDTO;
import co.edu.ufps.proyectosdegradoufps.proyectos.services.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @PostMapping("/crear/{cedulaEstudiante}")
    public ResponseEntity<ProyectoDTO> crearProyecto(@PathVariable String cedulaEstudiante, @RequestBody ProyectoDTO proyectoDTO) {
        try {
            ProyectoDTO nuevoProyecto = proyectoService.crearProyecto(proyectoDTO, cedulaEstudiante);
            return ResponseEntity.ok(nuevoProyecto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Or return error message wrapper
        }
    }

    @GetMapping("/director/{cedula}")
    public ResponseEntity<List<ProyectoDTO>> listarProyectosDirector(@PathVariable String cedula) {
        List<ProyectoDTO> proyectos = proyectoService.listarProyectosPorDirector(cedula);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping
    public ResponseEntity<List<ProyectoDTO>> listarTodosLosProyectos() {
        List<ProyectoDTO> proyectos = proyectoService.listarTodosLosProyectos();
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDTO> obtenerProyectoPorId(@PathVariable Integer id) {
        try {
            ProyectoDTO proyecto = proyectoService.obtenerProyectoPorId(id);
            return ResponseEntity.ok(proyecto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estudiante/{cedula}")
    public ResponseEntity<ProyectoDTO> obtenerProyectoEstudiante(@PathVariable String cedula) {
        ProyectoDTO proyecto = proyectoService.obtenerProyectoActivoPorEstudiante(cedula);
        if (proyecto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(proyecto);
    }

    /**
     * Endpoint para administradores: filtra proyectos según el programa del admin.
     * Si es admin general → devuelve todos.
     * Si no → devuelve solo proyectos del programa de su comité.
     */
    @GetMapping("/admin/{cedulaAdmin}")
    public ResponseEntity<List<ProyectoDTO>> listarProyectosPorAdministrador(@PathVariable String cedulaAdmin) {
        try {
            List<ProyectoDTO> proyectos = proyectoService.listarProyectosPorAdministrador(cedulaAdmin);
            return ResponseEntity.ok(proyectos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene información del administrador (nombre, comité, programa, es_admin_general)
     */
    @GetMapping("/admin/{cedulaAdmin}/info")
    public ResponseEntity<ProyectoService.AdministradorInfoDTO> obtenerInfoAdministrador(@PathVariable String cedulaAdmin) {
        ProyectoService.AdministradorInfoDTO info = proyectoService.obtenerInfoAdministrador(cedulaAdmin);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }
}
