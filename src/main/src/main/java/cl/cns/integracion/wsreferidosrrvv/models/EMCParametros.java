package cl.cns.integracion.wsreferidosrrvv.models;

import java.io.Serializable;
import java.util.List;

public class EMCParametros implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<EMCParametro> parametro;

    public List<EMCParametro> getParametro() {
        return parametro;
    }

    public void setParametro(List<EMCParametro> parametro) {
        this.parametro = parametro;
    }
}
