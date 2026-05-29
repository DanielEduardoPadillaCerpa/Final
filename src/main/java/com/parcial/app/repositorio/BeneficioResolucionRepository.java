package com.parcial.app.repositorio;

import com.parcial.app.entidades.BeneficioResolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BeneficioResolucionRepository extends JpaRepository<BeneficioResolucion, Integer> {
    List<BeneficioResolucion> findByTipoExamen(BeneficioResolucion.TipoExamen tipo);
}

