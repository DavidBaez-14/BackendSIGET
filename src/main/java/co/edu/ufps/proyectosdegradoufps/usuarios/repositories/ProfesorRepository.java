package co.edu.ufps.proyectosdegradoufps.usuarios.repositories;

import co.edu.ufps.proyectosdegradoufps.usuarios.models.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, String> {
    
    List<Profesor> findByTipoProfesor(Profesor.TipoProfesor tipoProfesor);
}