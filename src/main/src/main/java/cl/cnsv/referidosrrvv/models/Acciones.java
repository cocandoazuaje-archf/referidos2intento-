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
@Table(name = "RFD_ACCIONES")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Acciones.findAll", query = "SELECT a FROM Acciones a"),
        @NamedQuery(name = "Acciones.findById", query = "SELECT a FROM Acciones a WHERE a.id = :id"),
        @NamedQuery(name = "Acciones.findByVersion", query = "SELECT a FROM Acciones a WHERE a.version = :version"),
        @NamedQuery(name = "Acciones.findByColor", query = "SELECT a FROM Acciones a WHERE a.color = :color"),
        @NamedQuery(name = "Acciones.findByColorb", query = "SELECT a FROM Acciones a WHERE a.colorb = :colorb"),
        @NamedQuery(name = "Acciones.findByNombre", query = "SELECT a FROM Acciones a WHERE a.nombre = :nombre"), })
public class Acciones implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator1")
    @SequenceGenerator(name = "book_generator1", sequenceName = "RFD_ACCIONES_SEQ", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    private BigInteger version;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "COLOR")
    private String color;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "COLORB")
    private String colorb;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NOMBRE")
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accionId")
    private Collection<Referencias> referenciasCollection;

    public Acciones() {
        // constructor vacio
    }

    public Acciones(BigDecimal id) {
        this.id = id;
    }

    public Acciones(
            BigDecimal id,
            BigInteger version,
            String color,
            String colorb,
            String nombre) {
        this.id = id;
        this.version = version;
        this.color = color;
        this.colorb = colorb;
        this.nombre = nombre;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorb() {
        return colorb;
    }

    public void setColorb(String colorb) {
        this.colorb = colorb;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Referencias> getReferenciasCollection() {
        return referenciasCollection;
    }

    public void setReferenciasCollection(
            Collection<Referencias> referenciasCollection) {
        this.referenciasCollection = referenciasCollection;
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
        if (!(object instanceof Acciones)) {
            return false;
        }
        Acciones other = (Acciones) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidosrrvv.models.Acciones[ id=" + id + " ]";
    }
}
