package cl.cns.integracion.wsreferidosrrvv.vo;

import cl.cns.integracion.wsreferidosrrvv.models.RespuestaEncuesta;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

public class ListarespuestasWrapper {

    @NotNull
    @NotBlank
    @ApiModelProperty(value = "Rut en formato 12312312-3", required = true)
    String rut;

    @Valid
    List<RespuestaEncuestaInput> respuestas;

    public List<RespuestaEncuestaInput> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaEncuestaInput> respuestas) {
        this.respuestas = respuestas;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    @Override
    public String toString() {
        return ("ListarespuestasWrapper{"
                + "rut='"
                + rut
                + '\''
                + ", respuestas="
                + respuestas
                + '}');
    }
}
