package co.edu.ufps.proyectosdegradoufps.notificaciones.dtos;

public class InvitacionDirectorRequest {
    private Integer proyectoId;
    private String directorCedula;
    private String tipoDirector; // "PROFESOR" o "EXTERNO"
    private String cedulaEstudiante; // Quien envía la invitación
    
    // Getters y Setters
    public Integer getProyectoId() {
        return proyectoId;
    }
    
    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }
    
    public String getDirectorCedula() {
        return directorCedula;
    }
    
    public void setDirectorCedula(String directorCedula) {
        this.directorCedula = directorCedula;
    }
    
    public String getTipoDirector() {
        return tipoDirector;
    }
    
    public void setTipoDirector(String tipoDirector) {
        this.tipoDirector = tipoDirector;
    }
    
    public String getCedulaEstudiante() {
        return cedulaEstudiante;
    }
    
    public void setCedulaEstudiante(String cedulaEstudiante) {
        this.cedulaEstudiante = cedulaEstudiante;
    }
}
