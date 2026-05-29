package com.parcial.app.repositorio;

import com.parcial.app.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByCedula(String cedula);
    List<Usuario> findByRol(Usuario.Rol rol);
}
