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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cow
 */
@Entity
@Table(name = "RFD_REFERIDOS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Referidos.findAll", query = "SELECT r FROM Referidos r"),
        @NamedQuery(name = "Referidos.findById", query = "SELECT r FROM Referidos r WHERE r.id = :id"),
        @NamedQuery(name = "Referidos.findByVersion", query = "SELECT r FROM Referidos r WHERE r.version = :version"),
        @NamedQuery(name = "Referidos.findByAccionRealizo", query = "SELECT r FROM Referidos r WHERE r.accionRealizo = :accionRealizo"),
        @NamedQuery(name = "Referidos.findByApellido", query = "SELECT r FROM Referidos r WHERE r.apellido = :apellido"),
        @NamedQuery(name = "Referidos.findByCalle", query = "SELECT r FROM Referidos r WHERE r.calle = :calle"),
        @NamedQuery(name = "Referidos.findByCelularReferido", query = "SELECT r FROM Referidos r WHERE r.celularReferido = :celularReferido"),
        @NamedQuery(name = "Referidos.findByClienteSolicito", query = "SELECT r FROM Referidos r WHERE r.clienteSolicito = :clienteSolicito"),
        @NamedQuery(name = "Referidos.findByClientesEsperando", query = "SELECT r FROM Referidos r WHERE r.clientesEsperando = :clientesEsperando"),
        @NamedQuery(name = "Referidos.findByComuna", query = "SELECT r FROM Referidos r WHERE r.comuna = :comuna"),
        @NamedQuery(name = "Referidos.findByCorreo", query = "SELECT r FROM Referidos r WHERE r.correo = :correo"),
        @NamedQuery(name = "Referidos.findByCuantoDuroAtencion", query = "SELECT r FROM Referidos r WHERE r.cuantoDuroAtencion = :cuantoDuroAtencion"),
        @NamedQuery(name = "Referidos.findByDptoCasa", query = "SELECT r FROM Referidos r WHERE r.dptoCasa = :dptoCasa"),
        @NamedQuery(name = "Referidos.findByFechaIngreso", query = "SELECT r FROM Referidos r WHERE r.fechaIngreso = :fechaIngreso"),
        @NamedQuery(name = "Referidos.findByFechanac", query = "SELECT r FROM Referidos r WHERE r.fechanac = :fechanac"),
        @NamedQuery(name = "Referidos.findById3", query = "SELECT r FROM Referidos r WHERE r.id3 = :id3"),
        @NamedQuery(name = "Referidos.findByMailReferido", query = "SELECT r FROM Referidos r WHERE r.mailReferido = :mailReferido"),
        @NamedQuery(name = "Referidos.findByNombre", query = "SELECT r FROM Referidos r WHERE r.nombre = :nombre"),
        @NamedQuery(name = "Referidos.findByNombreReferido", query = "SELECT r FROM Referidos r WHERE r.nombreReferido = :nombreReferido"),
        @NamedQuery(name = "Referidos.findByNumDptoCasa", query = "SELECT r FROM Referidos r WHERE r.numDptoCasa = :numDptoCasa"),
        @NamedQuery(name = "Referidos.findByPensionarse", query = "SELECT r FROM Referidos r WHERE r.pensionarse = :pensionarse"),
        @NamedQuery(name = "Referidos.findByProximosPasos", query = "SELECT r FROM Referidos r WHERE r.proximosPasos = :proximosPasos"),
        @NamedQuery(name = "Referidos.findByRegion", query = "SELECT r FROM Referidos r WHERE r.region = :region"),
        @NamedQuery(name = "Referidos.findByRelacionReferido", query = "SELECT r FROM Referidos r WHERE r.relacionReferido = :relacionReferido"),
        @NamedQuery(name = "Referidos.findByRut", query = "SELECT r FROM Referidos r WHERE upper(r.rut) = upper(:rut)"),
        @NamedQuery(name = "Referidos.findByScore", query = "SELECT r FROM Referidos r WHERE r.score = :score"),
        @NamedQuery(name = "Referidos.findByTelefonos", query = "SELECT r FROM Referidos r WHERE r.telefonos = :telefonos"),
        @NamedQuery(name = "Referidos.findByTelefonos2", query = "SELECT r FROM Referidos r WHERE r.telefonos2 = :telefonos2"),
        @NamedQuery(name = "Referidos.findByTelefonos3", query = "SELECT r FROM Referidos r WHERE r.telefonos3 = :telefonos3"),
        @NamedQuery(name = "Referidos.findByTipoPension", query = "SELECT r FROM Referidos r WHERE r.tipoPension = :tipoPension"),
        @NamedQuery(name = "Referidos.findActivoByRut", query = "SELECT ref FROM Referidos r "
                + "join fetch Referencias ref on r.id = ref.referidoId.id WHERE "
                + "upper(r.rut) = upper(:rut) "
                + " order by ref.fecha desc"),
        @NamedQuery(name = "Referidos.findReferenciaReferido", query = "SELECT ref FROM Referidos r "
                + "join fetch Referencias ref on r.id = ref.referidoId.id WHERE "
                + "ref.referidoId.id =  :referidoId "
                + "order by ref.fecha desc"),
        @NamedQuery(name = "Referidos.findBySolicitoReferido", query = "SELECT r FROM Referidos r WHERE r.solicitoReferido = :solicitoReferido"),
        @NamedQuery(name = "Referidos.findBySexo", query = "SELECT r FROM Referidos r WHERE r.sexo = :sexo"), })
