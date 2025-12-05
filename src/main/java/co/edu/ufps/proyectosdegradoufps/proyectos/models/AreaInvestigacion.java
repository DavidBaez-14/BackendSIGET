package co.edu.ufps.proyectosdegradoufps.proyectos.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "areas_investigacion")
@Data
public class AreaInvestigacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "areaInvestigacion")
    @JsonIgnore
    private List<LineaInvestigacion> lineas;
}
