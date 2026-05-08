/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

/**
 *
 * @author cox
 */
public class OutActualizaProspeccion {

    private int codigo;
    private String mensaje;
    private String mensajeEx;

    public OutActualizaProspeccion() {
        // constructor vacio
    }

    public OutActualizaProspeccion(int codigo, String mensaje, String mensajeEx) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.mensajeEx = mensajeEx;
    }

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

    public String getMensajeEx() {
        return mensajeEx;
    }

    public void setMensajeEx(String mensajeEx) {
        this.mensajeEx = mensajeEx;
    }

    @Override
    public String toString() {
        return ("OutActualizaProspeccion{"
                + "codigo="
                + codigo
                + ", mensaje="
                + mensaje
                + ", mensajeEx="
                + mensajeEx
                + '}');
    }
}