public class Referidos implements Serializable {

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    private static final long serialVersionUID = 1L;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator6")
    @SequenceGenerator(name = "book_generator6", sequenceName = "RFD_REFERIDOS_SEQ", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    private BigInteger version;

    @Size(max = 255)
    @Column(name = "ACCION_REALIZO")
    private String accionRealizo;

    @Size(max = 255)
    @Column(name = "APELLIDO")
    private String apellido;

    @Size(max = 255)
    @Column(name = "CALLE")
    private String calle;

    @Size(max = 255)
    @Column(name = "CELULAR_REFERIDO")
    private String celularReferido;

    @Size(max = 255)
    @Column(name = "CLIENTE_SOLICITO")
    private String clienteSolicito;

    @Size(max = 255)
    @Column(name = "CLIENTES_ESPERANDO")
    private String clientesEsperando;

    @Size(max = 255)
    @Column(name = "COMUNA")
    private String comuna;

    @Size(max = 255)
    @Column(name = "CORREO")
    private String correo;

    @Size(max = 255)
    @Column(name = "CUANTO_DURO_ATENCION")
    private String cuantoDuroAtencion;

    @Size(max = 255)
    @Column(name = "DPTO_CASA")
    private String dptoCasa;

    @Column(name = "FECHA_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    @Column(name = "FECHANAC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechanac;

    @Column(name = "ID3")
    private Long id3;

    @Size(max = 255)
    @Column(name = "MAIL_REFERIDO")
    private String mailReferido;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NOMBRE")
    private String nombre;

    @Size(max = 255)
    @Column(name = "NOMBRE_REFERIDO")
    private String nombreReferido;

    @Size(max = 255)
    @Column(name = "NUM_DPTO_CASA")
    private String numDptoCasa;

    @Size(max = 255)
    @Column(name = "PENSIONARSE")
    private String pensionarse;

    @Size(max = 255)
    @Column(name = "PROXIMOS_PASOS")
    private String proximosPasos;

    @Size(max = 255)
    @Column(name = "REGION")
    private String region;

    @Size(max = 255)
    @Column(name = "RELACION_REFERIDO")
    private String relacionReferido;

    @Size(max = 255)
    @Column(name = "RUT")
    private String rut;

    @Size(max = 255)
    @Column(name = "SCORE")
    private String score;

    @Size(max = 255)
    @Column(name = "TELEFONOS")
    private String telefonos;

    @Size(max = 255)
    @Column(name = "TELEFONOS2")
    private String telefonos2;

    @Size(max = 255)
    @Column(name = "TELEFONOS3")
    private String telefonos3;

    @Size(max = 255)
    @Column(name = "TIPO_PENSION")
    private String tipoPension;

    @Size(max = 100)
    @Column(name = "SOLICITO_REFERIDO")
    private String solicitoReferido;

    @Size(max = 100)
    @Column(name = "SEXO")
    private String sexo;

    @Size(max = 255)
    @Column(name = "PRIMA")
    private String prima;

    @Size(max = 255)
    @Column(name = "AFP")
    private String afp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "referidoId")
    private Collection<Referencias> referenciasCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "referidoId")
    private Collection<RespEncuesta> respEncuestaCollection;

