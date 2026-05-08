package cl.cns.integracion.wsreferidosrrvv.models;

import java.io.Serializable;
import java.util.List;

public class EMCDestinatarios implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<EMCDestinatario> destinatario;

    public List<EMCDestinatario> getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(List<EMCDestinatario> destinatario) {
        this.destinatario = destinatario;
    }
}
