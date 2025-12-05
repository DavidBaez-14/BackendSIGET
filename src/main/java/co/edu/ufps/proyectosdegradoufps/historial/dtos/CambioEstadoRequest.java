package co.edu.ufps.proyectosdegradoufps.historial.dtos;

import lombok.Data;

@Data
public class CambioEstadoRequest {
    private Integer tipoEventoId;
    private String descripcion;
    private String usuarioResponsableCedula;
}
