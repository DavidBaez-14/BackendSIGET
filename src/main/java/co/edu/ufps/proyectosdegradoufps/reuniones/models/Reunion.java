package co.edu.ufps.proyectosdegradoufps.reuniones.models;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.Proyecto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "reuniones")
@Data
public class Reunion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @Column(name = "fecha_reunion", nullable = false)
    private LocalDateTime fechaReunion;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(nullable = false, length = 50)
    private String tipo; // PRESENCIAL, VIRTUAL, CORREO, TELEFONICA

    @Column(name = "temas_tratados", columnDefinition = "TEXT", nullable = false)
    private String temasTratados;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String acuerdos;

    @Column(name = "proxima_reunion")
    private LocalDate proximaReunion;

    @Column(name = "asistio_estudiante", nullable = false)
    private Boolean asistioEstudiante = true; // Por defecto true hasta que se marque lo contrario

    // Solo el director marca si el estudiante asistió o no
    // @Column(name = "asistio_director")
    // private Boolean asistioDirector = true;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // CAMPOS QUE NO EXISTEN EN LA BASE DE DATOS - COMENTADOS
    // @Column(name = "confirmada")
    // private Boolean confirmada = false;
    // @Column(name = "solicitante_cedula")
    // private String solicitanteCedula;
    // @Column(name = "fecha_creacion")
    // private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaReunion != null) {
            fechaReunion = fechaReunion.truncatedTo(ChronoUnit.SECONDS);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (fechaReunion != null) {
            fechaReunion = fechaReunion.truncatedTo(ChronoUnit.SECONDS);
        }
    }
}
