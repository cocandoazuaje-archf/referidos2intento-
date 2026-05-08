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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "RFD_EJECUTIVOS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Ejecutivos.findAll", query = "SELECT e FROM Ejecutivos e"),
        @NamedQuery(name = "Ejecutivos.findById", query = "SELECT e FROM Ejecutivos e WHERE e.id = :id"),
        @NamedQuery(name = "Ejecutivos.findByVersion", query = "SELECT e FROM Ejecutivos e WHERE e.version = :version"),
        @NamedQuery(name = "Ejecutivos.findByCodigo", query = "SELECT e FROM Ejecutivos e WHERE e.codigo = :codigo"),
        @NamedQuery(name = "Ejecutivos.findByCorreo", query = "SELECT e FROM Ejecutivos e WHERE e.correo = :correo"),
        @NamedQuery(name = "Ejecutivos.findByNombre", query = "SELECT e FROM Ejecutivos e WHERE e.nombre = :nombre"),
        @NamedQuery(name = "Ejecutivos.findByTelefono", query = "SELECT e FROM Ejecutivos e WHERE e.telefono = :telefono"), })
public class Ejecutivos implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator3")
    @SequenceGenerator(name = "book_generator3", sequenceName = "RFD_EJECUTIVOS_SEQ", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    private BigInteger version;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "CODIGO")
    private String codigo;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NOMBRE")
    private String nombre;

    @Size(max = 100)
    @Column(name = "CORREO")
    private String correo;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Column(name = "TELEFONO")
    private String telefono;

    @OneToMany(mappedBy = "ejecutivoId")
    private Collection<Referencias> referenciasCollection;

    @JoinColumn(name = "SUCURSAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Sucursales sucursalId;

    public Ejecutivos() {
        // constructor vacio
    }

    public Ejecutivos(BigDecimal id) {
        this.id = id;
    }

    public Ejecutivos(
            BigDecimal id,
            BigInteger version,
            String codigo,
            String nombre,
            String telefono) {
        this.id = id;
        this.version = version;
        this.codigo = codigo;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public Sucursales getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Sucursales sucursalId) {
        this.sucursalId = sucursalId;
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
        if (!(object instanceof Ejecutivos)) {
            return false;
        }
        Ejecutivos other = (Ejecutivos) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidosrrvv.models.Ejecutivos[ id=" + id + " ]";
    }
}
