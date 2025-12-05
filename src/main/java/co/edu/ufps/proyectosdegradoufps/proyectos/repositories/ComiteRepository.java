package co.edu.ufps.proyectosdegradoufps.proyectos.repositories;

import co.edu.ufps.proyectosdegradoufps.proyectos.models.Comite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComiteRepository extends JpaRepository<Comite, Integer> {
    Comite findByProgramaCodigo(String programaCodigo);
}
