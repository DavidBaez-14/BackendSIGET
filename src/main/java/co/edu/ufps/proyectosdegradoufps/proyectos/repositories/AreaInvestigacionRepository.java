package co.edu.ufps.proyectosdegradoufps.proyectos.repositories;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.AreaInvestigacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaInvestigacionRepository extends JpaRepository<AreaInvestigacion, Integer> {
}
