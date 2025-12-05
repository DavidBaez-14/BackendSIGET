package co.edu.ufps.proyectosdegradoufps.proyectos.repositories;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    
    List<Proyecto> findByDirectorProfesorCedula(String cedula);
    
    List<Proyecto> findByDirectorExternoCedula(String cedula);
    
    @Query("SELECT p FROM Proyecto p JOIN p.estudiantesVinculados ep WHERE ep.estudiante.cedula = :cedula AND ep.activo = true")
    Proyecto findProyectoActivoByEstudianteCedula(@Param("cedula") String cedula);
    
    @Query("SELECT DISTINCT p FROM Proyecto p JOIN p.estudiantesVinculados ep WHERE ep.estudiante.programaCodigo = :programaCodigo AND ep.activo = true")
    List<Proyecto> findByEstudiantesProgramaCodigo(@Param("programaCodigo") String programaCodigo);
}
