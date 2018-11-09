package ult.nodo.ciencia.clases;

import javax.persistence.*;

@Entity
@Table(name = "articulo")
public class Articulo {

    @Id
    @Column(name = "articulo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
