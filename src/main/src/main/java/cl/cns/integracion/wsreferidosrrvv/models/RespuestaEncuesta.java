package cl.cns.integracion.wsreferidosrrvv.models;

import cl.cnsv.referidosrrvv.models.Pregunta;
import cl.cnsv.referidosrrvv.models.Referidos;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "RFD_RESP_ENCUESTA")
@Cacheable(false)
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "RespuestaEncuesta.findRespuestasByRut", query = "SELECT re FROM RespuestaEncuesta re JOIN fetch Referidos r on re.referido.id = r.id where "
                + "upper(r.rut) = upper(:rut) "),
        @NamedQuery(name = "RespuestaEncuesta.findRespuestasByRutAndIdPregunta", query = "SELECT re FROM RespuestaEncuesta re JOIN fetch Referidos r on re.referido.id = r.id where "
                + "upper(r.rut) = upper(:rut) and re.idPregunta = :idPregunta"),
        @NamedQuery(name = "RespuestaEncuesta.deleteRespuestasByIdReferido", query = "DELETE FROM RespuestaEncuesta re  where re.referido.id = :idreferido"), })
public class RespuestaEncuesta {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generador_resp_encuesta")
    @SequenceGenerator(name = "generador_resp_encuesta", sequenceName = "RFD_RESP_ENCUESTA_SEQ", allocationSize = 1)
    @ApiModelProperty(value = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    @ApiModelProperty(value = "Version")
    private Long version;

    @Column(name = "REFERIDO_ID")
    @ApiModelProperty(value = "Referido asociado")
    private BigDecimal idReferido;

    @XmlTransient
    @JoinColumn(name = "REFERIDO_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @ApiModelProperty(value = "Referido asociado")
    private Referidos referido;

    @Column(name = "PREGUNTA_ID")
    private Long idPregunta;

    @XmlTransient
    @JoinColumn(name = "PREGUNTA_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @ApiModelProperty(value = "Pregunta asociada")
    private Pregunta pregunta;

    @Column(name = "RESPUESTA_BOOL")
    @ApiModelProperty(value = "Respuesta booleana")
    private Boolean respuesta;

    @Column(name = "RESPUESTA_DESC")
    @ApiModelProperty(value = "Descripcion Respuesta")
    private String descripcionRespuesta;

    @Column(name = "RESPUESTA_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "Fecha de ingreso/modificación del registro")
    private Date fechaRespuesta;

    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "Fecha de ingreso/modificación del registro")
    private Date fecha;

    @Size(max = 255)
    @Column(name = "USUARIO")
    @ApiModelProperty(value = "Usuario que realizo la encuesta")
    private String usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Referidos getReferido() {
        return referido;
    }

    public void setReferido(Referidos referido) {
        this.referido = referido;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public Boolean getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Boolean respuesta) {
        this.respuesta = respuesta;
    }

    public String getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    public void setDescripcionRespuesta(String descripcionRespuesta) {
        this.descripcionRespuesta = descripcionRespuesta;
    }

    public Date getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(Date fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
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

    public BigDecimal getIdReferido() {
        return idReferido;
    }

    public void setIdReferido(BigDecimal idReferido) {
        this.idReferido = idReferido;
    }

    public Long getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Long idPregunta) {
        this.idPregunta = idPregunta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RespuestaEncuesta that = (RespuestaEncuesta) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return ("RespuestaEncuesta{"
                + "id="
                + id
                + ", version="
                + version
                + ", idReferido="
                + idReferido
                + ", referido="
                + referido
                + ", idPregunta="
                + idPregunta
                + ", pregunta="
                + pregunta
                + ", respuesta="
                + respuesta
                + ", descripcionRespuesta='"
                + descripcionRespuesta
                + '\''
                + ", fechaRespuesta="
                + fechaRespuesta
                + ", fecha="
                + fecha
                + ", usuario='"
                + usuario
                + '\''
                + '}');
    }
}
