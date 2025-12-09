package co.edu.ufps.proyectosdegradoufps.proyectos.controllers;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.ModalidadProyecto;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.ModalidadProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/modalidades")
public class ModalidadProyectoController {

    @Autowired
    private ModalidadProyectoRepository modalidadProyectoRepository;

    /**
     * Obtener todas las modalidades de proyecto
     */
    @GetMapping
    public ResponseEntity<List<ModalidadProyecto>> listarModalidades() {
        List<ModalidadProyecto> modalidades = modalidadProyectoRepository.findAll();
        return ResponseEntity.ok(modalidades);
    }

    /**
     * Obtener modalidad por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModalidadProyecto> obtenerModalidadPorId(@PathVariable Integer id) {
        return modalidadProyectoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
