package cl.cns.integracion.wsreferidosrrvv.models;

import java.io.Serializable;
import java.util.Objects;

public class SMSParametros implements Serializable {

    private static final long serialVersionUID = 1L;

    private String parametro1 = "";
    private String parametro2 = "";
    private String parametro3 = "";
    private String parametro4 = "";
    private String parametro5 = "";

    public String getParametro1() {
        return parametro1;
    }

    public void setParametro1(String parametro1) {
        this.parametro1 = parametro1;
    }

    public String getParametro2() {
        return parametro2;
    }

    public void setParametro2(String parametro2) {
        this.parametro2 = parametro2;
    }

    public String getParametro3() {
        return parametro3;
    }

    public void setParametro3(String parametro3) {
        this.parametro3 = parametro3;
    }

    public String getParametro4() {
        return parametro4;
    }

    public void setParametro4(String parametro4) {
        this.parametro4 = parametro4;
    }

    public String getParametro5() {
        return parametro5;
    }

    public void setParametro5(String parametro5) {
        this.parametro5 = parametro5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SMSParametros that = (SMSParametros) o;
        return (Objects.equals(parametro1, that.parametro1)
                && Objects.equals(parametro2, that.parametro2)
                && Objects.equals(parametro3, that.parametro3)
                && Objects.equals(parametro4, that.parametro4)
                && Objects.equals(parametro5, that.parametro5));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                parametro1,
                parametro2,
                parametro3,
                parametro4,
                parametro5);
    }

    @Override
    public String toString() {
        return ("SMSParametros{"
                + "parametro1='"
                + parametro1
                + '\''
                + ", parametro2='"
                + parametro2
                + '\''
                + ", parametro3='"
                + parametro3
                + '\''
                + ", parametro4='"
                + parametro4
                + '\''
                + ", parametro5='"
                + parametro5
                + '\''
                + '}');
    }
}
