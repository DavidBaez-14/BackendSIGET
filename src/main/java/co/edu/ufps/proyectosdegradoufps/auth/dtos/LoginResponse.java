package co.edu.ufps.proyectosdegradoufps.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String cedula;
    private String nombre;
    private String email;
    private String rol;
    private Boolean esAdminGeneral;
    private String comiteNombre;
    private String programaCodigo;
    private String message;
}
