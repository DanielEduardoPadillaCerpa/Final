package com.parcial.app.repositorio;

import com.parcial.app.entidades.ResultadoTyt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResultadoTytRepository extends JpaRepository<ResultadoTyt, Integer> {
    Optional<ResultadoTyt> findByEstudianteId(Integer estudianteId);
}
