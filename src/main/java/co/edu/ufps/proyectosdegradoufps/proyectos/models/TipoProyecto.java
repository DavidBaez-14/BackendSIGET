package co.edu.ufps.proyectosdegradoufps.proyectos.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tipos_proyecto")
@Data
public class TipoProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 50)
    private String tipo;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @OneToMany(mappedBy = "tipoProyecto")
    @JsonIgnore
    private List<ModalidadProyecto> modalidades;
}
