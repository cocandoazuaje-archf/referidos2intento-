package cl.cns.integracion.wsreferidosrrvv.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wordnik.swagger.annotations.ApiModelProperty;

@JsonDeserialize(using = SMSDeserializer.class)
public class SMSOutput {

    public SMSOutput(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public static SMSOutput ok() {
        return new SMSOutput(0, "Ok");
    }

    public SMSOutput() {
        // constructor vacio
    }

    @ApiModelProperty(value = "0: OK\n"
            + "-1: Error Genérico, ver el mensaje.\n"
            + "1: Error en la validación.\n")
    private int codigo;

    private String mensaje;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
