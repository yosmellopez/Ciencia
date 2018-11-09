package ult.nodo.ciencia.clases;

import javax.persistence.*;

@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @Column(name = "autor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;
}
