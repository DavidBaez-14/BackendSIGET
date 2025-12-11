package co.edu.ufps.proyectosdegradoufps.reuniones.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrarReunionRequest {
    private Integer reunionId;
    private String acuerdos;
    private LocalDate proximaReunion;
    private String observaciones;
}
