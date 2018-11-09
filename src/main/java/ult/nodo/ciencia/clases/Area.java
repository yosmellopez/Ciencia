package ult.nodo.ciencia.clases;

import javax.persistence.*;

@Entity
@Table(name = "area")
public class Area {

    @Id
    @Column(name = "area_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;


}
