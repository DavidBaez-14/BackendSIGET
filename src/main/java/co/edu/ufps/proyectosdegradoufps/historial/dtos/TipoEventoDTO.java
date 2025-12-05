package co.edu.ufps.proyectosdegradoufps.historial.dtos;

import lombok.Data;

@Data
public class TipoEventoDTO {
    private Integer id;
    private String evento;
    private String nombre;
    private String categoria;
    private Boolean cambiaEstado;
    private Integer estadoResultanteId;
    private String estadoResultante;
    private String descripcion;
}
