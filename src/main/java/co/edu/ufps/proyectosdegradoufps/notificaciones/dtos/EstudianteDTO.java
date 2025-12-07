package co.edu.ufps.proyectosdegradoufps.notificaciones.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteDTO {
    private String cedula;
    private String nombre;
    private String codigo;
    private String programaId;
}
