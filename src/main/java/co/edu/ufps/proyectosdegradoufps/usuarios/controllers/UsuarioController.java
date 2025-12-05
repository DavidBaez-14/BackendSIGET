package co.edu.ufps.proyectosdegradoufps.usuarios.controllers;

import co.edu.ufps.proyectosdegradoufps.usuarios.dtos.*;
import co.edu.ufps.proyectosdegradoufps.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    // ENDPOINTS ESTUDIANTES

    @GetMapping("/estudiantes")
    public ResponseEntity<List<EstudianteDTO>> obtenerEstudiantes() {
        return ResponseEntity.ok(usuarioService.obtenerTodosEstudiantes());
    }
    
    @GetMapping("/estudiantes/{cedula}")
    public ResponseEntity<EstudianteDTO> obtenerEstudiante(@PathVariable String cedula) {
        EstudianteDTO estudiante = usuarioService.obtenerEstudiantePorCedula(cedula);
        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudiante);
    }
    
    @GetMapping("/estudiantes/codigo/{codigoEstudiantil}")
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorCodigo(@PathVariable String codigoEstudiantil) {
        EstudianteDTO estudiante = usuarioService.obtenerEstudiantePorCodigo(codigoEstudiantil);
        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudiante);
    }
    
    @PostMapping("/estudiantes")
    public ResponseEntity<?> crearEstudiante(@RequestBody EstudianteDTO dto) {
        try {
            EstudianteDTO creado = usuarioService.crearEstudiante(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/estudiantes/{cedula}")
    public ResponseEntity<?> actualizarEstudiante(@PathVariable String cedula, @RequestBody EstudianteDTO dto) {
        EstudianteDTO actualizado = usuarioService.actualizarEstudiante(cedula, dto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/estudiantes/{cedula}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable String cedula) {
        boolean eliminado = usuarioService.eliminarEstudiante(cedula);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
    
    // ========================================
    // ENDPOINTS PROFESORES
    // ========================================
    
    @GetMapping("/profesores")
    public ResponseEntity<List<ProfesorDTO>> obtenerProfesores() {
        return ResponseEntity.ok(usuarioService.obtenerTodosProfesores());
    }
    
    @GetMapping("/profesores/{cedula}")
    public ResponseEntity<ProfesorDTO> obtenerProfesor(@PathVariable String cedula) {
        ProfesorDTO profesor = usuarioService.obtenerProfesorPorCedula(cedula);
        if (profesor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profesor);
    }
    
    @GetMapping("/profesores/tipo/{tipoProfesor}")
    public ResponseEntity<List<ProfesorDTO>> obtenerProfesoresPorTipo(@PathVariable String tipoProfesor) {
        return ResponseEntity.ok(usuarioService.obtenerProfesoresPorTipo(tipoProfesor));
    }
    
    @PostMapping("/profesores")
    public ResponseEntity<?> crearProfesor(@RequestBody ProfesorDTO dto) {
        try {
            ProfesorDTO creado = usuarioService.crearProfesor(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/profesores/{cedula}")
    public ResponseEntity<?> actualizarProfesor(@PathVariable String cedula, @RequestBody ProfesorDTO dto) {
        ProfesorDTO actualizado = usuarioService.actualizarProfesor(cedula, dto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/profesores/{cedula}")
    public ResponseEntity<?> eliminarProfesor(@PathVariable String cedula) {
        boolean eliminado = usuarioService.eliminarProfesor(cedula);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
    
    // ========================================
    // ENDPOINTS DIRECTORES EXTERNOS
    // ========================================
    
    @GetMapping("/directores-externos")
    public ResponseEntity<List<DirectorExternoDTO>> obtenerDirectoresExternos() {
        return ResponseEntity.ok(usuarioService.obtenerTodosDirectoresExternos());
    }
    
    @GetMapping("/directores-externos/{cedula}")
    public ResponseEntity<DirectorExternoDTO> obtenerDirectorExterno(@PathVariable String cedula) {
        DirectorExternoDTO director = usuarioService.obtenerDirectorExternoPorCedula(cedula);
        if (director == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(director);
    }
    
    @GetMapping("/directores-externos/aprobados/{aprobado}")
    public ResponseEntity<List<DirectorExternoDTO>> obtenerDirectoresExternosPorAprobacion(@PathVariable Boolean aprobado) {
        return ResponseEntity.ok(usuarioService.obtenerDirectoresExternosPorAprobacion(aprobado));
    }
    
    @PostMapping("/directores-externos")
    public ResponseEntity<?> crearDirectorExterno(@RequestBody DirectorExternoDTO dto) {
        try {
            DirectorExternoDTO creado = usuarioService.crearDirectorExterno(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/directores-externos/{cedula}")
    public ResponseEntity<?> actualizarDirectorExterno(@PathVariable String cedula, @RequestBody DirectorExternoDTO dto) {
        DirectorExternoDTO actualizado = usuarioService.actualizarDirectorExterno(cedula, dto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/directores-externos/{cedula}")
    public ResponseEntity<?> eliminarDirectorExterno(@PathVariable String cedula) {
        boolean eliminado = usuarioService.eliminarDirectorExterno(cedula);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/directores-externos/{cedula}/aprobar")
    public ResponseEntity<?> aprobarDirectorExterno(@PathVariable String cedula, @RequestParam String actaAprobacion) {
        DirectorExternoDTO aprobado = usuarioService.aprobarDirectorExterno(cedula, actaAprobacion);
        if (aprobado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(aprobado);
    }
    
    // ========================================
    // ENDPOINTS ADMINISTRADORES
    // ========================================
    
    @GetMapping("/administradores")
    public ResponseEntity<List<AdministradorDTO>> obtenerAdministradores() {
        return ResponseEntity.ok(usuarioService.obtenerTodosAdministradores());
    }
    
    @GetMapping("/administradores/{id}")
    public ResponseEntity<AdministradorDTO> obtenerAdministrador(@PathVariable Integer id) {
        AdministradorDTO admin = usuarioService.obtenerAdministradorPorId(id);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(admin);
    }
    
    @GetMapping("/administradores/profesor/{profesorCedula}")
    public ResponseEntity<AdministradorDTO> obtenerAdministradorPorProfesor(@PathVariable String profesorCedula) {
        AdministradorDTO admin = usuarioService.obtenerAdministradorPorProfesor(profesorCedula);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(admin);
    }
    
    @PostMapping("/administradores")
    public ResponseEntity<?> crearAdministrador(@RequestBody AdministradorDTO dto) {
        try {
            AdministradorDTO creado = usuarioService.crearAdministrador(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/administradores/{id}")
    public ResponseEntity<?> actualizarAdministrador(@PathVariable Integer id, @RequestBody AdministradorDTO dto) {
        AdministradorDTO actualizado = usuarioService.actualizarAdministrador(id, dto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/administradores/{id}")
    public ResponseEntity<?> eliminarAdministrador(@PathVariable Integer id) {
        boolean eliminado = usuarioService.eliminarAdministrador(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
