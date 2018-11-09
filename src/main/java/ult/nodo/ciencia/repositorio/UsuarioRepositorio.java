package ult.nodo.ciencia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import ult.nodo.ciencia.clases.Usuario;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsuario(String usuario);
}
