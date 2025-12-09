package co.edu.ufps.proyectosdegradoufps.proyectos.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lineas_investigacion")
@Data
public class LineaInvestigacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private AreaInvestigacion areaInvestigacion;
    
    @Column(nullable = false)
    private Boolean activo = true;
}
