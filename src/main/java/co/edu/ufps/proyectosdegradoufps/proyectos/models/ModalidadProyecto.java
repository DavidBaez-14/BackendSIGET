package co.edu.ufps.proyectosdegradoufps.proyectos.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "modalidades_proyecto")
@Data
public class ModalidadProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "tipo_proyecto_id", nullable = false)
    private TipoProyecto tipoProyecto;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
