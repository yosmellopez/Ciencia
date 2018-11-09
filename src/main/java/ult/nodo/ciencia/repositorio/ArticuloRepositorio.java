package ult.nodo.ciencia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import ult.nodo.ciencia.clases.Articulo;

public interface ArticuloRepositorio extends JpaRepository<Articulo, Long> {

}
