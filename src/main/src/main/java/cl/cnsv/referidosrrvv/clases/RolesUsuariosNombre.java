package cl.cnsv.referidosrrvv.clases;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RolesUsuariosNombre implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "ROL")
    private String rol;

    public RolesUsuariosNombre() {
        // constructor vacio
    }

    public RolesUsuariosNombre(
            String id,
            String estado,
            String nombre,
            String rol) {
        this.id = id;
        this.estado = estado;
        this.nombre = nombre;
        this.rol = rol;
    }

    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return String return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return String return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return String return the rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}
