package co.edu.ufps.proyectosdegradoufps.proyectos.services;

import co.edu.ufps.proyectosdegradoufps.proyectos.dtos.ProyectoDTO;
import co.edu.ufps.proyectosdegradoufps.proyectos.models.*;
import co.edu.ufps.proyectosdegradoufps.proyectos.repositories.*;
import co.edu.ufps.proyectosdegradoufps.usuarios.dtos.EstudianteDTO;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.Administrador;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.DirectorExterno;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.Estudiante;
import co.edu.ufps.proyectosdegradoufps.usuarios.models.Profesor;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.AdministradorRepository;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.DirectorExternoRepository;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.EstudianteRepository;
import co.edu.ufps.proyectosdegradoufps.usuarios.repositories.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EstudianteProyectoRepository estudianteProyectoRepository;

    @Autowired
    private ModalidadProyectoRepository modalidadProyectoRepository;

    @Autowired
    private LineaInvestigacionRepository lineaInvestigacionRepository;

    @Autowired
    private EstadoProyectoRepository estadoProyectoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private DirectorExternoRepository directorExternoRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private ComiteRepository comiteRepository;

    public ProyectoDTO crearProyecto(ProyectoDTO dto, String cedulaLider) {
        // 1. Validar que el estudiante líder exista
        Estudiante lider = estudianteRepository.findById(cedulaLider)
                .orElseThrow(() -> new RuntimeException("Estudiante líder no encontrado: " + cedulaLider));

        // 2. Validar que el estudiante líder no tenga proyecto activo
        validarEstudianteSinProyectoActivo(cedulaLider);

        // 3. Validar estudiantes invitados (máximo 2)
        List<String> invitados = dto.getEstudiantesCedulas();
        if (invitados != null && invitados.size() > 2) {
            throw new RuntimeException("Máximo 2 estudiantes adicionales permitidos.");
        }
        if (invitados != null) {
            for (String cedula : invitados) {
                if (!cedula.equals(cedulaLider)) {
                    validarEstudianteSinProyectoActivo(cedula);
                }
            }
        }

        // 4. Crear entidad Proyecto
        Proyecto proyecto = new Proyecto();
        proyecto.setTitulo(dto.getTitulo());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setObjetivoGeneral(dto.getObjetivoGeneral());
        
        // Generar código único
        String codigoUnico = "PROY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        proyecto.setCodigoProyecto(codigoUnico);
        
        // Fechas iniciales
        proyecto.setFechaPresentacion(LocalDate.now());
        proyecto.setFechaUltimaActualizacion(LocalDateTime.now());
        proyecto.setPorcentajeAvance(0);

        // 5. Asignar relaciones
        ModalidadProyecto modalidad = modalidadProyectoRepository.findById(dto.getModalidadId())
                .orElseThrow(() -> new RuntimeException("Modalidad no encontrada con ID: " + dto.getModalidadId()));
        proyecto.setModalidad(modalidad);

        LineaInvestigacion linea = lineaInvestigacionRepository.findById(dto.getLineaInvestigacionId())
                .orElseThrow(() -> new RuntimeException("Línea de investigación no encontrada con ID: " + dto.getLineaInvestigacionId()));
        proyecto.setLineaInvestigacion(linea);

        EstadoProyecto estado = estadoProyectoRepository.findByEstado("REGISTRADO")
                .orElseThrow(() -> new RuntimeException("Estado REGISTRADO no encontrado en la base de datos"));
        proyecto.setEstado(estado);

        // 6. Asignar Director
        String tipoDirector = dto.getTipoDirector();
        if (tipoDirector == null || tipoDirector.isEmpty()) {
            tipoDirector = "PROFESOR"; // Por defecto
        }
        
        if ("PROFESOR".equalsIgnoreCase(tipoDirector)) {
            Profesor profesor = profesorRepository.findById(dto.getDirectorCedula())
                    .orElseThrow(() -> new RuntimeException("Profesor director no encontrado: " + dto.getDirectorCedula()));
            proyecto.setDirectorProfesor(profesor);
        } else if ("EXTERNO".equalsIgnoreCase(tipoDirector)) {
            DirectorExterno externo = directorExternoRepository.findById(dto.getDirectorCedula())
                    .orElseThrow(() -> new RuntimeException("Director externo no encontrado: " + dto.getDirectorCedula()));
            proyecto.setDirectorExterno(externo);
        } else {
            throw new RuntimeException("Tipo de director inválido: " + tipoDirector + ". Use PROFESOR o EXTERNO.");
        }

        // 7. Guardar Proyecto
        proyecto = proyectoRepository.save(proyecto);

        // 8. Vincular Estudiante Líder
        vincularEstudiante(proyecto, cedulaLider);
        
        // 9. Vincular estudiantes invitados
        if (invitados != null) {
            for (String cedula : invitados) {
                if (!cedula.equals(cedulaLider)) {
                    vincularEstudiante(proyecto, cedula);
                }
            }
        }

        // 10. Recargar proyecto con relaciones para el DTO
        proyecto = proyectoRepository.findById(proyecto.getId()).orElse(proyecto);
        
        return conversionDTO(proyecto);
    }

    private void validarEstudianteSinProyectoActivo(String cedula) {
        Optional<EstudianteProyecto> activo = estudianteProyectoRepository.findByEstudianteCedulaAndActivoTrue(cedula);
        if (activo.isPresent()) {
            throw new RuntimeException("El estudiante con cédula " + cedula + " ya tiene un proyecto activo.");
        }
    }

    private void vincularEstudiante(Proyecto proyecto, String cedula) {
        Estudiante estudiante = estudianteRepository.findById(cedula)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + cedula));
        
        EstudianteProyecto vinculacion = new EstudianteProyecto();
        vinculacion.setProyecto(proyecto);
        vinculacion.setEstudiante(estudiante);
        vinculacion.setActivo(true);
        //vinculacion.setFechaVinculacion(LocalDate.now());
        estudianteProyectoRepository.save(vinculacion);
    }

    public List<ProyectoDTO> listarProyectosPorDirector(String cedula) {
        // Buscar como profesor
        List<Proyecto> proyectosProfesor = proyectoRepository.findByDirectorProfesorCedula(cedula);
        if (!proyectosProfesor.isEmpty()) {
            return proyectosProfesor.stream().map(this::conversionDTO).collect(Collectors.toList());
        }
        
        // Buscar como externo
        List<Proyecto> proyectosExterno = proyectoRepository.findByDirectorExternoCedula(cedula);
        return proyectosExterno.stream().map(this::conversionDTO).collect(Collectors.toList());
    }
    
    public ProyectoDTO obtenerProyectoActivoPorEstudiante(String cedula) {
        Proyecto proyecto = proyectoRepository.findProyectoActivoByEstudianteCedula(cedula);
        if (proyecto == null) {
            return null;
        }
        return conversionDTO(proyecto);
    }

    public List<ProyectoDTO> listarTodosLosProyectos() {
        return proyectoRepository.findAll()
                .stream()
                .map(this::conversionDTO)
                .collect(Collectors.toList());
    }

    public ProyectoDTO obtenerProyectoPorId(Integer id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado: " + id));
        return conversionDTO(proyecto);
    }

    /**
     * Lista proyectos filtrados según el administrador.
     * Si es admin general (es_admin_general = true) → devuelve todos los proyectos.
     * Si no → devuelve solo proyectos de estudiantes del programa de su comité.
     */
    public List<ProyectoDTO> listarProyectosPorAdministrador(String cedulaAdmin) {
        // 1. Buscar el administrador por cédula del profesor
        Administrador admin = administradorRepository.findByProfesorCedula(cedulaAdmin);
        if (admin == null) {
            throw new RuntimeException("Administrador no encontrado con cédula: " + cedulaAdmin);
        }

        // 2. Si es admin general, devolver todos los proyectos
        if (admin.getEsAdminGeneral() != null && admin.getEsAdminGeneral()) {
            return listarTodosLosProyectos();
        }

        // 3. Si no es admin general, filtrar por programa del comité
        if (admin.getComiteId() == null) {
            throw new RuntimeException("El administrador no tiene un comité asignado");
        }

        Comite comite = comiteRepository.findById(admin.getComiteId())
                .orElseThrow(() -> new RuntimeException("Comité no encontrado: " + admin.getComiteId()));

        String programaCodigo = comite.getProgramaCodigo();
        
        List<Proyecto> proyectos = proyectoRepository.findByEstudiantesProgramaCodigo(programaCodigo);
        return proyectos.stream().map(this::conversionDTO).collect(Collectors.toList());
    }

    /**
     * Obtiene información del administrador para el frontend
     */
    public AdministradorInfoDTO obtenerInfoAdministrador(String cedulaAdmin) {
        Administrador admin = administradorRepository.findByProfesorCedula(cedulaAdmin);
        if (admin == null) {
            return null;
        }

        AdministradorInfoDTO info = new AdministradorInfoDTO();
        info.setCedula(cedulaAdmin);
        info.setCargo(admin.getCargo());
        info.setEsAdminGeneral(admin.getEsAdminGeneral() != null && admin.getEsAdminGeneral());

        if (admin.getProfesor() != null) {
            info.setNombreCompleto(admin.getProfesor().getNombres() + " " + admin.getProfesor().getApellidos());
        }

        if (admin.getComiteId() != null) {
            Comite comite = comiteRepository.findById(admin.getComiteId()).orElse(null);
            if (comite != null) {
                info.setComiteNombre(comite.getNombre());
                info.setProgramaCodigo(comite.getProgramaCodigo());
            }
        }

        return info;
    }

    // DTO interno para info del administrador
    @lombok.Data
    public static class AdministradorInfoDTO {
        private String cedula;
        private String nombreCompleto;
        private String cargo;
        private Boolean esAdminGeneral;
        private String comiteNombre;
        private String programaCodigo;
    }

    private ProyectoDTO conversionDTO(Proyecto p) {
        ProyectoDTO dto = new ProyectoDTO();
        dto.setId(p.getId());
        dto.setCodigoProyecto(p.getCodigoProyecto());
        dto.setTitulo(p.getTitulo());
        dto.setDescripcion(p.getDescripcion());
        dto.setObjetivoGeneral(p.getObjetivoGeneral());
        
        if (p.getModalidad() != null) {
            dto.setModalidadId(p.getModalidad().getId());
            dto.setModalidadNombre(p.getModalidad().getNombre());
        }
        
        if (p.getLineaInvestigacion() != null) {
            dto.setLineaInvestigacionId(p.getLineaInvestigacion().getId());
            dto.setLineaInvestigacionNombre(p.getLineaInvestigacion().getNombre());
        }
        
        if (p.getEstado() != null) {
            dto.setEstado(p.getEstado().getEstado());
            dto.setFase(p.getEstado().getFase());
        }
        
        if (p.getDirectorProfesor() != null) {
            dto.setDirectorCedula(p.getDirectorProfesor().getCedula());
            dto.setDirectorNombre(p.getDirectorProfesor().getNombres() + " " + p.getDirectorProfesor().getApellidos());
            dto.setTipoDirector("PROFESOR");
        } else if (p.getDirectorExterno() != null) {
            dto.setDirectorCedula(p.getDirectorExterno().getCedula());
            dto.setDirectorNombre(p.getDirectorExterno().getNombres() + " " + p.getDirectorExterno().getApellidos());
            dto.setTipoDirector("EXTERNO");
        }
        
        dto.setPorcentajeAvance(p.getPorcentajeAvance());
        dto.setFechaPresentacion(p.getFechaPresentacion());
        dto.setFechaInicioDesarrollo(p.getFechaInicioDesarrollo());
        dto.setFechaFinEstimada(p.getFechaFinEstimada());
        dto.setFechaSustentacion(p.getFechaSustentacion());
        dto.setFechaUltimaActualizacion(p.getFechaUltimaActualizacion());
        dto.setNotaFinal(p.getNotaFinal());
        dto.setResultadoFinal(p.getResultadoFinal());
        dto.setDocumentoPropuestaUrl(p.getDocumentoPropuestaUrl());
        dto.setDocumentoFinalUrl(p.getDocumentoFinalUrl());
        
        // Cargar estudiantes vinculados
        List<EstudianteProyecto> vinculaciones = estudianteProyectoRepository.findByProyectoId(p.getId());
        if (vinculaciones != null && !vinculaciones.isEmpty()) {
            List<EstudianteDTO> estudiantesDTO = vinculaciones.stream()
                    .filter(ep -> ep.getActivo() != null && ep.getActivo())
                    .map(ep -> {
                        Estudiante e = ep.getEstudiante();
                        EstudianteDTO eDto = new EstudianteDTO();
                        eDto.setCedula(e.getCedula());
                        eDto.setNombres(e.getNombres());
                        eDto.setApellidos(e.getApellidos());
                        eDto.setEmail(e.getEmail());
                        eDto.setProgramaCodigo(e.getProgramaCodigo());
                        return eDto;
                    })
                    .collect(Collectors.toList());
            dto.setEstudiantes(estudiantesDTO);
        }
        
        return dto;
    }
}