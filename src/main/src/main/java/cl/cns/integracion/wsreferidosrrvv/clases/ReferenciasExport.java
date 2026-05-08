package cl.cns.integracion.wsreferidosrrvv.clases;

import java.math.BigDecimal;
import java.util.Date;

public class ReferenciasExport {

    private BigDecimal ID;
    private String nombre;
    private Date FECHANAC;
    private String rut;
    private String correo;
    private Date fecha;
    private String canal;
    private String comuna;
    private String region;
    private String ejecutivo;
    private String estado;
    private String ultimos2Coments;
    private String telefonos;
    private String telefonos2;
    private String telefonos3;
    private String pensionarse;
    private String clienteSolicito;
    private String accionRealizo;
    private String tipoPension;
    private String sexo;
    private String fechaFicha;

    public ReferenciasExport() {
        // constructor vacio
    }

    public String getFechaFicha() {
        return fechaFicha;
    }

    public void setFechaFicha(String fechaFicha) {
        this.fechaFicha = fechaFicha;
    }

    /**
     * @return BigDecimal return the ID
     */
    public BigDecimal getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(BigDecimal ID) {
        this.ID = ID;
    }

    /**
     * @return String return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return Date return the FECHANAC
     */
    public Date getFECHANAC() {
        return FECHANAC;
    }

    /**
     * @param FECHANAC the FECHANAC to set
     */
    public void setFECHANAC(Date FECHANAC) {
        this.FECHANAC = FECHANAC;
    }

    /**
     * @return String return the rut
     */
    public String getRut() {
        return rut;
    }

    /**
     * @param rut the rut to set
     */
    public void setRut(String rut) {
        this.rut = rut;
    }

    /**
     * @return String return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return Date return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return String return the canal
     */
    public String getCanal() {
        return canal;
    }

    /**
     * @param canal the canal to set
     */
    public void setCanal(String canal) {
        this.canal = canal;
    }

    /**
     * @return String return the comuna
     */
    public String getComuna() {
        return comuna;
    }

    /**
     * @param comuna the comuna to set
     */
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    /**
     * @return String return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return String return the ejecutivo
     */
    public String getEjecutivo() {
        return ejecutivo;
    }

    /**
     * @param ejecutivo the ejecutivo to set
     */
    public void setEjecutivo(String ejecutivo) {
        this.ejecutivo = ejecutivo;
    }

    /**
     * @return String return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return String return the ultimos2Coments
     */
    public String getUltimos2Coments() {
        return ultimos2Coments;
    }

    /**
     * @param ultimos2Coments the ultimos2Coments to set
     */
    public void setUltimos2Coments(String ultimos2Coments) {
        this.ultimos2Coments = ultimos2Coments;
    }

    /**
     * @return String return the telefonos
     */
    public String getTelefonos() {
        return telefonos;
    }

    /**
     * @param telefonos the telefonos to set
     */
    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    /**
     * @return String return the pensionarse
     */
    public String getPensionarse() {
        return pensionarse;
    }

    /**
     * @param pensionarse the pensionarse to set
     */
    public void setPensionarse(String pensionarse) {
        this.pensionarse = pensionarse;
    }

    /**
     * @return String return the clienteSolicito
     */
    public String getClienteSolicito() {
        return clienteSolicito;
    }

    /**
     * @param clienteSolicito the clienteSolicito to set
     */
    public void setClienteSolicito(String clienteSolicito) {
        this.clienteSolicito = clienteSolicito;
    }

    /**
     * @return String return the accionRealizo
     */
    public String getAccionRealizo() {
        return accionRealizo;
    }

    /**
     * @param accionRealizo the accionRealizo to set
     */
    public void setAccionRealizo(String accionRealizo) {
        this.accionRealizo = accionRealizo;
    }

    /**
     * @return String return the tipoPension
     */
    public String getTipoPension() {
        return tipoPension;
    }

    /**
     * @param tipoPension the tipoPension to set
     */
    public void setTipoPension(String tipoPension) {
        this.tipoPension = tipoPension;
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
     * @return String return the telefonos2
     */
    public String getTelefonos2() {
        return telefonos2;
    }

    /**
     * @param telefonos2 the telefonos2 to set
     */
    public void setTelefonos2(String telefonos2) {
        this.telefonos2 = telefonos2;
    }

    /**
     * @return String return the telefonos3
     */
    public String getTelefonos3() {
        return telefonos3;
    }

    /**
     * @param telefonos3 the telefonos3 to set
     */
    public void setTelefonos3(String telefonos3) {
        this.telefonos3 = telefonos3;
    }
}
