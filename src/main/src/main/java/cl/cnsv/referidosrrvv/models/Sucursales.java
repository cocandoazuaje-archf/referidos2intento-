/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cow
 */
@Entity
@Table(name = "RFD_SUCURSALES")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Sucursales.findAll", query = "SELECT s FROM Sucursales s"),
        @NamedQuery(name = "Sucursales.findById", query = "SELECT s FROM Sucursales s WHERE s.id = :id"),
        @NamedQuery(name = "Sucursales.findByVersion", query = "SELECT s FROM Sucursales s WHERE s.version = :version"),
        @NamedQuery(name = "Sucursales.findByNombre", query = "SELECT s FROM Sucursales s WHERE s.nombre = :nombre"),
        @NamedQuery(name = "Sucursales.findByDireccion", query = "SELECT s FROM Sucursales s WHERE s.direccion = :direccion"),
        @NamedQuery(name = "Sucursales.findByComuna", query = "SELECT s FROM Sucursales s WHERE s.comuna = :comuna"),
        @NamedQuery(name = "Sucursales.findByRegion", query = "SELECT s FROM Sucursales s WHERE s.region = :region"), })
public class Sucursales implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator8")
    @SequenceGenerator(name = "book_generator8", sequenceName = "RFD_SUCURSALES_SEQ", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    private BigInteger version;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NOMBRE")
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sucursalId")
    private Collection<Ejecutivos> ejecutivosCollection;

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "DIRECCION")
    private String direccion;

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "COMUNA")
    private String comuna;

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "REGION")
    private String region;

    public Sucursales() {
        // constructor vacio
    }

    public Sucursales(BigDecimal id) {
        this.id = id;
    }

    public Sucursales(
            BigDecimal id,
            BigInteger version,
            String nombre,
            String direccion,
            String comuna,
            String region) {
        this.id = id;
        this.version = version;
        this.nombre = nombre;
        this.direccion = direccion;
        this.comuna = comuna;
        this.region = region;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the comuna
     */
    public String getComuna() {
        return comuna;
    }

    /**
     * @param comuna the comuna to set
     */
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    @XmlTransient
    public Collection<Ejecutivos> getEjecutivosCollection() {
        return ejecutivosCollection;
    }

    public void setEjecutivosCollection(
            Collection<Ejecutivos> ejecutivosCollection) {
        this.ejecutivosCollection = ejecutivosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sucursales)) {
            return false;
        }
        Sucursales other = (Sucursales) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidosrrvv.models.Sucursales[ id=" + id + " ]";
    }
}
