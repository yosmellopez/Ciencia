package ult.nodo.ciencia.clases;

import javax.persistence.*;

@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @Column(name = "grupo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;
}
