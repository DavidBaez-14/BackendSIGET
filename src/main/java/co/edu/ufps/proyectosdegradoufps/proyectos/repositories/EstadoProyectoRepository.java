package co.edu.ufps.proyectosdegradoufps.proyectos.repositories;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.EstadoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadoProyectoRepository extends JpaRepository<EstadoProyecto, Integer> {
    Optional<EstadoProyecto> findByEstado(String estado);
}
