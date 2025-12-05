package co.edu.ufps.proyectosdegradoufps.usuarios.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Estudiante extends Usuario {
    
    @Column(name = "codigo_estudiantil", unique = true, nullable = false, length = 20)
    private String codigoEstudiantil;
    
    @Column(name = "programa_codigo", nullable = false, length = 10)
    private String programaCodigo;
    
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;
}