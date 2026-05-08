/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author cow
 */
public class ReferenciasOut {

    private BigDecimal id;
    private String nombre;
    private String rut;
    private String correo;
    private Date fecha;
    private String canal;
    private String comuna;
    private String region;
    private String ejecutivo;
    private BigDecimal estadoId;
    private String nombreEstado;

    public ReferenciasOut() {
        // constructor vacio
    }

    public ReferenciasOut(
            BigDecimal id,
            String nombre,
            String rut,
            String correo,
            Date fecha,
            String canal,
            String comuna,
            String region,
            String ejecutivo,
            BigDecimal estadoId,
            String nombreEstado) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
        this.fecha = fecha;
        this.canal = canal;
        this.comuna = comuna;
        this.region = region;
        this.ejecutivo = ejecutivo;
        this.estadoId = estadoId;
        this.nombreEstado = nombreEstado;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEjecutivo() {
        return ejecutivo;
    }

    public void setEjecutivo(String ejecutivo) {
        this.ejecutivo = ejecutivo;
    }

    public BigDecimal getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(BigDecimal estadoId) {
        this.estadoId = estadoId;
    }
}
