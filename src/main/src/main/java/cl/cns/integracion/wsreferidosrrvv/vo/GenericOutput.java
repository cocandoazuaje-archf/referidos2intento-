package cl.cns.integracion.wsreferidosrrvv.vo;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class GenericOutput {

    public GenericOutput(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public static GenericOutput ok() {
        return new GenericOutput(0, "Ok");
    }

    public GenericOutput() {
        // constructor vacio
    }

    @ApiModelProperty(value = "0: OK\n"
            + "-1: Error Genérico, ver el mensaje.\n"
            + "1: Error en la validación.\n")
    private int codigo;

    private String mensaje;
    private String estado;

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
