package co.edu.ufps.proyectosdegradoufps.usuarios.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorDTO {
    private Integer id;
    private String profesorCedula;
    private String cargo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;
    
    // Datos del profesor asociado
    private String profesorNombres;
    private String profesorApellidos;
    private String profesorEmail;
    
    // Nuevos campos para filtrado por programa
    private Integer comiteId;
    private String comiteNombre;
    private String programaCodigo;
    private Boolean esAdminGeneral;
}
