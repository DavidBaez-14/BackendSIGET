package co.edu.ufps.proyectosdegradoufps.usuarios.repositories;

import co.edu.ufps.proyectosdegradoufps.usuarios.models.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    
    Administrador findByProfesorCedula(String profesorCedula);
    
    List<Administrador> findByActivo(Boolean activo);
}