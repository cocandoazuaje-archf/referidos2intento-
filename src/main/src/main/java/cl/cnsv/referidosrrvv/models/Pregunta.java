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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Carlos Ocando
 */
@Entity
@Table(name = "RFD_PREGUNTA")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Pregunta.findAll", query = "SELECT p FROM Pregunta p"),
        @NamedQuery(name = "Pregunta.findById", query = "SELECT p FROM Pregunta p WHERE p.id = :id"),
        @NamedQuery(name = "Pregunta.findByVersion", query = "SELECT p FROM Pregunta p WHERE p.version = :version"),
        @NamedQuery(name = "Pregunta.findByPregunta", query = "SELECT p FROM Pregunta p WHERE p.pregunta = :pregunta"),
        @NamedQuery(name = "Pregunta.findByDescripcion", query = "SELECT p FROM Pregunta p WHERE p.descripcion = :descripcion"),
        @NamedQuery(name = "Pregunta.findByFecha", query = "SELECT p FROM Pregunta p WHERE p.fecha = :fecha"),
        @NamedQuery(name = "Pregunta.findByUsuario", query = "SELECT p FROM Pregunta p WHERE p.usuario = :usuario"), })
public class Pregunta implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    private BigInteger version;

    @Size(max = 1020)
    @Column(name = "PREGUNTA")
    private String pregunta;

    @Size(max = 1020)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Size(max = 1020)
    @Column(name = "USUARIO")
    private String usuario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntaId")
    private Collection<RespEncuesta> respEncuestaCollection;

    public Pregunta() {
        // constructor vacio
    }

    public Pregunta(BigDecimal id) {
        this.id = id;
    }

    public Pregunta(BigDecimal id, BigInteger version, Date fecha) {
        this.id = id;
        this.version = version;
        this.fecha = fecha;
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

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @XmlTransient
    public Collection<RespEncuesta> getRespEncuestaCollection() {
        return respEncuestaCollection;
    }

    public void setRespEncuestaCollection(
            Collection<RespEncuesta> respEncuestaCollection) {
        this.respEncuestaCollection = respEncuestaCollection;
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
        if (!(object instanceof Pregunta)) {
            return false;
        }
        Pregunta other = (Pregunta) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidosrrvv.models.Pregunta[ id=" + id + " ]";
    }
}
