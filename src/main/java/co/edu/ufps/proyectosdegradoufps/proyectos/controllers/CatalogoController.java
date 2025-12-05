package co.edu.ufps.proyectosdegradoufps.proyectos.controllers;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.AreaInvestigacion;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.LineaInvestigacion;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.ModalidadProyecto;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.AreaInvestigacionRepository;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.LineaInvestigacionRepository;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.ModalidadProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/catalogos")
public class CatalogoController {

    @Autowired
    private ModalidadProyectoRepository modalidadRepository;

    @Autowired
    private AreaInvestigacionRepository areaRepository;

    @Autowired
    private LineaInvestigacionRepository lineaRepository;

    @GetMapping("/modalidades")
    public ResponseEntity<List<ModalidadProyecto>> listarModalidades() {
        return ResponseEntity.ok(modalidadRepository.findAll());
    }

    @GetMapping("/areas")
    public ResponseEntity<List<AreaInvestigacion>> listarAreas() {
        return ResponseEntity.ok(areaRepository.findAll());
    }

    @GetMapping("/lineas")
    public ResponseEntity<List<LineaInvestigacion>> listarLineas() {
        return ResponseEntity.ok(lineaRepository.findAll());
    }

    @GetMapping("/lineas/area/{areaId}")
    public ResponseEntity<List<LineaInvestigacion>> listarLineasPorArea(@PathVariable Integer areaId) {
        return ResponseEntity.ok(lineaRepository.findByAreaInvestigacionId(areaId));
    }
}
