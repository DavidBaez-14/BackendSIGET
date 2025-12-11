package co.edu.ufps.proyectosdegradoufps.historial.models;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.Proyecto;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "historiales")
@Data
public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;
    
    @ManyToOne
    @JoinColumn(name = "tipo_evento_id", nullable = false)
    private TipoEventoHistorial tipoEvento;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;
    
    @ManyToOne
    @JoinColumn(name = "usuario_responsable_cedula")
    private Usuario usuarioResponsable;
    
    @PrePersist
    protected void onCreate() {
        if (fechaEvento == null) {
            fechaEvento = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }
    }
}
