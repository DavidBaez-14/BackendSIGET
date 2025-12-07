package co.edu.ufps.proyectosdegradoufps.notificaciones.repositories;

import co.edu.ufps.proyectosdegradoufps.notificaciones.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    
    // Obtener todas las notificaciones de un usuario
    List<Notificacion> findByUsuarioCedulaOrderByFechaCreacionDesc(String usuarioCedula);
    
    // Obtener notificaciones no leídas de un usuario
    List<Notificacion> findByUsuarioCedulaAndLeidaOrderByFechaCreacionDesc(String usuarioCedula, Boolean leida);
    
    // Contar notificaciones no leídas
    Long countByUsuarioCedulaAndLeida(String usuarioCedula, Boolean leida);
    
    // Obtener notificaciones por tipo
    List<Notificacion> findByUsuarioCedulaAndTipoOrderByFechaCreacionDesc(String usuarioCedula, String tipo);
    
    // Buscar invitación pendiente específica
    @Query(value = "SELECT * FROM notificaciones n WHERE n.usuario_cedula = :cedula " +
           "AND n.tipo = 'INVITACION_PROYECTO' " +
           "AND n.leida = false " +
           "AND n.metadata::jsonb->>'proyectoId' = :proyectoId " +
           "AND n.metadata::jsonb->>'estado' = 'PENDIENTE'", nativeQuery = true)
    Optional<Notificacion> findInvitacionPendiente(@Param("cedula") String cedula, @Param("proyectoId") String proyectoId);
}
