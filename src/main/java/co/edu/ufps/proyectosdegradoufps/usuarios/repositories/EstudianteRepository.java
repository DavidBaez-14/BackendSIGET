package co.edu.ufps.proyectosdegradoufps.usuarios.repositories;

import co.edu.ufps.proyectosdegradoufps.usuarios.models.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String> {
    
    Estudiante findByCodigoEstudiantil(String codigoEstudiantil);
    
    List<Estudiante> findByProgramaCodigo(String programaCodigo);
    
    boolean existsByCodigoEstudiantil(String codigoEstudiantil);
}