    public Referidos() {
        // constructor vacio
    }

    public Referidos(BigDecimal id) {
        this.id = id;
    }

    public Referidos(BigDecimal id, BigInteger version, String nombre) {
        this.id = id;
        this.version = version;
        this.nombre = nombre;
    }

    public String getSolicitoReferido() {
        return solicitoReferido;
    }

    public void setSolicitoReferido(String solicitoReferido) {
        this.solicitoReferido = solicitoReferido;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public String getAccionRealizo() {
        return accionRealizo;
    }

    public void setAccionRealizo(String accionRealizo) {
        this.accionRealizo = accionRealizo;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCelularReferido() {
        return celularReferido;
    }

    public void setCelularReferido(String celularReferido) {
        this.celularReferido = celularReferido;
    }

    public String getClienteSolicito() {
        return clienteSolicito;
    }

    public void setClienteSolicito(String clienteSolicito) {
        this.clienteSolicito = clienteSolicito;
    }

    public String getClientesEsperando() {
        return clientesEsperando;
    }

    public void setClientesEsperando(String clientesEsperando) {
        this.clientesEsperando = clientesEsperando;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCuantoDuroAtencion() {
        return cuantoDuroAtencion;
    }

    public void setCuantoDuroAtencion(String cuantoDuroAtencion) {
        this.cuantoDuroAtencion = cuantoDuroAtencion;
    }

    public String getDptoCasa() {
        return dptoCasa;
    }

    public void setDptoCasa(String dptoCasa) {
        this.dptoCasa = dptoCasa;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechanac() {
        return fechanac;
    }

    public void setFechanac(Date fechanac) {
        this.fechanac = fechanac;
    }

    public Long getId3() {
        return id3;
    }

    public void setId3(Long id3) {
        this.id3 = id3;
    }

    public String getMailReferido() {
        return mailReferido;
    }

    public void setMailReferido(String mailReferido) {
        this.mailReferido = mailReferido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreReferido() {
        return nombreReferido;
    }

    public void setNombreReferido(String nombreReferido) {
        this.nombreReferido = nombreReferido;
    }

    public String getNumDptoCasa() {
        return numDptoCasa;
    }

    public void setNumDptoCasa(String numDptoCasa) {
        this.numDptoCasa = numDptoCasa;
    }

    public String getPensionarse() {
        return pensionarse;
    }

    public void setPensionarse(String pensionarse) {
        this.pensionarse = pensionarse;
    }

    public String getProximosPasos() {
        return proximosPasos;
    }

    public void setProximosPasos(String proximosPasos) {
        this.proximosPasos = proximosPasos;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRelacionReferido() {
        return relacionReferido;
    }

    public void setRelacionReferido(String relacionReferido) {
        this.relacionReferido = relacionReferido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getTipoPension() {
        return tipoPension;
    }

    public void setTipoPension(String tipoPension) {
        this.tipoPension = tipoPension;
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
        if (!(object instanceof Referidos)) {
            return false;
        }
        Referidos other = (Referidos) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidosrrvv.models.Referidos[ id=" + id + " ]";
    }

    /**
     * @return String return the telefonos2
     */
    public String getTelefonos2() {
        return telefonos2;
    }

    /**
     * @param telefonos2 the telefonos2 to set
     */
    public void setTelefonos2(String telefonos2) {
        this.telefonos2 = telefonos2;
    }

    /**
     * @return String return the telefonos3
     */
    public String getTelefonos3() {
        return telefonos3;
    }

    /**
     * @param telefonos3 the telefonos3 to set
     */
    public void setTelefonos3(String telefonos3) {
        this.telefonos3 = telefonos3;
    }

    @XmlTransient
    public Collection<RespEncuesta> getRespEncuestaCollection() {
        return respEncuestaCollection;
    }

    public void setRespEncuestaCollection(
            Collection<RespEncuesta> respEncuestaCollection) {
        this.respEncuestaCollection = respEncuestaCollection;
    }

    /**
     * @return String return the prima
     */
    public String getPrima() {
        return prima;
    }

    /**
     * @param prima the prima to set
     */
    public void setPrima(String prima) {
        this.prima = prima;
    }

    /**
     * @return String return the afp
     */
    public String getAfp() {
        return afp;
    }

    /**
     * @param afp the afp to set
     */
    public void setAfp(String afp) {
        this.afp = afp;
    }
}
