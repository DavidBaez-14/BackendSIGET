package co.edu.ufps.proyectosdegradoufps.proyectos.models;

import co.edu.ufps.proyectosdegradoufps.usuarios.models.DirectorExterno;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.Profesor;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "proyectos")
@Data
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_proyecto", unique = true, nullable = false, length = 20)
    private String codigoProyecto;

    @Column(nullable = false, length = 300)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "objetivo_general", columnDefinition = "TEXT")
    private String objetivoGeneral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modalidad_id", nullable = false)
    private ModalidadProyecto modalidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_investigacion_id", nullable = false)
    private LineaInvestigacion lineaInvestigacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoProyecto estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_profesor_cedula", referencedColumnName = "cedula")
    private Profesor directorProfesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_externo_cedula", referencedColumnName = "cedula")
    private DirectorExterno directorExterno;

    @Column(name = "porcentaje_avance")
    private Integer porcentajeAvance = 0;

    @Column(name = "fecha_presentacion")
    private LocalDate fechaPresentacion;

    @Column(name = "fecha_inicio_desarrollo")
    private LocalDate fechaInicioDesarrollo;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Column(name = "fecha_sustentacion")
    private LocalDate fechaSustentacion;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @Column(name = "nota_final", precision = 3, scale = 2)
    private BigDecimal notaFinal;

    @Column(name = "resultado_final", length = 20)
    private String resultadoFinal;

    @Column(name = "documento_propuesta_url")
    private String documentoPropuestaUrl;

    @Column(name = "documento_final_url")
    private String documentoFinalUrl;

    @Column(name = "acta_sustentacion", length = 50)
    private String actaSustentacion;

    @OneToMany(mappedBy = "proyecto", fetch = FetchType.LAZY)
    private List<EstudianteProyecto> estudiantesVinculados;

    @PrePersist
    protected void onCreate() {
        if (fechaUltimaActualizacion == null) {
            fechaUltimaActualizacion = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }
        if (fechaPresentacion == null) {
            fechaPresentacion = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaUltimaActualizacion = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}