/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.vo;

import cl.cnsv.referidosrrvv.models.Ejecutivos;

/**
 *
 * @author RyC1
 */
public class ErrorCrearReferidoReferenciaBitacoraOut {

    String CANAL;
    String NOMBRE;
    String APELLIDOS;
    String RUT;
    String TELEFONO;
    String TELEFONO2;
    String TELEFONO3;
    String E_MAIL;
    String COMUNA;
    String REGION;
    String ERROR;
    private Ejecutivos ejecutivo;

    public ErrorCrearReferidoReferenciaBitacoraOut() {
        // constructor por defecto
    }

    public String getCANAL() {
        return CANAL;
    }

    public String getERROR() {
        return ERROR;
    }

    public void setERROR(String ERROR) {
        this.ERROR = ERROR;
    }

    public void setCANAL(String CANAL) {
        this.CANAL = CANAL;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getAPELLIDOS() {
        return APELLIDOS;
    }

    public void setAPELLIDOS(String APELLIDOS) {
        this.APELLIDOS = APELLIDOS;
    }

    public String getRUT() {
        return RUT;
    }

    public void setRUT(String RUT) {
        this.RUT = RUT;
    }

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getTELEFONO2() {
        return TELEFONO2;
    }

    public void setTELEFONO2(String TELEFONO2) {
        this.TELEFONO2 = TELEFONO2;
    }

    public String getTELEFONO3() {
        return TELEFONO3;
    }

    public void setTELEFONO3(String TELEFONO3) {
        this.TELEFONO3 = TELEFONO3;
    }

    public String getE_MAIL() {
        return E_MAIL;
    }

    public void setE_MAIL(String E_MAIL) {
        this.E_MAIL = E_MAIL;
    }

    public String getCOMUNA() {
        return COMUNA;
    }

    public void setCOMUNA(String COMUNA) {
        this.COMUNA = COMUNA;
    }

    public String getREGION() {
        return REGION;
    }

    public void setREGION(String REGION) {
        this.REGION = REGION;
    }

    public Ejecutivos getEjecutivo() {
        return ejecutivo;
    }

    public void setEjecutivo(Ejecutivos ejecutivo) {
        this.ejecutivo = ejecutivo;
    }
}
