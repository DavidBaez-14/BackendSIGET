package co.edu.ufps.proyectosdegradoufps.historial.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistorialDTO {
    private Integer id;
    private Integer proyectoId;
    private String proyectoTitulo;
    private Integer tipoEventoId;
    private String eventoNombre;
    private String categoria;
    private String descripcion;
    private LocalDateTime fechaEvento;
    private String usuarioResponsableCedula;
    private String usuarioResponsableNombre;
}
