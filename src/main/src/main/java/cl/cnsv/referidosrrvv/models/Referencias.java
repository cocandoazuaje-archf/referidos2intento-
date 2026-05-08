/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cow
 */
@Entity
@Table(name = "RFD_REFERENCIAS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Referencias.findAll", query = "SELECT r FROM Referencias r"),
        @NamedQuery(name = "Referencias.findById", query = "SELECT r FROM Referencias r WHERE r.id = :id"),
        @NamedQuery(name = "Referencias.findByVersion", query = "SELECT r FROM Referencias r WHERE r.version = :version"),
        @NamedQuery(name = "Referencias.findByBlockedbycall", query = "SELECT r FROM Referencias r WHERE r.blockedbycall = :blockedbycall"),
        @NamedQuery(name = "Referencias.findByCanalname", query = "SELECT r FROM Referencias r WHERE r.canalname = :canalname"),
        @NamedQuery(name = "Referencias.findByFecha", query = "SELECT r FROM Referencias r WHERE r.fecha = :fecha"),
        @NamedQuery(name = "Referencias.findByFechaAccion", query = "SELECT r FROM Referencias r WHERE r.fechaAccion = :fechaAccion"),
        @NamedQuery(name = "Referencias.findByFechablockedbycall", query = "SELECT r FROM Referencias r WHERE r.fechablockedbycall = :fechablockedbycall"),
        @NamedQuery(name = "Referencias.findByFechaclickc", query = "SELECT r FROM Referencias r WHERE r.fechaclickc = :fechaclickc"),
        @NamedQuery(name = "Referencias.findByFechaclicke", query = "SELECT r FROM Referencias r WHERE r.fechaclicke = :fechaclicke"),
        @NamedQuery(name = "Referencias.findByFechaterminoc", query = "SELECT r FROM Referencias r WHERE r.fechaterminoc = :fechaterminoc"),
        @NamedQuery(name = "Referencias.findByFechaterminoe", query = "SELECT r FROM Referencias r WHERE r.fechaterminoe = :fechaterminoe"),
        @NamedQuery(name = "Referencias.findByMailAccion", query = "SELECT r FROM Referencias r WHERE r.mailAccion = :mailAccion"),
        @NamedQuery(name = "Referencias.findByOwnerc", query = "SELECT r FROM Referencias r WHERE r.ownerc = :ownerc"),
        @NamedQuery(name = "Referencias.findByOwnere", query = "SELECT r FROM Referencias r WHERE r.ownere = :ownere"),
        @NamedQuery(name = "Referencias.findByOwnerename", query = "SELECT r FROM Referencias r WHERE r.ownerename = :ownerename"),
        @NamedQuery(name = "Referencias.findByReferidor", query = "SELECT r FROM Referencias r WHERE r.referidor = :referidor"),
        @NamedQuery(name = "Referencias.findByUsuario", query = "SELECT r FROM Referencias r WHERE r.usuario = :usuario"), })
