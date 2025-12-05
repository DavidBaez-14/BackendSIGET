package co.edu.ufps.proyectosdegradoufps.historial.services;

import co.edu.ufps.proyectosdegradoufps.historial.dtos.*;
import co.edu.ufps.proyectosdegradoufps.historial.models.*;
import co.edu.ufps.proyectosdegradoufps.historial.repositories.*;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.EstadoProyecto;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.Proyecto;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.EstadoProyectoRepository;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.ProyectoRepository;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.Usuario;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.UsuarioRepository;
import co.edu.ufps.proyectosdegradoufps.historial.repositories.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private TipoEventoHistorialRepository tipoEventoRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EstadoProyectoRepository estadoProyectoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los tipos de evento que cambian estado
     */
    public List<TipoEventoDTO> obtenerEventosCambianEstado() {
        return tipoEventoRepository.findByCambiaEstadoTrue()
                .stream()
                .map(this::convertirATipoEventoDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los tipos de evento
     */
    public List<TipoEventoDTO> obtenerTodosLosEventos() {
        return tipoEventoRepository.findAll()
                .stream()
                .map(this::convertirATipoEventoDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los estados de proyecto
     */
    public List<EstadoProyecto> obtenerTodosLosEstados() {
        return estadoProyectoRepository.findAll();
    }

    /**
     * Obtiene el historial de un proyecto
     */
    public List<HistorialDTO> obtenerHistorialPorProyecto(Integer proyectoId) {
        return historialRepository.findByProyectoIdOrderByFechaEventoDesc(proyectoId)
                .stream()
                .map(this::convertirAHistorialDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cambia el estado de un proyecto mediante un evento
     */
    public HistorialDTO cambiarEstadoProyecto(Integer proyectoId, CambioEstadoRequest request) {
        // 1. Obtener el proyecto
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado: " + proyectoId));

        // 2. Obtener el tipo de evento
        TipoEventoHistorial tipoEvento = tipoEventoRepository.findById(request.getTipoEventoId())
                .orElseThrow(() -> new RuntimeException("Tipo de evento no encontrado: " + request.getTipoEventoId()));

        // 3. Verificar que el evento cambia estado
        if (!tipoEvento.getCambiaEstado()) {
            throw new RuntimeException("El evento seleccionado no cambia el estado del proyecto");
        }

        // 4. Obtener el nuevo estado
        EstadoProyecto nuevoEstado = tipoEvento.getEstadoResultante();
        if (nuevoEstado == null) {
            throw new RuntimeException("El evento no tiene un estado resultante definido");
        }

        // 5. Obtener el usuario responsable (opcional)
        Usuario usuarioResponsable = null;
        if (request.getUsuarioResponsableCedula() != null && !request.getUsuarioResponsableCedula().isEmpty()) {
            usuarioResponsable = usuarioRepository.findById(request.getUsuarioResponsableCedula()).orElse(null);
        }

        // 6. Crear registro en historial
        Historial historial = new Historial();
        historial.setProyecto(proyecto);
        historial.setTipoEvento(tipoEvento);
        historial.setDescripcion(request.getDescripcion() != null ? request.getDescripcion() : tipoEvento.getNombre());
        historial.setFechaEvento(LocalDateTime.now());
        historial.setUsuarioResponsable(usuarioResponsable);

        historialRepository.save(historial);

        // 7. Actualizar el estado del proyecto
        proyecto.setEstado(nuevoEstado);
        proyecto.setFechaUltimaActualizacion(LocalDateTime.now());
        proyectoRepository.save(proyecto);

        return convertirAHistorialDTO(historial);
    }

    /**
     * Registra un evento en el historial sin cambiar el estado
     */
    public HistorialDTO registrarEvento(Integer proyectoId, CambioEstadoRequest request) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado: " + proyectoId));

        TipoEventoHistorial tipoEvento = tipoEventoRepository.findById(request.getTipoEventoId())
                .orElseThrow(() -> new RuntimeException("Tipo de evento no encontrado: " + request.getTipoEventoId()));

        Usuario usuarioResponsable = null;
        if (request.getUsuarioResponsableCedula() != null && !request.getUsuarioResponsableCedula().isEmpty()) {
            usuarioResponsable = usuarioRepository.findById(request.getUsuarioResponsableCedula()).orElse(null);
        }

        Historial historial = new Historial();
        historial.setProyecto(proyecto);
        historial.setTipoEvento(tipoEvento);
        historial.setDescripcion(request.getDescripcion() != null ? request.getDescripcion() : tipoEvento.getNombre());
        historial.setFechaEvento(LocalDateTime.now());
        historial.setUsuarioResponsable(usuarioResponsable);

        historialRepository.save(historial);

        return convertirAHistorialDTO(historial);
    }

    private TipoEventoDTO convertirATipoEventoDTO(TipoEventoHistorial te) {
        TipoEventoDTO dto = new TipoEventoDTO();
        dto.setId(te.getId());
        dto.setEvento(te.getEvento());
        dto.setNombre(te.getNombre());
        dto.setCategoria(te.getCategoria());
        dto.setCambiaEstado(te.getCambiaEstado());
        dto.setDescripcion(te.getDescripcion());
        if (te.getEstadoResultante() != null) {
            dto.setEstadoResultanteId(te.getEstadoResultante().getId());
            dto.setEstadoResultante(te.getEstadoResultante().getEstado());
        }
        return dto;
    }

    private HistorialDTO convertirAHistorialDTO(Historial h) {
        HistorialDTO dto = new HistorialDTO();
        dto.setId(h.getId());
        dto.setDescripcion(h.getDescripcion());
        dto.setFechaEvento(h.getFechaEvento());
        
        if (h.getProyecto() != null) {
            dto.setProyectoId(h.getProyecto().getId());
            dto.setProyectoTitulo(h.getProyecto().getTitulo());
        }
        
        if (h.getTipoEvento() != null) {
            dto.setTipoEventoId(h.getTipoEvento().getId());
            dto.setEventoNombre(h.getTipoEvento().getNombre());
            dto.setCategoria(h.getTipoEvento().getCategoria());
        }
        
        if (h.getUsuarioResponsable() != null) {
            dto.setUsuarioResponsableCedula(h.getUsuarioResponsable().getCedula());
            dto.setUsuarioResponsableNombre(
                h.getUsuarioResponsable().getNombres() + " " + h.getUsuarioResponsable().getApellidos()
            );
        }
        
        return dto;
    }
}
