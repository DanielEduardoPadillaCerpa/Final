package com.parcial.app.repositorio;

import com.parcial.app.entidades.PagoSaberPro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoSaberProRepository extends JpaRepository<PagoSaberPro, Integer> {
    Optional<PagoSaberPro> findByEstudianteId(Integer estudianteId);
    List<PagoSaberPro> findByEstado(PagoSaberPro.Estado estado);
    List<PagoSaberPro> findByEstudianteFacultadId(Integer facultadId);

}
