package co.edu.ufps.proyectosdegradoufps.proyectos.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "comites")
@Data
public class Comite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "programa_codigo", nullable = false, length = 10)
    private String programaCodigo;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
}
