package co.edu.ufps.proyectosdegradoufps.usuarios.repositories;

import co.edu.ufps.proyectosdegradoufps.usuarios.models.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, String> {
    
    List<Profesor> findByTipoProfesor(Profesor.TipoProfesor tipoProfesor);
    
    @Query("SELECT p FROM Profesor p WHERE " +
           "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.especialidad) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Profesor> buscarPorNombreOEspecialidad(@Param("busqueda") String busqueda);
}