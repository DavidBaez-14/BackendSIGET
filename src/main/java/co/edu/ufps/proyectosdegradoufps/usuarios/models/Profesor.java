package co.edu.ufps.proyectosdegradoufps.usuarios.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "profesores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Profesor extends Usuario {
    
    @Column(name = "titulo_academico", nullable = false, length = 50)
    private String tituloAcademico;
    
    @Column(nullable = false, length = 100)
    private String especialidad;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_profesor", nullable = false, length = 20)
    private TipoProfesor tipoProfesor;
    
    @Column(name = "fecha_vinculacion", nullable = false)
    private LocalDate fechaVinculacion;
    
    public enum TipoProfesor {
        PLANTA,
        CATEDRATICO,
        OCASIONAL
    }
}