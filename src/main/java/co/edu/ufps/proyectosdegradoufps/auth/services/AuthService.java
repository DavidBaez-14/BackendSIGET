package co.edu.ufps.proyectosdegradoufps.auth.services;

import co.edu.ufps.proyectosdegradoufps.auth.dtos.LoginResponse;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.*;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AdministradorRepository administradorRepository;
    
    @Autowired
    private ProfesorRepository profesorRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private DirectorExternoRepository directorExternoRepository;
    
    public LoginResponse login(String cedula, String password) {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(cedula)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        
        // Verificar que esté activo
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }
        
        // Por ahora validación simple - TODO: usar BCrypt para producción
        if (!password.equals("1234")) {  // Contraseña temporal para todos
            throw new RuntimeException("Credenciales inválidas");
        }
        
        // Construir respuesta según el tipo de usuario
        LoginResponse response = new LoginResponse();
        response.setCedula(usuario.getCedula());
        response.setNombre(usuario.getNombres() + " " + usuario.getApellidos());
        response.setEmail(usuario.getEmail());
        
        // Determinar rol y permisos
        switch (usuario.getTipoUsuario()) {
            case PROFESOR:
                // Verificar si es administrador (profesor con registro en tabla administradores)
                Administrador admin = administradorRepository.findByProfesorCedula(usuario.getCedula());
                
                if (admin != null && admin.getActivo()) {
                    // Es administrador activo
                    return buildAdminResponse(usuario, response, admin);
                } else {
                    // Es profesor/director normal
                    return buildProfesorResponse(usuario, response);
                }
                
            case ESTUDIANTE:
                return buildEstudianteResponse(usuario, response);
                
            case DIRECTOR_EXTERNO:
                return buildDirectorExternoResponse(usuario, response);
                
            default:
                throw new RuntimeException("Tipo de usuario no reconocido");
        }
    }
    
    private LoginResponse buildAdminResponse(Usuario usuario, LoginResponse response, Administrador admin) {
        response.setRol("ADMINISTRADOR");
        response.setEsAdminGeneral(admin.getEsAdminGeneral());
        
        // Nota: El comité y programa se cargan por separado en el frontend usando adminService.obtenerInfoAdmin()
        // No intentar acceder a admin.getComite() aquí (lazy loading causaría excepción)
        
        response.setMessage("Bienvenido " + (admin.getEsAdminGeneral() ? "Administrador General" : "Coordinador"));
        
        return response;
    }
    
    private LoginResponse buildProfesorResponse(Usuario usuario, LoginResponse response) {
        Profesor profesor = profesorRepository.findById(usuario.getCedula())
                .orElseThrow(() -> new RuntimeException("Datos de profesor no encontrados"));
        
        response.setRol("DIRECTOR");
        response.setEsAdminGeneral(false);
        response.setMessage("Bienvenido Director");
        
        return response;
    }
    
    private LoginResponse buildEstudianteResponse(Usuario usuario, LoginResponse response) {
        Estudiante estudiante = estudianteRepository.findById(usuario.getCedula())
                .orElseThrow(() -> new RuntimeException("Datos de estudiante no encontrados"));
        
        response.setRol("ESTUDIANTE");
        response.setEsAdminGeneral(false);
        response.setProgramaCodigo(estudiante.getProgramaCodigo());
        response.setMessage("Bienvenido Estudiante");
        
        return response;
    }
    
    private LoginResponse buildDirectorExternoResponse(Usuario usuario, LoginResponse response) {
        DirectorExterno director = directorExternoRepository.findById(usuario.getCedula())
                .orElseThrow(() -> new RuntimeException("Datos de director externo no encontrados"));
        
        if (!director.getAprobado()) {
            throw new RuntimeException("Director externo pendiente de aprobación");
        }
        
        response.setRol("DIRECTOR");
        response.setEsAdminGeneral(false);
        response.setMessage("Bienvenido Director Externo");
        
        return response;
    }
}
