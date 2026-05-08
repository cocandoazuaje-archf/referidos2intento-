package cl.cns.integracion.wsreferidosrrvv.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CorreoDeserializer.class)
public class EnvioCorreoOutput extends GenericOutput {

    private String codigoCorreo;
    private String mensajeCorreo;

    public EnvioCorreoOutput() {
        // constructor vacio
    }

    public String getCodigoCorreo() {
        return codigoCorreo;
    }

    public void setCodigoCorreo(String codigoCorreo) {
        this.codigoCorreo = codigoCorreo;
    }

    public String getMensajeCorreo() {
        return mensajeCorreo;
    }

    public void setMensajeCorreo(String mensajeCorreo) {
        this.mensajeCorreo = mensajeCorreo;
    }

    @Override
    public String toString() {
        return ("EnvioCorreoOutput{"
                + "codigoCorreo="
                + codigoCorreo
                + ", mensajeCorreo='"
                + mensajeCorreo
                + '\''
                + '}');
    }
}
