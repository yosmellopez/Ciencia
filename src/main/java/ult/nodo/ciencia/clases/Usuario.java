package ult.nodo.ciencia.clases;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(name = "usuario_unico", columnNames = {"usuario"}),
        @UniqueConstraint(name = "nombre_apellidos_unico", columnNames = {"nombre", "apellidos"})})
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Usuario implements UserDetails, EntidadClonable<Usuario> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "usuario", length = 255)
    private String usuario;

    @Column(name = "contrasena", length = 515)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contrasena;

    @Column(name = "nombre", length = 255)
    private String nombre;

    @Column(name = "apellidos", length = 255)
    private String apellidos;

    @Column(name = "ultimo_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date ultimoInicio;

    @Column(name = "eliminado")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean eliminado = false;

    @ManyToOne
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol", foreignKey = @ForeignKey(name = "fk_usuario_rol"))
    private Rol rol;

    @Transient
    private String nombreCompleto;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(Integer idUsuario, String usuario, String nombre, String apellidos) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Usuario(Integer idUsuario, String usuario, String nombre, String apellidos, Date ultimoInicio) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ultimoInicio = ultimoInicio;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario.toLowerCase();
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario.toLowerCase();
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Date getUltimoInicio() {
        return ultimoInicio;
    }

    public void setUltimoInicio(Date ultimoInicio) {
        this.ultimoInicio = ultimoInicio;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<Rol> roles = new ArrayList<>();
        roles.add(rol);
        return roles;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return contrasena;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return getUsuario();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreCompleto() {
        nombreCompleto = nombre + " " + apellidos;
        return nombreCompleto;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", usuario=" + usuario + ", nombre=" + nombre + ", apellidos=" + apellidos + ", rol=" + rol + ", nombreCompleto=" + nombreCompleto + '}';
    }

    @Override
    public void clonar(Usuario t) {
        rol = t.rol;
        ultimoInicio = t.ultimoInicio == null ? ultimoInicio : t.ultimoInicio;
        usuario = t.usuario;
    }
}
