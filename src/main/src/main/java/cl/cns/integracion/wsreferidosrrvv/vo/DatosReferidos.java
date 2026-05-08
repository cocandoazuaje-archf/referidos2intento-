/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.vo;

import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author cow
 */
public class DatosReferidos implements Serializable {

    private String apellidos;
    private BigDecimal idreferencia;
    private BigDecimal acciones;
    private String calle;
    private String canal;
    private String comuna;
    private String correo;
    private String dpto_casa;

    private String nombre;
    private String num_dpto_casa;
    private String region;

    @ApiModelProperty(value = "Rut en formato 12312312-3", required = true)
    private String rut;

    private String score;
    private String telefono;
    private String telefono2;
    private String telefono3;

    private BigInteger version;
    private String comentarios;

    @ApiModelProperty(value = "Usuario", required = true)
    private String usuario;

    private String cuantoduroatencion;
    private String nombrereferido;
    private String celularreferido;
    private String mailreferido;
    private String pensionarse;
    private String clientesolicito;
    private String accionrealizo;
    private String proximospasos;
    private String tipopension;
    private String clientesesperando;
    private String relacionreferido;
    private String sexo;
    private String solicitoreferido;
    private BigDecimal idejecutivo;

    private String fechanacimiento;
    private BigDecimal idsucursal;

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDpto_casa() {
        return dpto_casa;
    }

    public void setDpto_casa(String dpto_casa) {
        this.dpto_casa = dpto_casa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNum_dpto_casa() {
        return num_dpto_casa;
    }

    public void setNum_dpto_casa(String num_dpto_casa) {
        this.num_dpto_casa = num_dpto_casa;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getTelefono3() {
        return telefono3;
    }

    public void setTelefono3(String telefono3) {
        this.telefono3 = telefono3;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCuantoduroatencion() {
        return cuantoduroatencion;
    }

    public void setCuantoduroatencion(String cuantoduroatencion) {
        this.cuantoduroatencion = cuantoduroatencion;
    }

    public String getNombrereferido() {
        return nombrereferido;
    }

    public void setNombrereferido(String nombrereferido) {
        this.nombrereferido = nombrereferido;
    }

    public String getCelularreferido() {
        return celularreferido;
    }

    public void setCelularreferido(String celularreferido) {
        this.celularreferido = celularreferido;
    }

    public String getMailreferido() {
        return mailreferido;
    }

    public void setMailreferido(String mailreferido) {
        this.mailreferido = mailreferido;
    }

    public String getPensionarse() {
        return pensionarse;
    }

    public void setPensionarse(String pensionarse) {
        this.pensionarse = pensionarse;
    }

    public String getClientesolicito() {
        return clientesolicito;
    }

    public void setClientesolicito(String clientesolicito) {
        this.clientesolicito = clientesolicito;
    }

    public String getAccionrealizo() {
        return accionrealizo;
    }

    public void setAccionrealizo(String accionrealizo) {
        this.accionrealizo = accionrealizo;
    }

    public String getProximospasos() {
        return proximospasos;
    }

    public void setProximospasos(String proximospasos) {
        this.proximospasos = proximospasos;
    }

    public String getTipopension() {
        return tipopension;
    }

    public void setTipopension(String tipopension) {
        this.tipopension = tipopension;
    }

    public String getClientesesperando() {
        return clientesesperando;
    }

    public void setClientesesperando(String clientesesperando) {
        this.clientesesperando = clientesesperando;
    }

    public String getRelacionreferido() {
        return relacionreferido;
    }

    public void setRelacionreferido(String relacionreferido) {
        this.relacionreferido = relacionreferido;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getSolicitoreferido() {
        return solicitoreferido;
    }

    public void setSolicitoreferido(String solicitoreferido) {
        this.solicitoreferido = solicitoreferido;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public BigDecimal getIdejecutivo() {
        return idejecutivo;
    }

    public void setIdejecutivo(BigDecimal idejecutivo) {
        this.idejecutivo = idejecutivo;
    }

    public BigDecimal getIdreferencia() {
        return idreferencia;
    }

    public void setIdreferencia(BigDecimal idreferencia) {
        this.idreferencia = idreferencia;
    }

    public BigDecimal getAcciones() {
        return acciones;
    }

    public void setAcciones(BigDecimal acciones) {
        this.acciones = acciones;
    }

    public BigDecimal getIdsucursal() {
        return idsucursal;
    }

    public void setIdsucursal(BigDecimal idsucursal) {
        this.idsucursal = idsucursal;
    }

    @Override
    public String toString() {
        return ("DatosReferidos{"
                + "apellidos="
                + apellidos
                + ", idreferencia="
                + idreferencia
                + ", acciones="
                + acciones
                + ", calle="
                + calle
                + ", canal="
                + canal
                + ", comuna="
                + comuna
                + ", correo="
                + correo
                + ", dpto_casa="
                + dpto_casa
                + ", nombre="
                + nombre
                + ", num_dpto_casa="
                + num_dpto_casa
                + ", region="
                + region
                + ", rut="
                + rut
                + ", score="
                + score
                + ", telefono="
                + telefono
                + ", telefono2="
                + telefono2
                + ", telefono3="
                + telefono3
                + ", version="
                + version
                + ", comentarios="
                + comentarios
                + ", usuario="
                + usuario
                + ", cuantoduroatencion="
                + cuantoduroatencion
                + ", nombrereferido="
                + nombrereferido
                + ", celularreferido="
                + celularreferido
                + ", mailreferido="
                + mailreferido
                + ", pensionarse="
                + pensionarse
                + ", clientesolicito="
                + clientesolicito
                + ", accionrealizo="
                + accionrealizo
                + ", proximospasos="
                + proximospasos
                + ", tipopension="
                + tipopension
                + ", clientesesperando="
                + clientesesperando
                + ", relacionreferido="
                + relacionreferido
                + ", sexo="
                + sexo
                + ", solicitoreferido="
                + solicitoreferido
                + ", idejecutivo="
                + idejecutivo
                + ", fechanacimiento="
                + fechanacimiento
                + ", idsucursal="
                + idsucursal
                + '}');
    }
}
