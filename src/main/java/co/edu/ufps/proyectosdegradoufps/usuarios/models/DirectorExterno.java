package co.edu.ufps.proyectosdegradoufps.usuarios.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "directores_externos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DirectorExterno extends Usuario {
    
    @Column(name = "titulo_academico", nullable = false, length = 50)
    private String tituloAcademico;
    
    @Column(nullable = false, length = 100)
    private String especialidad;
    
    @Column(name = "institucion_procedencia", nullable = false, length = 150)
    private String institucion;
    
    @Column(name = "acta_aprobacion_comite", length = 50)
    private String actaAprobacionComite;
    
    @Column(name = "fecha_aprobacion")
    private LocalDate fechaAprobacion;
    
    // Supabase DDL usa 'aprobado_comite' (NOT NULL, default false)
    @Column(name = "aprobado_comite", nullable = false)
    private Boolean aprobado = false;
}