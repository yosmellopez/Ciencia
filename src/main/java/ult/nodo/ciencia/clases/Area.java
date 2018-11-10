package ult.nodo.ciencia.clases;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "area")
public class Area implements Serializable, EntidadClonable<Area> {

    @Id
    @Column(name = "area_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    public Area() {
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

    @Override
    public void clonar(Area area) {
        nombre = area.nombre;
    }
}
