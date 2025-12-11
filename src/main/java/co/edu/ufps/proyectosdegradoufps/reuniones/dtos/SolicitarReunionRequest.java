package co.edu.ufps.proyectosdegradoufps.reuniones.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitarReunionRequest {
    private Integer proyectoId;
    private String solicitanteCedula; // Quién solicita (estudiante o director)
    private String receptorCedula; // A quién se le solicita
    private LocalDateTime fechaReunion;
    private Integer duracionMinutos;
    private String tipo; // PRESENCIAL, VIRTUAL, CORREO, TELEFONICA
    private String temasPropuestos;
    private String observaciones;
}
