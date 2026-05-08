package cl.cns.integracion.wsreferidosrrvv.models;

import java.io.Serializable;
import java.util.Objects;

public class EMCDestinatario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EMCDestinatario{" + "email='" + email + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EMCDestinatario that = (EMCDestinatario) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
