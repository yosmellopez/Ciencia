package ult.nodo.ciencia.clases;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "grupo")
public class Grupo implements Serializable {

    @Id
    @Column(name = "grupo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    public Grupo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
