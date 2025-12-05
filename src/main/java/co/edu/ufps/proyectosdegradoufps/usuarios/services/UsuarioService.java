package co.edu.ufps.proyectosdegradoufps.usuarios.services;

import co.edu.ufps.proyectosdegradoufps.usuarios.dtos.*;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.*;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private ProfesorRepository profesorRepository;
    
    @Autowired
    private DirectorExternoRepository directorExternoRepository;
    
    @Autowired
    private AdministradorRepository administradorRepository;
    
    // ========================================
    // ESTUDIANTES - CRUD
    // ========================================
    
    public List<EstudianteDTO> obtenerTodosEstudiantes() {
        return estudianteRepository.findAll().stream()
                .map(this::conversionEstudianteDTO)
                .collect(Collectors.toList());
    }
    
    public EstudianteDTO obtenerEstudiantePorCedula(String cedula) {
        Estudiante estudiante = estudianteRepository.findById(cedula).orElse(null);
        return estudiante != null ? conversionEstudianteDTO(estudiante) : null;
    }
    
    public EstudianteDTO obtenerEstudiantePorCodigo(String codigoEstudiantil) {
        Estudiante estudiante = estudianteRepository.findByCodigoEstudiantil(codigoEstudiantil);
        return estudiante != null ? conversionEstudianteDTO(estudiante) : null;
    }
    
    public EstudianteDTO crearEstudiante(EstudianteDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (estudianteRepository.existsByCodigoEstudiantil(dto.getCodigoEstudiantil())) {
            throw new RuntimeException("El código estudiantil ya existe");
        }
        
        Estudiante estudiante = new Estudiante();
        estudiante.setCedula(dto.getCedula());
        estudiante.setEmail(dto.getEmail());
        estudiante.setPasswordHash("$2b$10$defaultHash");
        estudiante.setNombres(dto.getNombres());
        estudiante.setApellidos(dto.getApellidos());
        estudiante.setTelefono(dto.getTelefono());
        estudiante.setPaisId(dto.getPaisId());
        estudiante.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        estudiante.setCodigoEstudiantil(dto.getCodigoEstudiantil());
        estudiante.setProgramaCodigo(dto.getProgramaCodigo());
        estudiante.setFechaIngreso(dto.getFechaIngreso());
        
        Estudiante nuevoEstudiante = estudianteRepository.save(estudiante);
        return conversionEstudianteDTO(nuevoEstudiante);
    }
    
    public EstudianteDTO actualizarEstudiante(String cedula, EstudianteDTO dto) {
        Estudiante estudiante = estudianteRepository.findById(cedula).orElse(null);
        if (estudiante == null) {
            return null;
        }
        
        estudiante.setEmail(dto.getEmail());
        estudiante.setNombres(dto.getNombres());
        estudiante.setApellidos(dto.getApellidos());
        estudiante.setTelefono(dto.getTelefono());
        estudiante.setProgramaCodigo(dto.getProgramaCodigo());
        estudiante.setActivo(dto.getActivo());
        
        Estudiante estudianteActualizado = estudianteRepository.save(estudiante);
        return conversionEstudianteDTO(estudianteActualizado);
    }
    
    public boolean eliminarEstudiante(String cedula) {
        if (estudianteRepository.existsById(cedula)) {
            estudianteRepository.deleteById(cedula);
            return true;
        }
        return false;
    }
    
    // ========================================
    // PROFESORES - CRUD
    // ========================================
    
    public List<ProfesorDTO> obtenerTodosProfesores() {
        return profesorRepository.findAll().stream()
                .map(this::conversionProfesorDTO)
                .collect(Collectors.toList());
    }
    
    public ProfesorDTO obtenerProfesorPorCedula(String cedula) {
        Profesor profesor = profesorRepository.findById(cedula).orElse(null);
        return profesor != null ? conversionProfesorDTO(profesor) : null;
    }
    
    public List<ProfesorDTO> obtenerProfesoresPorTipo(String tipoProfesor) {
        Profesor.TipoProfesor tipo = Profesor.TipoProfesor.valueOf(tipoProfesor);
        return profesorRepository.findByTipoProfesor(tipo).stream()
                .map(this::conversionProfesorDTO)
                .collect(Collectors.toList());
    }
    
    public ProfesorDTO crearProfesor(ProfesorDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        Profesor profesor = new Profesor();
        profesor.setCedula(dto.getCedula());
        profesor.setEmail(dto.getEmail());
        profesor.setPasswordHash("$2b$10$defaultHash");
        profesor.setNombres(dto.getNombres());
        profesor.setApellidos(dto.getApellidos());
        profesor.setTelefono(dto.getTelefono());
        profesor.setPaisId(dto.getPaisId());
        profesor.setTipoUsuario(Usuario.TipoUsuario.PROFESOR);
        profesor.setTituloAcademico(dto.getTituloAcademico());
        profesor.setEspecialidad(dto.getEspecialidad());
        profesor.setTipoProfesor(Profesor.TipoProfesor.valueOf(dto.getTipoProfesor()));
        profesor.setFechaVinculacion(dto.getFechaVinculacion());
        
        Profesor nuevoProfesor = profesorRepository.save(profesor);
        return conversionProfesorDTO(nuevoProfesor);
    }
    
    public ProfesorDTO actualizarProfesor(String cedula, ProfesorDTO dto) {
        Profesor profesor = profesorRepository.findById(cedula).orElse(null);
        if (profesor == null) {
            return null;
        }
        
        profesor.setEmail(dto.getEmail());
        profesor.setNombres(dto.getNombres());
        profesor.setApellidos(dto.getApellidos());
        profesor.setTelefono(dto.getTelefono());
        profesor.setTituloAcademico(dto.getTituloAcademico());
        profesor.setEspecialidad(dto.getEspecialidad());
        profesor.setActivo(dto.getActivo());
        
        Profesor profesorActualizado = profesorRepository.save(profesor);
        return conversionProfesorDTO(profesorActualizado);
    }
    
    public boolean eliminarProfesor(String cedula) {
        if (profesorRepository.existsById(cedula)) {
            profesorRepository.deleteById(cedula);
            return true;
        }
        return false;
    }
    
    // ========================================
    // DIRECTORES EXTERNOS - CRUD
    // ========================================
    
    public List<DirectorExternoDTO> obtenerTodosDirectoresExternos() {
        return directorExternoRepository.findAll().stream()
                .map(this::conversionDirectorExternoDTO)
                .collect(Collectors.toList());
    }
    
    public DirectorExternoDTO obtenerDirectorExternoPorCedula(String cedula) {
        DirectorExterno director = directorExternoRepository.findById(cedula).orElse(null);
        return director != null ? conversionDirectorExternoDTO(director) : null;
    }
    
    public List<DirectorExternoDTO> obtenerDirectoresExternosPorAprobacion(Boolean aprobado) {
        return directorExternoRepository.findByAprobado(aprobado).stream()
                .map(this::conversionDirectorExternoDTO)
                .collect(Collectors.toList());
    }
    
    public DirectorExternoDTO crearDirectorExterno(DirectorExternoDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        DirectorExterno director = new DirectorExterno();
        director.setCedula(dto.getCedula());
        director.setEmail(dto.getEmail());
        director.setPasswordHash("$2b$10$defaultHash");
        director.setNombres(dto.getNombres());
        director.setApellidos(dto.getApellidos());
        director.setTelefono(dto.getTelefono());
        director.setPaisId(dto.getPaisId());
        director.setTipoUsuario(Usuario.TipoUsuario.DIRECTOR_EXTERNO);
        director.setTituloAcademico(dto.getTituloAcademico());
        director.setEspecialidad(dto.getEspecialidad());
        director.setInstitucion(dto.getInstitucion());
        director.setAprobado(false);
        
        DirectorExterno nuevoDirectorExt = directorExternoRepository.save(director);
        return conversionDirectorExternoDTO(nuevoDirectorExt);
    }
    
    public DirectorExternoDTO actualizarDirectorExterno(String cedula, DirectorExternoDTO dto) {
        DirectorExterno director = directorExternoRepository.findById(cedula).orElse(null);
        if (director == null) {
            return null;
        }
        
        director.setEmail(dto.getEmail());
        director.setNombres(dto.getNombres());
        director.setApellidos(dto.getApellidos());
        director.setTelefono(dto.getTelefono());
        director.setTituloAcademico(dto.getTituloAcademico());
        director.setEspecialidad(dto.getEspecialidad());
        director.setInstitucion(dto.getInstitucion());
        director.setActivo(dto.getActivo());
        
        DirectorExterno directorExternoActualizado = directorExternoRepository.save(director);
        return conversionDirectorExternoDTO(directorExternoActualizado);
    }
    
    public boolean eliminarDirectorExterno(String cedula) {
        if (directorExternoRepository.existsById(cedula)) {
            directorExternoRepository.deleteById(cedula);
            return true;
        }
        return false;
    }
    
    public DirectorExternoDTO aprobarDirectorExterno(String cedula, String actaAprobacion) {
        DirectorExterno director = directorExternoRepository.findById(cedula).orElse(null);
        if (director == null) {
            return null;
        }
        
        director.setAprobado(true);
        director.setActaAprobacionComite(actaAprobacion);
        director.setFechaAprobacion(java.time.LocalDate.now());
        
        DirectorExterno directorExternoAprobado = directorExternoRepository.save(director);
        return conversionDirectorExternoDTO(directorExternoAprobado);
    }
    
    // ========================================
    // ADMINISTRADORES - CRUD
    // ========================================
    
    public List<AdministradorDTO> obtenerTodosAdministradores() {
        return administradorRepository.findAll().stream()
                .map(this::conversionAdministradorDTO)
                .collect(Collectors.toList());
    }
    
    public AdministradorDTO obtenerAdministradorPorId(Integer id) {
        Administrador admin = administradorRepository.findById(id).orElse(null);
        return admin != null ? conversionAdministradorDTO(admin) : null;
    }
    
    public AdministradorDTO obtenerAdministradorPorProfesor(String profesorCedula) {
        Administrador admin = administradorRepository.findByProfesorCedula(profesorCedula);
        return admin != null ? conversionAdministradorDTO(admin) : null;
    }
    
    public AdministradorDTO crearAdministrador(AdministradorDTO dto) {
        if (!profesorRepository.existsById(dto.getProfesorCedula())) {
            throw new RuntimeException("El profesor no existe");
        }
        
        Administrador admin = new Administrador();
        admin.setProfesorCedula(dto.getProfesorCedula());
        admin.setCargo(dto.getCargo());
        admin.setFechaInicio(dto.getFechaInicio());
        admin.setFechaFin(dto.getFechaFin());
        admin.setActivo(true);
        
        Administrador nuevoAdministrador = administradorRepository.save(admin);
        return conversionAdministradorDTO(nuevoAdministrador);
    }
    
    public AdministradorDTO actualizarAdministrador(Integer id, AdministradorDTO dto) {
        Administrador admin = administradorRepository.findById(id).orElse(null);
        if (admin == null) {
            return null;
        }
        
        admin.setCargo(dto.getCargo());
        admin.setFechaFin(dto.getFechaFin());
        admin.setActivo(dto.getActivo());
        
        Administrador administradorActualizado = administradorRepository.save(admin);
        return conversionAdministradorDTO(administradorActualizado);
    }
    
    public boolean eliminarAdministrador(Integer id) {
        if (administradorRepository.existsById(id)) {
            administradorRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // ========================================
    // MÉTODOS DE CONVERSIÓN DTO
    // ========================================
    
    private EstudianteDTO conversionEstudianteDTO(Estudiante estudiante) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setCedula(estudiante.getCedula());
        dto.setEmail(estudiante.getEmail());
        dto.setNombres(estudiante.getNombres());
        dto.setApellidos(estudiante.getApellidos());
        dto.setTelefono(estudiante.getTelefono());
        dto.setPaisId(estudiante.getPaisId());
        dto.setTipoUsuario(estudiante.getTipoUsuario().name());
        dto.setActivo(estudiante.getActivo());
        dto.setFechaRegistro(estudiante.getFechaRegistro());
        dto.setCodigoEstudiantil(estudiante.getCodigoEstudiantil());
        dto.setProgramaCodigo(estudiante.getProgramaCodigo());
        dto.setFechaIngreso(estudiante.getFechaIngreso());
        return dto;
    }
    
    private ProfesorDTO conversionProfesorDTO(Profesor profesor) {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setCedula(profesor.getCedula());
        dto.setEmail(profesor.getEmail());
        dto.setNombres(profesor.getNombres());
        dto.setApellidos(profesor.getApellidos());
        dto.setTelefono(profesor.getTelefono());
        dto.setPaisId(profesor.getPaisId());
        dto.setTipoUsuario(profesor.getTipoUsuario().name());
        dto.setActivo(profesor.getActivo());
        dto.setFechaRegistro(profesor.getFechaRegistro());
        dto.setTituloAcademico(profesor.getTituloAcademico());
        dto.setEspecialidad(profesor.getEspecialidad());
        dto.setTipoProfesor(profesor.getTipoProfesor().name());
        dto.setFechaVinculacion(profesor.getFechaVinculacion());
        return dto;
    }
    
    private DirectorExternoDTO conversionDirectorExternoDTO(DirectorExterno director) {
        DirectorExternoDTO dto = new DirectorExternoDTO();
        dto.setCedula(director.getCedula());
        dto.setEmail(director.getEmail());
        dto.setNombres(director.getNombres());
        dto.setApellidos(director.getApellidos());
        dto.setTelefono(director.getTelefono());
        dto.setPaisId(director.getPaisId());
        dto.setTipoUsuario(director.getTipoUsuario().name());
        dto.setActivo(director.getActivo());
        dto.setFechaRegistro(director.getFechaRegistro());
        dto.setTituloAcademico(director.getTituloAcademico());
        dto.setEspecialidad(director.getEspecialidad());
        dto.setInstitucion(director.getInstitucion());
        dto.setActaAprobacionComite(director.getActaAprobacionComite());
        dto.setFechaAprobacion(director.getFechaAprobacion());
        dto.setAprobado(director.getAprobado());
        return dto;
    }
    
    private AdministradorDTO conversionAdministradorDTO(Administrador admin) {
        AdministradorDTO dto = new AdministradorDTO();
        dto.setId(admin.getId());
        dto.setProfesorCedula(admin.getProfesorCedula());
        dto.setCargo(admin.getCargo());
        dto.setFechaInicio(admin.getFechaInicio());
        dto.setFechaFin(admin.getFechaFin());
        dto.setActivo(admin.getActivo());
        
        if (admin.getProfesor() != null) {
            dto.setProfesorNombres(admin.getProfesor().getNombres());
            dto.setProfesorApellidos(admin.getProfesor().getApellidos());
            dto.setProfesorEmail(admin.getProfesor().getEmail());
        }
        
        return dto;
    }
}
