package co.edu.ufps.proyectosdegradoufps.usuarios.repositories;

import co.edu.ufps.proyectosdegradoufps.usuarios.models.DirectorExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DirectorExternoRepository extends JpaRepository<DirectorExterno, String> {
    
    List<DirectorExterno> findByAprobado(Boolean aprobado);
}