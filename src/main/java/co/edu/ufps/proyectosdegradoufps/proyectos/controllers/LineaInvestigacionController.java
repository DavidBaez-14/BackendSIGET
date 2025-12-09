package co.edu.ufps.proyectosdegradoufps.proyectos.controllers;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.LineaInvestigacion;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.LineaInvestigacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/lineas-investigacion")
public class LineaInvestigacionController {

    @Autowired
    private LineaInvestigacionRepository lineaInvestigacionRepository;

    /**
     * Obtener todas las líneas de investigación activas
     */
    @GetMapping
    public ResponseEntity<List<LineaInvestigacion>> listarLineasActivas() {
        List<LineaInvestigacion> lineas = lineaInvestigacionRepository.findByActivoTrue();
        return ResponseEntity.ok(lineas);
    }

    /**
     * Obtener línea de investigación por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LineaInvestigacion> obtenerLineaPorId(@PathVariable Integer id) {
        return lineaInvestigacionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
