/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.models.Referidos;

/**
 *
 * @author carlos
 */
public class OutVerificaReferenciaActiva {

    private Referidos referido;
    private boolean tieneReferenciaActiva;

    public OutVerificaReferenciaActiva() {
        // constructor vacio
    }

    public OutVerificaReferenciaActiva(
            Referidos referido,
            boolean tieneReferenciaActiva) {
        this.referido = referido;
        this.tieneReferenciaActiva = tieneReferenciaActiva;
    }

    public Referidos getReferido() {
        return referido;
    }

    public void setReferido(Referidos referido) {
        this.referido = referido;
    }

    public boolean isTieneReferenciaActiva() {
        return tieneReferenciaActiva;
    }

    public void setTieneReferenciaActiva(boolean tieneReferenciaActiva) {
        this.tieneReferenciaActiva = tieneReferenciaActiva;
    }

    @Override
    public String toString() {
        return ("OutVerificaReferenciaActiva{"
                + "referido="
                + referido
                + ", tieneReferenciaActiva="
                + tieneReferenciaActiva
                + '}');
    }
}
