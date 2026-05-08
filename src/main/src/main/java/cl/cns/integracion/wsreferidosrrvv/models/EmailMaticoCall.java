package cl.cns.integracion.wsreferidosrrvv.models;

import java.io.Serializable;
import java.util.Objects;

public class EmailMaticoCall implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fecha;
    private String hora;
    private int flagLineaNegocio;
    private EMCCliente cliente;
    private EMCEmail email;

    public EmailMaticoCall() {
        // constructor vacio
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getFlagLineaNegocio() {
        return flagLineaNegocio;
    }

    public void setFlagLineaNegocio(int flagLineaNegocio) {
        this.flagLineaNegocio = flagLineaNegocio;
    }

    public EMCCliente getCliente() {
        return cliente;
    }

    public void setCliente(EMCCliente cliente) {
        this.cliente = cliente;
    }

    public EMCEmail getEmail() {
        return email;
    }

    public void setEmail(EMCEmail email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmailMaticoCall that = (EmailMaticoCall) o;
        return (flagLineaNegocio == that.flagLineaNegocio
                && Objects.equals(fecha, that.fecha)
                && Objects.equals(hora, that.hora)
                && Objects.equals(cliente, that.cliente)
                && Objects.equals(email, that.email));
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha, hora, flagLineaNegocio, cliente, email);
    }

    @Override
    public String toString() {
        return ("EmailMaticoCall{"
                + "fecha='"
                + fecha
                + '\''
                + ", hora='"
                + hora
                + '\''
                + ", flagLineaNegocio="
                + flagLineaNegocio
                + ", cliente="
                + cliente
                + ", email="
                + email
                + '}');
    }
}
