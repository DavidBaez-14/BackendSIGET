package co.edu.ufps.proyectosdegradoufps.proyectos.repositories;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.LineaInvestigacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineaInvestigacionRepository extends JpaRepository<LineaInvestigacion, Integer> {
    List<LineaInvestigacion> findByAreaInvestigacionId(Integer areaId);
}
