package co.edu.ufps.proyectosdegradoufps.notificaciones.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "usuario_cedula", nullable = false)
    private String usuarioCedula;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;
    
    @Column
    private String enlace;
    
    @Column(nullable = false)
    private Boolean leida = false;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;
    
    @Column
    private String tipo = "GENERAL"; // GENERAL, INVITACION_PROYECTO, CAMBIO_ESTADO, REUNION
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String metadata; // JSON string para almacenar información adicional
}
