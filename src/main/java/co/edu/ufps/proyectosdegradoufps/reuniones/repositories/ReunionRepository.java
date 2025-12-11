package co.edu.ufps.proyectosdegradoufps.reuniones.repositories;

import co.edu.ufps.proyectosdegradoufps.reuniones.models.Reunion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReunionRepository extends JpaRepository<Reunion, Integer> {

    // Obtener todas las reuniones de un proyecto
    List<Reunion> findByProyectoIdOrderByFechaReunionDesc(Integer proyectoId);

    // Obtener todas las reuniones donde participa un director (por sus proyectos)
    @Query("SELECT r FROM Reunion r WHERE r.proyecto.directorProfesor.cedula = :cedulaDirector OR r.proyecto.directorExterno.cedula = :cedulaDirector ORDER BY r.fechaReunion DESC")
    List<Reunion> findReunionesDeDirector(@Param("cedulaDirector") String cedulaDirector);

    // Obtener todas las reuniones donde participa un estudiante (por sus proyectos)
    @Query("SELECT r FROM Reunion r JOIN r.proyecto.estudiantesVinculados ev WHERE ev.estudiante.cedula = :cedulaEstudiante AND ev.activo = true ORDER BY r.fechaReunion DESC")
    List<Reunion> findReunionesDeEstudiante(@Param("cedulaEstudiante") String cedulaEstudiante);
}
