package co.edu.ufps.proyectosdegradoufps.notificaciones.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponderInvitacionRequest {
    private String respuesta; // "ACEPTADA" o "RECHAZADA"
}
