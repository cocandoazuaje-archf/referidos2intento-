package cl.cns.integracion.wsreferidosrrvv.vo;

import cl.cns.integracion.wsreferidosrrvv.models.RespuestaEncuesta;
import java.util.List;

public class RespuestasOutput extends GenericOutput {

    private java.util.List<RespuestaEncuesta> respuestas;

    public RespuestasOutput() {
        // constructor vacio
    }

    public RespuestasOutput(List<RespuestaEncuesta> respuestaEncuestas) {
        respuestas = respuestaEncuestas;
    }

    public List<RespuestaEncuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaEncuesta> respuestas) {
        this.respuestas = respuestas;
    }

    @Override
    public String toString() {
        return "RespuestasOutput{" + "respuestas=" + respuestas + '}';
    }
}
