package cl.cns.integracion.wsreferidosrrvv.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class EMCEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idPlantilla;
    private EMCParametros parametros;
    private EMCDestinatarios destinatarios;

    public String getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(String idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    public EMCParametros getParametros() {
        return parametros;
    }

    public void setParametros(EMCParametros parametros) {
        this.parametros = parametros;
    }

    public EMCDestinatarios getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(EMCDestinatarios destinatarios) {
        this.destinatarios = destinatarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EMCEmail emcEmail = (EMCEmail) o;
        return (idPlantilla == emcEmail.idPlantilla
                && Objects.equals(parametros, emcEmail.parametros)
                && Objects.equals(destinatarios, emcEmail.destinatarios));
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlantilla, parametros, destinatarios);
    }

    @Override
    public String toString() {
        return ("EMCEmail{"
                + "idPlantilla="
                + idPlantilla
                + ", parametros="
                + parametros
                + ", destinatarios="
                + destinatarios
                + '}');
    }
}
