package co.edu.ufps.proyectosdegradoufps.proyectos.models;

import co.edu.ufps.proyectosdegradoufps.usuarios.models.Estudiante;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "estudiantes_proyecto")
@Data
public class EstudianteProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;
    
    @ManyToOne
    @JoinColumn(name = "estudiante_cedula", nullable = false)
    private Estudiante estudiante;
    
    @Column(name = "fecha_vinculacion")
    private LocalDateTime fechaVinculacion;
    
    private Boolean activo = true;
    
    @PrePersist
    @PreUpdate
    protected void onCreate() {
        if (fechaVinculacion == null) {
            fechaVinculacion = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }
    }
}
