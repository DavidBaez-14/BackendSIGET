package co.edu.ufps.proyectosdegradoufps.reuniones.dtos;

import lombok.Data;

@Data
public class MarcarAsistenciaRequest {
    private Boolean asistioEstudiante;
    // private Boolean asistioDirector; // Campo removido - solo marcamos asistencia del estudiante
    private String observaciones;
}
