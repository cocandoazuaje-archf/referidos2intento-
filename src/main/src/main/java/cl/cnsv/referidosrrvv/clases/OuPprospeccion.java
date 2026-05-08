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
public class OuPprospeccion {

    private String param;
    private String indicador;

    public OuPprospeccion() {
        // constructor vacio
    }

    public OuPprospeccion(String param, String indicador) {
        this.param = param;
        this.indicador = indicador;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }
}
