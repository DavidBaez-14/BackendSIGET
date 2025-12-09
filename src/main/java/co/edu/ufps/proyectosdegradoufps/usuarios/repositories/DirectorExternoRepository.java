package co.edu.ufps.proyectosdegradoufps.usuarios.repositories;

import co.edu.ufps.proyectosdegradoufps.usuarios.models.DirectorExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DirectorExternoRepository extends JpaRepository<DirectorExterno, String> {
    
    List<DirectorExterno> findByAprobado(Boolean aprobado);
    
    @Query("SELECT d FROM DirectorExterno d WHERE d.aprobado = true AND (" +
           "LOWER(d.nombres) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(d.apellidos) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(d.especialidad) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(d.institucion) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<DirectorExterno> buscarPorNombreOEspecialidad(@Param("busqueda") String busqueda);
    
    List<DirectorExterno> findByAprobadoTrue();
}