public class Referencias implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator5")
    @SequenceGenerator(name = "book_generator5", sequenceName = "RFD_REFERENCIAS_SEQ", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    private BigInteger version;

    @Column(name = "BLOCKEDBYCALL")
    private Long blockedbycall;

    @Size(max = 255)
    @Column(name = "CANALNAME")
    private String canalname;

    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "FECHA_ACCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAccion;

    @Basic(optional = false)
    @Column(name = "FECHABLOCKEDBYCALL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechablockedbycall;

    @Column(name = "FECHACLICKC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaclickc;

    @Column(name = "FECHACLICKE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaclicke;

    @Column(name = "FECHATERMINOC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaterminoc;

    @Column(name = "FECHATERMINOE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaterminoe;

    @Size(max = 255)
    @Column(name = "MAIL_ACCION")
    private String mailAccion;

    @Size(max = 255)
    @Column(name = "OWNERC")
    private String ownerc;

    @Size(max = 255)
    @Column(name = "OWNERE")
    private String ownere;

    @Size(max = 255)
    @Column(name = "OWNERENAME")
    private String ownerename;

    @Size(max = 255)
    @Column(name = "REFERIDOR")
    private String referidor;

    @Size(max = 255)
    @Column(name = "USUARIO")
    private String usuario;

    @Column(name = "PRIORITARIO")
    private Boolean prioritario;

    @JoinColumn(name = "ACCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Acciones accionId;

    @JoinColumn(name = "EJECUTIVO_ID", referencedColumnName = "ID")
    @ManyToOne
    private Ejecutivos ejecutivoId;

    @JoinColumn(name = "REFERIDO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Referidos referidoId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "referenciaId")
    private Collection<Bitacoras> bitacorasCollection;

    public Referencias() {
        // constructor vacio
    }

    public Referencias(BigDecimal id) {
        this.id = id;
    }

    public Referencias(
            BigDecimal id,
            BigInteger version,
            Date fecha,
            Date fechablockedbycall) {
        this.id = id;
        this.version = version;
        this.fecha = fecha;
        this.fechablockedbycall = fechablockedbycall;
    }

    public Boolean getPrioritario() {
        return prioritario;
    }

    public void setPrioritario(Boolean prioritario) {
        this.prioritario = prioritario;
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

    public Long getBlockedbycall() {
        return blockedbycall;
    }

    public void setBlockedbycall(Long blockedbycall) {
        this.blockedbycall = blockedbycall;
    }

    public String getCanalname() {
        return canalname;
    }

    public void setCanalname(String canalname) {
        this.canalname = canalname;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(Date fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public Date getFechablockedbycall() {
        return fechablockedbycall;
    }

    public void setFechablockedbycall(Date fechablockedbycall) {
        this.fechablockedbycall = fechablockedbycall;
    }

    public Date getFechaclickc() {
        return fechaclickc;
    }

    public void setFechaclickc(Date fechaclickc) {
        this.fechaclickc = fechaclickc;
    }

    public Date getFechaclicke() {
        return fechaclicke;
    }

    public void setFechaclicke(Date fechaclicke) {
        this.fechaclicke = fechaclicke;
    }

    public Date getFechaterminoc() {
        return fechaterminoc;
    }

    public void setFechaterminoc(Date fechaterminoc) {
        this.fechaterminoc = fechaterminoc;
    }

    public Date getFechaterminoe() {
        return fechaterminoe;
    }

    public void setFechaterminoe(Date fechaterminoe) {
        this.fechaterminoe = fechaterminoe;
    }

    public String getMailAccion() {
        return mailAccion;
    }

    public void setMailAccion(String mailAccion) {
        this.mailAccion = mailAccion;
    }

    public String getOwnerc() {
        return ownerc;
    }

    public void setOwnerc(String ownerc) {
        this.ownerc = ownerc;
    }

    public String getOwnere() {
        return ownere;
    }

    public void setOwnere(String ownere) {
        this.ownere = ownere;
    }

    public String getOwnerename() {
        return ownerename;
    }

    public void setOwnerename(String ownerename) {
        this.ownerename = ownerename;
    }

    public String getReferidor() {
        return referidor;
    }

    public void setReferidor(String referidor) {
        this.referidor = referidor;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Acciones getAccionId() {
        return accionId;
    }

    public void setAccionId(Acciones accionId) {
        this.accionId = accionId;
    }

    public Ejecutivos getEjecutivoId() {
        return ejecutivoId;
    }

    public void setEjecutivoId(Ejecutivos ejecutivoId) {
        this.ejecutivoId = ejecutivoId;
    }

    public Referidos getReferidoId() {
        return referidoId;
    }

    public void setReferidoId(Referidos referidoId) {
        this.referidoId = referidoId;
    }

    @XmlTransient
    public Collection<Bitacoras> getBitacorasCollection() {
        return bitacorasCollection;
    }

    public void setBitacorasCollection(
            Collection<Bitacoras> bitacorasCollection) {
        this.bitacorasCollection = bitacorasCollection;
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
        if (!(object instanceof Referencias)) {
            return false;
        }
        Referencias other = (Referencias) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidosrrvv.models.Referencias[ id=" + id + " ]";
    }
    // @Override
    // public String toString() {
    // StringBuilder result = new StringBuilder();
    // String newLine = System.getProperty("line.separator");
    //
    // result.append(this.getClass().getName());
    // result.append(" Object {");
    // result.append(newLine);
    //
    // //determine fields declared in this class only (no fields of superclass)
    // Field[] fields = this.getClass().getDeclaredFields();
    //
    // //print field names paired with their values
    // for (Field field : fields) {
    // result.append(" ");
    // try {
    // result.append(field.getName());
    // result.append(": ");
    // //requires access to private field:
    // result.append(field.get(this));
    // } catch (IllegalAccessException ex) {
    // System.out.println(ex);
    // }
    // result.append(newLine);
    // }
    // result.append("}");
    //
    // return result.toString();
    // }

}
