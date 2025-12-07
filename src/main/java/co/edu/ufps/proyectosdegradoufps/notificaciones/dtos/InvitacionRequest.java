package co.edu.ufps.proyectosdegradoufps.notificaciones.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitacionRequest {
    private Integer proyectoId;
    private String estudianteInvitadoCedula;
    private String invitanteCedula;
    private String invitanteNombre;
    private String tituloProyecto;
}
