package cl.cns.integracion.wsreferidosrrvv.models;

import java.io.Serializable;
import java.util.Objects;

public class EMCCliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return ("EMCCliente{"
                + "nombre='"
                + nombre
                + '\''
                + ", apellidoPaterno='"
                + apellidoPaterno
                + '\''
                + ", apellidoMaterno='"
                + apellidoMaterno
                + '\''
                + ", email='"
                + email
                + '\''
                + '}');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EMCCliente that = (EMCCliente) o;
        return (Objects.equals(nombre, that.nombre)
                && Objects.equals(apellidoPaterno, that.apellidoPaterno)
                && Objects.equals(apellidoMaterno, that.apellidoMaterno)
                && Objects.equals(email, that.email));
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, apellidoPaterno, apellidoMaterno, email);
    }
}
