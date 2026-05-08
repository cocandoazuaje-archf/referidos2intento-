/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.util.ValidarRutProspeccion;

/**
 *
 * @author cox
 */
public class ValidaRutProspeccion {

    public static String obtener(String vRut) {
        String vRutSDV = vRut;

        if (vRut.contains("-")) {
            vRutSDV = (vRut.contains("SR")) ? "" : vRut.substring(0, vRut.length() - 2);
        } else {
            if (vRutSDV.length() > 7) {
                vRutSDV = vRutSDV.substring(0, vRutSDV.length() - 1);
            }
        }

        return vRutSDV.trim();
    }
}
