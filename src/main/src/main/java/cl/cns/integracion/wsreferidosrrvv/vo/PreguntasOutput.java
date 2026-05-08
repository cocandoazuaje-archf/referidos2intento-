package cl.cns.integracion.wsreferidosrrvv.vo;

import cl.cnsv.referidosrrvv.models.Pregunta;
import java.util.List;

public class PreguntasOutput extends GenericOutput {

    private List<Pregunta> preguntas;

    public PreguntasOutput() {
        // constructor vacio
    }

    public PreguntasOutput(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    @Override
    public String toString() {
        return "RespuestasOutput{" + "preguntas=" + preguntas + '}';
    }
}
