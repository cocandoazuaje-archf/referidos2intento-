/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author cow
 */
public class EntidadDeCargaJs implements Serializable {

    private BigDecimal ID;
    private String APELLIDOS;
    private String CALLE;
    private String CANAL;
    private String COMUNA;
    private String CORREO;
    private String DPTO_CASA;
    private Date FECHA_RECEPCION;
    private String NOMBRE;
    private String NUM_DPTO_CASA;
    private String REGION;
    private String RUT;
    private String SCORE;
    private String TELEFONO;
    private String TELEFONO2;
    private String TELEFONO3;
    private String CANALNAME;
    private String ROWINDEX;
    private BigInteger VERSION;
    private String COMENTARIOS;
    private String USUARIO;
    private String CONECTA_NOCONECTA;
    private String SUCURSAL;
    private String EJECUTIVO;
    private String CODEJECUTIVO;
    private BigDecimal CODSUCURSAL;
    private String DIRECCION;
    private String OWNERE;
    private String OWNERENAME;
    private Date FECHANAC;
    private String ROL;
    private String AST;
    private String SUP;
    private BigDecimal IDEJECUTIVO;
    private String ESTADO;

    private String cuantoDuroAtencion;
    private String nombreReferido;
    private String celularReferido;
    private String mailReferido;
    private String pensionarse;
    private String clienteSolicito;
    private String accionRealizo;
    private String proximosPasos;
    private String tipoPension;
    private String clientesEsperando;
    private String relacionReferido;
    private String sexo;

    public EntidadDeCargaJs() {
        // constructor vacio
    }

    public String getCuantoDuroAtencion() {
        return cuantoDuroAtencion;
    }

    public void setCuantoDuroAtencion(String cuantoDuroAtencion) {
        this.cuantoDuroAtencion = cuantoDuroAtencion;
    }

    public String getNombreReferido() {
        return nombreReferido;
    }

    public void setNombreReferido(String nombreReferido) {
        this.nombreReferido = nombreReferido;
    }

    public String getCelularReferido() {
        return celularReferido;
    }

    public void setCelularReferido(String celularReferido) {
        this.celularReferido = celularReferido;
    }

    public String getMailReferido() {
        return mailReferido;
    }

    public void setMailReferido(String mailReferido) {
        this.mailReferido = mailReferido;
    }

    public String getPensionarse() {
        return pensionarse;
    }

    public void setPensionarse(String pensionarse) {
        this.pensionarse = pensionarse;
    }

    public String getClienteSolicito() {
        return clienteSolicito;
    }

    public void setClienteSolicito(String clienteSolicito) {
        this.clienteSolicito = clienteSolicito;
    }

    public String getAccionRealizo() {
        return accionRealizo;
    }

    public void setAccionRealizo(String accionRealizo) {
        this.accionRealizo = accionRealizo;
    }

    public String getProximosPasos() {
        return proximosPasos;
    }

    public void setProximosPasos(String proximosPasos) {
        this.proximosPasos = proximosPasos;
    }

    public String getTipoPension() {
        return tipoPension;
    }

    public void setTipoPension(String tipoPension) {
        this.tipoPension = tipoPension;
    }

    public String getClientesEsperando() {
        return clientesEsperando;
    }

    public void setClientesEsperando(String clientesEsperando) {
        this.clientesEsperando = clientesEsperando;
    }

    public String getRelacionReferido() {
        return relacionReferido;
    }

    public void setRelacionReferido(String relacionReferido) {
        this.relacionReferido = relacionReferido;
    }

    public BigDecimal getIDEJECUTIVO() {
        return IDEJECUTIVO;
    }

    public void setIDEJECUTIVO(BigDecimal IDEJECUTIVO) {
        this.IDEJECUTIVO = IDEJECUTIVO;
    }

    public String getESTADO() {
        return ESTADO;
    }

    public void setESTADO(String ESTADO) {
        this.ESTADO = ESTADO;
    }

    public String getROL() {
        return ROL;
    }

    public void setROL(String ROL) {
        this.ROL = ROL;
    }

    public BigDecimal getID() {
        return ID;
    }

    public String getCODEJECUTIVO() {
        return CODEJECUTIVO;
    }

    public void setCODEJECUTIVO(String CODEJECUTIVO) {
        this.CODEJECUTIVO = CODEJECUTIVO;
    }

    /**
     * @return the CODSUCURSAL
     */
    public BigDecimal getCODSUCURSAL() {
        return CODSUCURSAL;
    }

    /**
     * @param CODSUCURSAL the CODSUCURSAL to set
     */
    public void setCODSUCURSAL(BigDecimal CODSUCURSAL) {
        this.CODSUCURSAL = CODSUCURSAL;
    }

    /**
     * @return the DIRECCION
     */
    public String getDIRECCION() {
        return DIRECCION;
    }

    /**
     * @param DIRECCION the DIRECCION to set
     */
    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public Date getFECHANAC() {
        return FECHANAC;
    }

