package co.edu.ufps.proyectosdegradoufps.reuniones.dtos;

import lombok.Data;

@Data
public class ResponderReunionRequest {
    private Boolean aceptada; // true = aceptar, false = rechazar
    private String motivoRechazo; // Opcional, solo si aceptada = false
}
