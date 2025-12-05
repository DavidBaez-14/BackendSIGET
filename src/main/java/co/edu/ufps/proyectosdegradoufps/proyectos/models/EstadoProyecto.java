package co.edu.ufps.proyectosdegradoufps.proyectos.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "estados_proyecto")
@Data
public class EstadoProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 50, unique = true)
    private String estado;
    
    @Column(nullable = false, length = 50)
    private String fase;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private Integer orden;
}
