package co.edu.ufps.proyectosdegradoufps.notificaciones.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {
    private Integer id;
    private String usuarioCedula;
    private String titulo;
    private String mensaje;
    private String enlace;
    private Boolean leida;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLectura;
    private String tipo;
    private Map<String, Object> metadata; // Será parseado desde JSON
}
