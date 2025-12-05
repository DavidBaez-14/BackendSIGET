package co.edu.ufps.proyectosdegradoufps.proyectos.repositories;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.EstudianteProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteProyectoRepository extends JpaRepository<EstudianteProyecto, Integer> {
    
    Optional<EstudianteProyecto> findByEstudianteCedulaAndActivoTrue(String cedula);
    
    List<EstudianteProyecto> findByProyectoId(Integer proyectoId);
    
    List<EstudianteProyecto> findByEstudianteCedula(String cedula);
}