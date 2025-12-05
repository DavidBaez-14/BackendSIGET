package co.edu.ufps.proyectosdegradoufps.proyectos.dtos;

import co.edu.ufps.proyectosdegradoufps.usuarios.dtos.EstudianteDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProyectoDTO {
    private Integer id;
    private String codigoProyecto;
    private String titulo;
    private String descripcion;
    private String objetivoGeneral;
    
    // IDs for creation/update
    private Integer modalidadId;
    private Integer lineaInvestigacionId;
    private String directorCedula;
    private String tipoDirector; // "PROFESOR" or "EXTERNO"
    private List<String> estudiantesCedulas; // For creation (additional students)
    
    // Display fields
    private String modalidadNombre;
    private String lineaInvestigacionNombre;
    private String estado;
    private String fase;
    private String directorNombre;
    
    // Fechas
    private LocalDate fechaPresentacion;
    private LocalDate fechaInicioDesarrollo;
    private LocalDate fechaFinEstimada;
    private LocalDate fechaSustentacion;
    private LocalDateTime fechaUltimaActualizacion;
    
    // Seguimiento y resultados
    private Integer porcentajeAvance;
    private BigDecimal notaFinal;
    private String resultadoFinal;
    
    // Documentos
    private String documentoPropuestaUrl;
    private String documentoFinalUrl;
    
    private List<EstudianteDTO> estudiantes; 
}
