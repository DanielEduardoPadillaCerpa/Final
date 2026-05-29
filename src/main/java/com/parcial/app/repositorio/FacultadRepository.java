package com.parcial.app.repositorio;

import com.parcial.app.entidades.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Integer> {
}
