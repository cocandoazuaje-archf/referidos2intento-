package cl.cns.integracion.wsreferidosrrvv.models;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SMSCall implements Serializable {

    private static final long serialVersionUID = 1L;

    private String celular;
    private String idPlantilla;
    private String lineaNegocio;
    private SMSParametros parametros;

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(String idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    public String getLineaNegocio() {
        return lineaNegocio;
    }

    public void setLineaNegocio(String lineaNegocio) {
        this.lineaNegocio = lineaNegocio;
    }

    public SMSParametros getParametros() {
        return parametros;
    }

    public void setParametros(SMSParametros parametros) {
        this.parametros = parametros;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SMSCall smsCall = (SMSCall) o;
        return (idPlantilla == smsCall.idPlantilla
                && Objects.equals(celular, smsCall.celular)
                && Objects.equals(lineaNegocio, smsCall.lineaNegocio)
                && Objects.equals(parametros, smsCall.parametros));
    }

    @Override
    public int hashCode() {
        return Objects.hash(celular, idPlantilla, lineaNegocio, parametros);
    }

    @Override
    public String toString() {
        return ("SMSCall{"
                + "celular='"
                + celular
                + '\''
                + ", idPlantilla="
                + idPlantilla
                + ", lineaNegocio='"
                + lineaNegocio
                + '\''
                + ", parametros="
                + parametros
                + '}');
    }
}
