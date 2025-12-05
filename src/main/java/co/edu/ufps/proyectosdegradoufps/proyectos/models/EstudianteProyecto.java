package co.edu.ufps.proyectosdegradoufps.proyectos.models;

import co.edu.ufps.proyectosdegradoufps.usuarios.models.Estudiante;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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
    
    @Column(name = "fecha_vinculacion", insertable = false, updatable = false)
    private LocalDateTime fechaVinculacion;
    
    private Boolean activo = true;
}
