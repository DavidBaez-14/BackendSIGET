package co.edu.ufps.proyectosdegradoufps.reuniones.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReunionDTO {
    private Integer id;
    private Integer proyectoId;
    private String proyectoTitulo;
    private LocalDateTime fechaReunion;
    private Integer duracionMinutos;
    private String tipo;
    private String temasTratados;
    private String acuerdos;
    private LocalDate proximaReunion;
    private Boolean asistioEstudiante;
    // private Boolean asistioDirector; // Campo removido - solo marcamos asistencia del estudiante
    private String observaciones;
    // Campos que NO existen en la BD:
    // private Boolean confirmada;
    // private String solicitanteCedula;
    // private String solicitanteNombre;
    // private LocalDateTime fechaCreacion;
}
