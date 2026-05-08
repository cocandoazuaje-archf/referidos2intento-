package cl.cns.integracion.wsreferidosrrvv.vo;

import cl.cnsv.referidosrrvv.models.Referencias;
import java.util.ArrayList;
import java.util.List;

public class ReferenciasOutput extends GenericOutput {

    private java.util.List<Referencias> referencias;

    public ReferenciasOutput() {
        // constructor vacio
    }

    public ReferenciasOutput(Referencias referencias) {
        this.referencias = new ArrayList<>();
        this.referencias.add(referencias);
    }

    public List<Referencias> getReferencias() {
        return referencias;
    }

    public void setReferencias(List<Referencias> referencias) {
        this.referencias = referencias;
    }

    @Override
    public String toString() {
        return "ReferenciasOutput{" + "referencias=" + referencias + '}';
    }
}
