package co.edu.ufps.proyectosdegradoufps.historial.repositories;

import co.edu.ufps.proyectosdegradoufps.historial.models.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Integer> {
    List<Historial> findByProyectoIdOrderByFechaEventoDesc(Integer proyectoId);
    List<Historial> findByUsuarioResponsableCedulaOrderByFechaEventoDesc(String cedula);
}
