package co.edu.ufps.proyectosdegradoufps.proyectos.repositories;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.ModalidadProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModalidadProyectoRepository extends JpaRepository<ModalidadProyecto, Integer> {
}
