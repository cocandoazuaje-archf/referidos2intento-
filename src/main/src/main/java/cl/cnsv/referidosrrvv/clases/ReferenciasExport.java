package cl.cnsv.referidosrrvv.clases;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import org.eclipse.persistence.sessions.serializers.Serializer;

//@SqlResultSetMapping(name = "AggregateStatsResult", classes = {
//        @ConstructorResult(targetClass = ReferenciasExport.class,
//                columns = {
//                        @ColumnResult(name = "id"),
//                        @ColumnResult(name = "nombre"),
//                        @ColumnResult(name = "FECHANAC"),
//                        @ColumnResult(name = "rut"),
//                        @ColumnResult(name = "correo"),
//                        @ColumnResult(name = "fecha"),
//                        @ColumnResult(name = "canal"),
//                        @ColumnResult(name = "comuna"),
//                        @ColumnResult(name = "region"),
//                        @ColumnResult(name = "ejecutivo"),
//                        @ColumnResult(name = "estado"),
//                        @ColumnResult(name = "ultimos2Coments"),
//                        @ColumnResult(name = "telefonos"),
//                        @ColumnResult(name = "telefonos2"),
//                        @ColumnResult(name = "telefonos3"),
//                        @ColumnResult(name = "pensionarse"),
//                        @ColumnResult(name = "clienteSolicito"),
//                        @ColumnResult(name = "accionRealizo"),
//                        @ColumnResult(name = "tipoPension"),
//                        @ColumnResult(name = "sexo"),
//                        @ColumnResult(name = "fechaFicha"),
//                        @ColumnResult(name = "fechaAccion"),
//                        @ColumnResult(name = "encDigitalPreg1"),
//                        @ColumnResult(name = "encDigitalResp1"),
//                        @ColumnResult(name = "encDigitalPreg2"),
//                        @ColumnResult(name = "encDigitalResp2"),
//                        @ColumnResult(name = "encDigitalPreg3"),
//                        @ColumnResult(name = "encDigitalResp3"),
//                        @ColumnResult(name = "encDigitalPreg4"),
//                        @ColumnResult(name = "encDigitalResp4")
//                })
//})
@Entity
public class ReferenciasExport implements Serializable {

    @Id
    private BigDecimal ID;

    @Column
    private String nombre;

    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date FECHANAC;

    @Column
    private String rut;

    @Column
    private String correo;

    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;

    @Column
    private String canal;

    @Column
    private String comuna;

    @Column
    private String region;

    @Column
    private String ejecutivo;

    @Column
    private String estado;

    @Column
    private String ultimos2Coments;

    @Column
    private String telefonos;

    @Column
    private String telefonos2;

    @Column
    private String telefonos3;

    @Column
    private String pensionarse;

    @Column
    private String clienteSolicito;

    @Column
    private String accionRealizo;

    @Column
    private String tipoPension;

    @Column
    private String sexo;

    @Column
    private String fechaFicha;

    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAccion;

    @Column
    private String encDigitalPreg1;

    @Column
    private int encDigitalResp1;

    @Column
    private String encDigitalPreg2;

    @Column
    private int encDigitalResp2;

    @Column
    private String encDigitalPreg3;

    @Column
    private int encDigitalResp3;

    @Column
    private String encDigitalPreg4;

    @Column
    private String encDigitalResp4;

    @Column
    private String afp;

    @Column
    private String prima;

    public ReferenciasExport() {
        // constructor vacio
    }

