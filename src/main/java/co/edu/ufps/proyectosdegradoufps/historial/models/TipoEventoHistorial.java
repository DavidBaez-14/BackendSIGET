package co.edu.ufps.proyectosdegradoufps.historial.models;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.EstadoProyecto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tipos_evento_historial")
@Data
public class TipoEventoHistorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100, unique = true)
    private String evento;
    
    @Column(nullable = false, length = 150)
    private String nombre;
    
    @Column(length = 50)
    private String categoria;
    
    @Column(name = "cambia_estado", nullable = false)
    private Boolean cambiaEstado = false;
    
    @ManyToOne
    @JoinColumn(name = "estado_resultante_id")
    private EstadoProyecto estadoResultante;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private Integer orden;
}
