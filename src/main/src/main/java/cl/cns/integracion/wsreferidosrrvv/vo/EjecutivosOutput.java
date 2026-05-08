package cl.cns.integracion.wsreferidosrrvv.vo;

import cl.cnsv.referidosrrvv.models.Ejecutivos;
import java.util.ArrayList;
import java.util.List;

public class EjecutivosOutput extends GenericOutput {

    private java.util.List<Ejecutivos> ejecutivos;

    public EjecutivosOutput() {
        // constructor vacio
    }

    public EjecutivosOutput(List ejecutivos) {
        this.ejecutivos = ejecutivos;
    }

    public EjecutivosOutput(Ejecutivos ejecutivos) {
        this.ejecutivos = new ArrayList<>();
        this.ejecutivos.add(ejecutivos);
    }

    public List<Ejecutivos> getEjecutivos() {
        return ejecutivos;
    }

    public void setEjecutivos(List<Ejecutivos> ejecutivos) {
        this.ejecutivos = ejecutivos;
    }

    @Override
    public String toString() {
        return "EjecutivosOutput{" + "ejecutivos=" + ejecutivos + '}';
    }
}
