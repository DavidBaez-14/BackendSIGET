package co.edu.ufps.proyectosdegradoufps.usuarios.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteDTO {
    // Datos de Usuario
    private String cedula;
    private String email;
    private String nombres;
    private String apellidos;
    private String telefono;
    private Integer paisId;
    private String tipoUsuario;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
    
    // Datos específicos de Estudiante
    private String codigoEstudiantil;
    private String programaCodigo;
    private LocalDate fechaIngreso;
}
