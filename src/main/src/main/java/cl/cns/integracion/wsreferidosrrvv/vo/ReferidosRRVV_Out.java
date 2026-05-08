/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.vo;

import cl.cnsv.referidosrrvv.models.Ejecutivos;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *
 * @author RyC1
 */
public class ReferidosRRVV_Out {

    public ReferidosRRVV_Out(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public ReferidosRRVV_Out() {
        // constructor vacio
    }

    public static ReferidosRRVV_Out ok() {
        return new ReferidosRRVV_Out(0, "Ok");
    }

    @ApiModelProperty(value = "0: OK\n"
            + "-1: Error Genérico, ver el mensaje.\n"
            + "1: Error en la validación.\n")
    private int codigo;

    private String mensaje;
    private Ejecutivos ejecutivo;
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

    public Ejecutivos getEjecutivo() {
        return ejecutivo;
    }

    public void setEjecutivo(Ejecutivos ejecutivo) {
        this.ejecutivo = ejecutivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