    public ReferenciasExport(
            BigDecimal ID,
            String nombre,
            Date FECHANAC,
            String rut,
            String correo,
            Date fecha,
            String canal,
            String comuna,
            String region,
            String ejecutivo,
            String estado,
            String ultimos2Coments,
            String telefonos,
            String telefonos2,
            String telefonos3,
            String pensionarse,
            String clienteSolicito,
            String accionRealizo,
            String tipoPension,
            String sexo,
            String fechaFicha,
            Date fechaAccion,
            String encDigitalPreg1,
            int encDigitalResp1,
            String encDigitalPreg2,
            int encDigitalResp2,
            String encDigitalPreg3,
            int encDigitalResp3,
            String encDigitalPreg4,
            String encDigitalResp4,
            String afp,
            String prima) {
        this.ID = ID;
        this.nombre = nombre;
        this.FECHANAC = FECHANAC;
        this.rut = rut;
        this.correo = correo;
        this.fecha = fecha;
        this.canal = canal;
        this.comuna = comuna;
        this.region = region;
        this.ejecutivo = ejecutivo;
        this.estado = estado;
        this.ultimos2Coments = ultimos2Coments;
        this.telefonos = telefonos;
        this.telefonos2 = telefonos2;
        this.telefonos3 = telefonos3;
        this.pensionarse = pensionarse;
        this.clienteSolicito = clienteSolicito;
        this.accionRealizo = accionRealizo;
        this.tipoPension = tipoPension;
        this.sexo = sexo;
        this.fechaFicha = fechaFicha;
        this.fechaAccion = fechaAccion;
        this.encDigitalPreg1 = encDigitalPreg1;
        this.encDigitalResp1 = encDigitalResp1;
        this.encDigitalPreg2 = encDigitalPreg2;
        this.encDigitalResp2 = encDigitalResp2;
        this.encDigitalPreg3 = encDigitalPreg3;
        this.encDigitalResp3 = encDigitalResp3;
        this.encDigitalPreg4 = encDigitalPreg4;
        this.afp = afp;
        this.prima = prima;
    }

    public String getAfp() {
        return afp;
    }

    public void setAfp(String afp) {
        this.afp = afp;
    }

    public String getPrima() {
        return prima;
    }

    public void setPrima(String prima) {
        this.prima = prima;
    }

    public BigDecimal getID() {
        return ID;
    }

    public void setID(BigDecimal ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFECHANAC() {
        return FECHANAC;
    }

    public void setFECHANAC(Date FECHANAC) {
        this.FECHANAC = FECHANAC;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUltimos2Coments() {
        return ultimos2Coments;
    }

    public void setUltimos2Coments(String ultimos2Coments) {
        this.ultimos2Coments = ultimos2Coments;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getTelefonos2() {
        return telefonos2;
    }

    public void setTelefonos2(String telefonos2) {
        this.telefonos2 = telefonos2;
    }

    public String getTelefonos3() {
        return telefonos3;
    }

    public void setTelefonos3(String telefonos3) {
        this.telefonos3 = telefonos3;
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

    public String getTipoPension() {
        return tipoPension;
    }

    public void setTipoPension(String tipoPension) {
        this.tipoPension = tipoPension;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechaFicha() {
        return fechaFicha;
    }

    public void setFechaFicha(String fechaFicha) {
        this.fechaFicha = fechaFicha;
    }

    public Date getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(Date fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public String getEncDigitalPreg1() {
        return encDigitalPreg1;
    }

    public void setEncDigitalPreg1(String encDigitalPreg1) {
        this.encDigitalPreg1 = encDigitalPreg1;
    }

    public int getEncDigitalResp1() {
        return encDigitalResp1;
    }

    public void setEncDigitalResp1(int encDigitalResp1) {
        this.encDigitalResp1 = encDigitalResp1;
    }

    public String getEncDigitalPreg2() {
        return encDigitalPreg2;
    }

    public void setEncDigitalPreg2(String encDigitalPreg2) {
        this.encDigitalPreg2 = encDigitalPreg2;
    }

    public int getEncDigitalResp2() {
        return encDigitalResp2;
    }

    public void setEncDigitalResp2(int encDigitalResp2) {
        this.encDigitalResp2 = encDigitalResp2;
    }

    public String getEncDigitalPreg3() {
        return encDigitalPreg3;
    }

    public void setEncDigitalPreg3(String encDigitalPreg3) {
        this.encDigitalPreg3 = encDigitalPreg3;
    }

    public int getEncDigitalResp3() {
        return encDigitalResp3;
    }

    public void setEncDigitalResp3(int encDigitalResp3) {
        this.encDigitalResp3 = encDigitalResp3;
    }

    public String getEncDigitalPreg4() {
        return encDigitalPreg4;
    }

    public void setEncDigitalPreg4(String encDigitalPreg4) {
        this.encDigitalPreg4 = encDigitalPreg4;
    }

    public String getEncDigitalResp4() {
        return encDigitalResp4;
    }

    public void setEncDigitalResp4(String encDigitalResp4) {
        this.encDigitalResp4 = encDigitalResp4;
    }
}
