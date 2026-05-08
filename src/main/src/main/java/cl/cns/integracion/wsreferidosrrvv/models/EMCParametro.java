package cl.cns.integracion.wsreferidosrrvv.models;

import java.io.Serializable;
import java.util.Objects;

public class EMCParametro implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String valor;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return ("EMCParametro{"
                + "nombre='"
                + nombre
                + '\''
                + ", valor='"
                + valor
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
        EMCParametro that = (EMCParametro) o;
        return (Objects.equals(nombre, that.nombre) && Objects.equals(valor, that.valor));
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, valor);
    }
}
