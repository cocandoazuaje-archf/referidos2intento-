package cl.cns.integracion.wsreferidosrrvv.vo;

import cl.cns.integracion.wsreferidosrrvv.models.RespuestaEncuesta;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;

public class RespuestaEncuestaInput {

    @NotNull
    @ApiModelProperty(value = "Id Pregunta", required = true)
    private Long idPregunta;

    @NotNull
    @ApiModelProperty(value = "Respuesta", required = true)
    private Boolean respuesta;

    private String descripcionRespuesta;

    private Date fechaRespuesta;

    public Long getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Long idPregunta) {
        this.idPregunta = idPregunta;
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

    public RespuestaEncuesta toModel() {
        RespuestaEncuesta r = new RespuestaEncuesta();
        r.setIdPregunta(this.getIdPregunta());
        r.setRespuesta(this.getRespuesta());
        r.setDescripcionRespuesta(this.getDescripcionRespuesta());
        r.setFechaRespuesta(this.getFechaRespuesta());
        return r;
    }
}
