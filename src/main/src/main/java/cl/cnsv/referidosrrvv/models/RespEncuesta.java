/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos Ocando
 */
@Entity
@Table(name = "RFD_RESP_ENCUESTA")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "RespEncuesta.findAll", query = "SELECT r FROM RespEncuesta r"),
        @NamedQuery(name = "RespEncuesta.findById", query = "SELECT r FROM RespEncuesta r WHERE r.id = :id"),
        @NamedQuery(name = "RespEncuesta.findByVersion", query = "SELECT r FROM RespEncuesta r WHERE r.version = :version"),
        @NamedQuery(name = "RespEncuesta.findByRespuestaBool", query = "SELECT r FROM RespEncuesta r WHERE r.respuestaBool = :respuestaBool"),
        @NamedQuery(name = "RespEncuesta.findByRespuestaDesc", query = "SELECT r FROM RespEncuesta r WHERE r.respuestaDesc = :respuestaDesc"),
        @NamedQuery(name = "RespEncuesta.findByRespuestaFecha", query = "SELECT r FROM RespEncuesta r WHERE r.respuestaFecha = :respuestaFecha"),
        @NamedQuery(name = "RespEncuesta.findByFecha", query = "SELECT r FROM RespEncuesta r WHERE r.fecha = :fecha"),
        @NamedQuery(name = "RespEncuesta.findByUsuario", query = "SELECT r FROM RespEncuesta r WHERE r.usuario = :usuario"), })
public class RespEncuesta implements Serializable {

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

    @Basic(optional = false)
    @NotNull
    @Column(name = "RESPUESTA_BOOL")
    private short respuestaBool;

    @Size(max = 1020)
    @Column(name = "RESPUESTA_DESC")
    private String respuestaDesc;

    @Column(name = "RESPUESTA_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date respuestaFecha;

    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Size(max = 1020)
    @Column(name = "USUARIO")
    private String usuario;

    @JoinColumn(name = "PREGUNTA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Pregunta preguntaId;

    @JoinColumn(name = "REFERIDO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Referidos referidoId;

    public RespEncuesta() {
        // constructor vacio
    }

    public RespEncuesta(BigDecimal id) {
        this.id = id;
    }

    public RespEncuesta(
            BigDecimal id,
            BigInteger version,
            short respuestaBool,
            Date fecha) {
        this.id = id;
        this.version = version;
        this.respuestaBool = respuestaBool;
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

    public short getRespuestaBool() {
        return respuestaBool;
    }

    public void setRespuestaBool(short respuestaBool) {
        this.respuestaBool = respuestaBool;
    }

    public String getRespuestaDesc() {
        return respuestaDesc;
    }

    public void setRespuestaDesc(String respuestaDesc) {
        this.respuestaDesc = respuestaDesc;
    }

    public Date getRespuestaFecha() {
        return respuestaFecha;
    }

    public void setRespuestaFecha(Date respuestaFecha) {
        this.respuestaFecha = respuestaFecha;
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

    public Pregunta getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Pregunta preguntaId) {
        this.preguntaId = preguntaId;
    }

    public Referidos getReferidoId() {
        return referidoId;
    }

    public void setReferidoId(Referidos referidoId) {
        this.referidoId = referidoId;
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
        if (!(object instanceof RespEncuesta)) {
            return false;
        }
        RespEncuesta other = (RespEncuesta) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidosrrvv.models.RespEncuesta[ id=" + id + " ]";
    }
}
