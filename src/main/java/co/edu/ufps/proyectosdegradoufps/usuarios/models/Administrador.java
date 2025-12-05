package co.edu.ufps.proyectosdegradoufps.usuarios.models;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.Comite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "administradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "profesor_cedula", length = 20, nullable = false)
    private String profesorCedula;
    
    @Column(length = 50, nullable = false)
    private String cargo;
    
    // En Supabase DDL: fecha_inicio_cargo (NOT NULL)
    @Column(name = "fecha_inicio_cargo", nullable = false)
    private LocalDate fechaInicio;
    
    // En Supabase DDL: fecha_fin_cargo (NULLABLE)
    @Column(name = "fecha_fin_cargo")
    private LocalDate fechaFin;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    // Nuevos campos para filtrado por programa
    @Column(name = "comite_id")
    private Integer comiteId;
    
    @Column(name = "es_admin_general")
    private Boolean esAdminGeneral = false;
    
    // Relación con Profesor (1:1)
    @OneToOne
    @JoinColumn(name = "profesor_cedula", referencedColumnName = "cedula", insertable = false, updatable = false)
    private Profesor profesor;
    
    // Relación con Comité
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comite_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Comite comite;
}