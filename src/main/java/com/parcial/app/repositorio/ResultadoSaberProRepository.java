package com.parcial.app.repositorio;

import com.parcial.app.entidades.ResultadoSaberPro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResultadoSaberProRepository extends JpaRepository<ResultadoSaberPro, Integer> {
    Optional<ResultadoSaberPro> findByEstudianteId(Integer estudianteId);
}
