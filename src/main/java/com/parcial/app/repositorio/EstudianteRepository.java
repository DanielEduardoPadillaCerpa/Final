package com.parcial.app.repositorio;

import com.parcial.app.entidades.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    Optional<Estudiante> findByCedula(String cedula);
    Optional<Estudiante> findByCorreo(String correo);
    List<Estudiante> findByProgramaFacultadId(Integer facultadId);
    List<Estudiante> findByTipoExamen(Estudiante.TipoExamen tipo);

    @Query("SELECT e FROM Estudiante e WHERE e.programa.facultad.id = :facId AND e.activo = true")
    List<Estudiante> findActivosByFacultad(@Param("facId") Integer facId);
}