    public void setFECHANAC(Date FECHANAC) {
        this.FECHANAC = FECHANAC;
    }

    public String getOWNERE() {
        return OWNERE;
    }

    public void setOWNERE(String OWNERE) {
        this.OWNERE = OWNERE;
    }

    public String getOWNERENAME() {
        return OWNERENAME;
    }

    public void setOWNERENAME(String OWNERENAME) {
        this.OWNERENAME = OWNERENAME;
    }

    public void setID(BigDecimal ID) {
        this.ID = ID;
    }

    public String getCONECTA_NOCONECTA() {
        return CONECTA_NOCONECTA;
    }

    public void setCONECTA_NOCONECTA(String CONECTA_NOCONECTA) {
        this.CONECTA_NOCONECTA = CONECTA_NOCONECTA;
    }

    public String getSUCURSAL() {
        return SUCURSAL;
    }

    public void setSUCURSAL(String SUCURSAL) {
        this.SUCURSAL = SUCURSAL;
    }

    public String getEJECUTIVO() {
        return EJECUTIVO;
    }

    public void setEJECUTIVO(String EJECUTIVO) {
        this.EJECUTIVO = EJECUTIVO;
    }

    public String getUSUARIO() {
        return USUARIO;
    }

    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public String getCOMENTARIOS() {
        return COMENTARIOS;
    }

    public void setCOMENTARIOS(String COMENTARIOS) {
        this.COMENTARIOS = COMENTARIOS;
    }

    public String getAPELLIDOS() {
        return APELLIDOS;
    }

    public void setAPELLIDOS(String APELLIDOS) {
        this.APELLIDOS = APELLIDOS;
    }

    public String getCALLE() {
        return CALLE;
    }

    public void setCALLE(String CALLE) {
        this.CALLE = CALLE;
    }

    public String getCANAL() {
        return CANAL;
    }

    public void setCANAL(String CANAL) {
        this.CANAL = CANAL;
    }

    public String getCOMUNA() {
        return COMUNA;
    }

    public void setCOMUNA(String COMUNA) {
        this.COMUNA = COMUNA;
    }

    public String getCORREO() {
        return CORREO;
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
    }

    public String getDPTO_CASA() {
        return DPTO_CASA;
    }

    public void setDPTO_CASA(String DPTO_CASA) {
        this.DPTO_CASA = DPTO_CASA;
    }

    public Date getFECHA_RECEPCION() {
        return FECHA_RECEPCION;
    }

    public void setFECHA_RECEPCION(Date FECHA_RECEPCION) {
        this.FECHA_RECEPCION = FECHA_RECEPCION;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getNUM_DPTO_CASA() {
        return NUM_DPTO_CASA;
    }

    public void setNUM_DPTO_CASA(String NUM_DPTO_CASA) {
        this.NUM_DPTO_CASA = NUM_DPTO_CASA;
    }

    public String getREGION() {
        return REGION;
    }

    public void setREGION(String REGION) {
        this.REGION = REGION;
    }

    public String getRUT() {
        return RUT;
    }

    public void setRUT(String RUT) {
        this.RUT = RUT;
    }

    public String getSCORE() {
        return SCORE;
    }

    public void setSCORE(String SCORE) {
        this.SCORE = SCORE;
    }

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getCANALNAME() {
        return CANALNAME;
    }

    public void setCANALNAME(String CANALNAME) {
        this.CANALNAME = CANALNAME;
    }

    public String getROWINDEX() {
        return ROWINDEX;
    }

    public void setROWINDEX(String ROWINDEX) {
        this.ROWINDEX = ROWINDEX;
    }

    public BigInteger getVERSION() {
        return VERSION;
    }

    public void setVERSION(BigInteger VERSION) {
        this.VERSION = VERSION;
    }

    /**
     * @return String return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * @return String return the TELEFONO2
     */
    public String getTELEFONO2() {
        return TELEFONO2;
    }

    /**
     * @param TELEFONO2 the TELEFONO2 to set
     */
    public void setTELEFONO2(String TELEFONO2) {
        this.TELEFONO2 = TELEFONO2;
    }

    /**
     * @return String return the TELEFONO3
     */
    public String getTELEFONO3() {
        return TELEFONO3;
    }

    /**
     * @param TELEFONO3 the TELEFONO3 to set
     */
    public void setTELEFONO3(String TELEFONO3) {
        this.TELEFONO3 = TELEFONO3;
    }

    /**
     * @return String return the AST
     */
    public String getAST() {
        return AST;
    }

    /**
     * @param AST the AST to set
     */
    public void setAST(String AST) {
        this.AST = AST;
    }

    /**
     * @return String return the SUP
     */
    public String getSUP() {
        return SUP;
    }

    /**
     * @param SUP the SUP to set
     */
    public void setSUP(String SUP) {
        this.SUP = SUP;
    }
}
