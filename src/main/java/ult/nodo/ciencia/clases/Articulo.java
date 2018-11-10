package ult.nodo.ciencia.clases;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "articulo")
public class Articulo implements Serializable, EntidadClonable<Articulo> {

    @Id
    @Column(name = "articulo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "year_publicado")
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "area_id", foreignKey = @ForeignKey(name = "fk_articulo_area"))
    private Area area;

    @ManyToOne
    @JoinColumn(name = "grupo_id", foreignKey = @ForeignKey(name = "fk_articulo_grupo"))
    private Grupo grupo;

    @ManyToMany
    @JoinTable(name = "articulo_autor",
            joinColumns = @JoinColumn(name = "articulo_id", foreignKey = @ForeignKey(name = "fk_articulo_autor")),
            inverseJoinColumns = @JoinColumn(name = "autor_id", foreignKey = @ForeignKey(name = "fk_autor_articulo")))
    private Set<Autor> autores;

    public Articulo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    @Override
    public void clonar(Articulo articulo) {
        titulo = articulo.titulo;
        year = articulo.year;
        area = articulo.area;
        grupo = articulo.grupo;
        autores = articulo.autores;
    }
}
