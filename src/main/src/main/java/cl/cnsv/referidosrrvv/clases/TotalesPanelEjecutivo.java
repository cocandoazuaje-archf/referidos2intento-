/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cow
 */
@Entity
@XmlRootElement
public class TotalesPanelEjecutivo implements Serializable {

    @Column(name = "TOTAL")
    private int total;

    @Column(name = "TINACTIVIDAD")
    private int tinactividad;

    @Column(name = "PINACTIVIDAD")
    private int pinactividad;

    @Column(name = "COLORINACTIVIDAD")
    private String colorinactividad;

    @Column(name = "COLORBINACTIVIDAD")
    private String colorbinactividad;

    @Column(name = "TINTERESADOS")
    private int tinteresados;

    @Column(name = "PINTERESADOS")
    private float pinteresados;

    @Column(name = "COLORINTERESADOS")
    private String colorinteresados;

    @Column(name = "COLORBINTERESADOS")
    private String colorbinteresados;

    @Column(name = "TREAGENDADOS")
    private int treagendados;

    @Column(name = "PREAGENDADOS")
    private float preagendados;

    @Column(name = "COLORREAGENDADOS")
    private String colorreagendados;

    @Column(name = "COLORBREAGENDADOS")
    private String colorbreagendados;

    @Column(name = "TDERIVADOS")
    private int tderivados;

    @Column(name = "PDERIVADOS")
    private float pderivados;

    @Column(name = "COLORDERIVADOS")
    private String colorderivados;

    @Column(name = "COLORBDERIVADOS")
    private String colorbderivados;

    @Column(name = "TSINEXITO")
    private int tsinexito;

    @Column(name = "PSINEXITO")
    private float psinexito;

    @Column(name = "COLORSINEXITO")
    private String colorsinexito;

    @Column(name = "COLORBSINEXITO")
    private String colorbsinexito;

    @Column(name = "TCERRADOS")
    private int tcerrados;

    @Column(name = "PCERRADOS")
    private float pcerrados;

    @Column(name = "COLORCERRADOS")
    private String colorcerrados;

    @Column(name = "COLORBCERRADOS")
    private String colorbcerrados;

    @Column(name = "TATENDIDOS")
    private float tatendidos;

    @Column(name = "PATENDIDOS")
    private float patendidos;

    @Column(name = "COLORATENDIDOS")
    private String coloratendidos;

    @Column(name = "COLORBATENDIDOS")
    private String colorbatendidos;

    @Column(name = "TGENESIS")
    private float tgenesis;

    @Column(name = "PGENESIS")
    private float pgenesis;

    @Column(name = "COLORGENESIS")
    private String colorgenesis;

    @Column(name = "COLORBGENESIS")
    private String colorbgenesis;

    @Id
    @Column(name = "ID")
    private String id;

    public TotalesPanelEjecutivo() {
        // constructor vacio
    }

