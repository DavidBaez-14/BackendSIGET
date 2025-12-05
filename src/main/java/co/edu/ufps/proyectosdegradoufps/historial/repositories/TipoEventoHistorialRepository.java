package co.edu.ufps.proyectosdegradoufps.historial.repositories;

import co.edu.ufps.proyectosdegradoufps.historial.models.TipoEventoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoEventoHistorialRepository extends JpaRepository<TipoEventoHistorial, Integer> {
    Optional<TipoEventoHistorial> findByEvento(String evento);
    List<TipoEventoHistorial> findByCategoria(String categoria);
    List<TipoEventoHistorial> findByCambiaEstadoTrue();
    List<TipoEventoHistorial> findByEstadoResultanteId(Integer estadoId);
}