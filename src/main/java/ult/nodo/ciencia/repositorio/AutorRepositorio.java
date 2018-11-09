package ult.nodo.ciencia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import ult.nodo.ciencia.clases.Autor;

public interface AutorRepositorio extends JpaRepository<Autor, Integer> {

}
