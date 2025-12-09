package co.edu.ufps.proyectosdegradoufps.notificaciones.dtos;

public class DirectorDTO {
    private String cedula;
    private String nombre;
    private String tituloAcademico;
    private String especialidad;
    private String tipoDirector; // "PROFESOR" o "EXTERNO"
    private String tipoProfesor; // Solo si es profesor: PLANTA, CATEDRATICO, OCASIONAL
    private String institucion; // Solo si es externo
    
    // Getters y Setters
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTituloAcademico() {
        return tituloAcademico;
    }
    
    public void setTituloAcademico(String tituloAcademico) {
        this.tituloAcademico = tituloAcademico;
    }
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public String getTipoDirector() {
        return tipoDirector;
    }
    
    public void setTipoDirector(String tipoDirector) {
        this.tipoDirector = tipoDirector;
    }
    
    public String getTipoProfesor() {
        return tipoProfesor;
    }
    
    public void setTipoProfesor(String tipoProfesor) {
        this.tipoProfesor = tipoProfesor;
    }
    
    public String getInstitucion() {
        return institucion;
    }
    
    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }
}