    public TotalesPanelEjecutivo(
            int total,
            int tinactividad,
            int pinactivdad,
            String colorinactividad,
            String colorbinactividad,
            int tinteresados,
            float pinteresados,
            String colorinteresados,
            String colorbinteresados,
            int treagendados,
            float preagendados,
            String colorreagendados,
            String colorbreagendados,
            int tderivados,
            float pderivados,
            String colorderivados,
            String colorbderivados,
            int tsinexito,
            float psinexito,
            String colorsinexito,
            String colorbsinexito,
            int tcerrados,
            float pcerrados,
            String colorcerrados,
            String colorbcerrados,
            float tatendidos,
            float patendidos,
            String coloratendidos,
            String colorbatendidos,
            float tgenesis,
            float pgensis,
            String colorgenesis,
            String colorbgenesis,
            String id) {
        this.total = total;
        this.tinactividad = tinactividad;
        this.pinactividad = pinactivdad;
        this.colorinactividad = colorinactividad;
        this.colorbinactividad = colorbinactividad;
        this.tinteresados = tinteresados;
        this.pinteresados = pinteresados;
        this.colorinteresados = colorinteresados;
        this.colorbinteresados = colorbinteresados;
        this.treagendados = treagendados;
        this.preagendados = preagendados;
        this.colorreagendados = colorreagendados;
        this.colorbreagendados = colorbreagendados;
        this.tderivados = tderivados;
        this.pderivados = pderivados;
        this.colorderivados = colorderivados;
        this.colorbderivados = colorbderivados;
        this.tsinexito = tsinexito;
        this.psinexito = psinexito;
        this.colorsinexito = colorsinexito;
        this.colorbsinexito = colorbsinexito;
        this.tcerrados = tcerrados;
        this.pcerrados = pcerrados;
        this.colorcerrados = colorcerrados;
        this.colorbcerrados = colorbcerrados;
        this.tatendidos = tatendidos;
        this.patendidos = patendidos;
        this.coloratendidos = coloratendidos;
        this.colorbatendidos = colorbatendidos;
        this.tgenesis = tgenesis;
        this.pgenesis = pgensis;
        this.colorgenesis = colorgenesis;
        this.colorbgenesis = colorbgenesis;
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTinactividad() {
        return tinactividad;
    }

    public void setTinactividad(int tinactividad) {
        this.tinactividad = tinactividad;
    }

    public int getPinactividad() {
        return pinactividad;
    }

    public void setPinactividad(int pinactividad) {
        this.pinactividad = pinactividad;
    }

    public String getColorinactividad() {
        return colorinactividad;
    }

    public void setColorinactividad(String colorinactividad) {
        this.colorinactividad = colorinactividad;
    }

    public String getColorbinactividad() {
        return colorbinactividad;
    }

    public void setColorbinactividad(String colorbinactividad) {
        this.colorbinactividad = colorbinactividad;
    }

    public int getTinteresados() {
        return tinteresados;
    }

    public void setTinteresados(int tinteresados) {
        this.tinteresados = tinteresados;
    }

    public float getPinteresados() {
        return pinteresados;
    }

    public void setPinteresados(float pinteresados) {
        this.pinteresados = pinteresados;
    }

    public String getColorinteresados() {
        return colorinteresados;
    }

    public void setColorinteresados(String colorinteresados) {
        this.colorinteresados = colorinteresados;
    }

    public String getColorbinteresados() {
        return colorbinteresados;
    }

    public void setColorbinteresados(String colorbinteresados) {
        this.colorbinteresados = colorbinteresados;
    }

    public int getTreagendados() {
        return treagendados;
    }

    public void setTreagendados(int treagendados) {
        this.treagendados = treagendados;
    }

    public float getPreagendados() {
        return preagendados;
    }

    public void setPreagendados(float preagendados) {
        this.preagendados = preagendados;
    }

    public String getColorreagendados() {
        return colorreagendados;
    }

    public void setColorreagendados(String colorreagendados) {
        this.colorreagendados = colorreagendados;
    }

    public String getColorbreagendados() {
        return colorbreagendados;
    }

    public void setColorbreagendados(String colorbreagendados) {
        this.colorbreagendados = colorbreagendados;
    }

    public int getTderivados() {
        return tderivados;
    }

    public void setTderivados(int tderivados) {
        this.tderivados = tderivados;
    }

    public float getPderivados() {
        return pderivados;
    }

    public void setPderivados(float pderivados) {
        this.pderivados = pderivados;
    }

    public String getColorderivados() {
        return colorderivados;
    }

    public void setColorderivados(String colorderivados) {
        this.colorderivados = colorderivados;
    }

    public String getColorbderivados() {
        return colorbderivados;
    }

    public void setColorbderivados(String colorbderivados) {
        this.colorbderivados = colorbderivados;
    }

    public int getTsinexito() {
        return tsinexito;
    }

    public void setTsinexito(int tsinexito) {
        this.tsinexito = tsinexito;
    }

    public float getPsinexito() {
        return psinexito;
    }

    public void setPsinexito(float psinexito) {
        this.psinexito = psinexito;
    }

    public String getColorsinexito() {
        return colorsinexito;
    }

    public void setColorsinexito(String colorsinexito) {
        this.colorsinexito = colorsinexito;
    }

    public String getColorbsinexito() {
        return colorbsinexito;
    }

    public void setColorbsinexito(String colorbsinexito) {
        this.colorbsinexito = colorbsinexito;
    }

    public int getTcerrados() {
        return tcerrados;
    }

    public void setTcerrados(int tcerrados) {
        this.tcerrados = tcerrados;
    }

    public float getPcerrados() {
        return pcerrados;
    }

    public void setPcerrados(float pcerrados) {
        this.pcerrados = pcerrados;
    }

    public String getColorcerrados() {
        return colorcerrados;
    }

    public void setColorcerrados(String colorcerrados) {
        this.colorcerrados = colorcerrados;
    }

    public String getColorbcerrados() {
        return colorbcerrados;
    }

    public void setColorbcerrados(String colorbcerrados) {
        this.colorbcerrados = colorbcerrados;
    }

    public float getTatendidos() {
        return tatendidos;
    }

    public void setTatendidos(float tatendidos) {
        this.tatendidos = tatendidos;
    }

    public float getPatendidos() {
        return patendidos;
    }

    public void setPatendidos(float patendidos) {
        this.patendidos = patendidos;
    }

    public String getColoratendidos() {
        return coloratendidos;
    }

    public void setColoratendidos(String coloratendidos) {
        this.coloratendidos = coloratendidos;
    }

    public String getColorbatendidos() {
        return colorbatendidos;
    }

    public void setColorbatendidos(String colorbatendidos) {
        this.colorbatendidos = colorbatendidos;
    }

    public float getTgenesis() {
        return tgenesis;
    }

    public void setTgenesis(float tgenesis) {
        this.tgenesis = tgenesis;
    }

    public float getPgenesis() {
        return pgenesis;
    }

    public void setPgenesis(float pgenesis) {
        this.pgenesis = pgenesis;
    }

    public String getColorgenesis() {
        return colorgenesis;
    }

    public void setColorgenesis(String colorgenesis) {
        this.colorgenesis = colorgenesis;
    }

    public String getColorbgenesis() {
        return colorbgenesis;
    }

    public void setColorbgenesis(String colorbgenesis) {
        this.colorbgenesis = colorbgenesis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
