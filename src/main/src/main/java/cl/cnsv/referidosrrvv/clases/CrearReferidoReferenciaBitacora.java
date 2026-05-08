/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.AccionesJpaController;
import cl.cnsv.referidosrrvv.controller.BitacorasJpaController;
import cl.cnsv.referidosrrvv.controller.ReferenciasJpaController;
import cl.cnsv.referidosrrvv.controller.ReferidosJpaControllerOLD;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Referidos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class CrearReferidoReferenciaBitacora {

    private String noSePudoInsertar = "No se pudo insertar el referido -> ";
    private static final Logger LOGGER = Logger.getLogger(
            CrearReferidoReferenciaBitacora.class.getName());

    public CrearReferidoReferenciaBitacora() throws NamingException {
        // Constructor vacio
    }

    public List<ErrorCrearReferidoReferenciaBitacoraOut> cargar(
            List<EntidadDeCargaJs> rjsList,
            UserTransaction utx,
            EntityManager em) throws Exception {
        ReferidosJpaControllerOLD rc = new ReferidosJpaControllerOLD(utx, null, em);
        AccionesJpaController a = new AccionesJpaController(null, null, em);
        ReferenciasJpaController refc = new ReferenciasJpaController(utx, null, em);
        BitacorasJpaController bc = new BitacorasJpaController(utx, null, em);
        List<ErrorCrearReferidoReferenciaBitacoraOut> ecrrbList = new ArrayList<>();

        Integer index = 0;
        Resources.listaParaProspeccionConHilo.clear();
        for (EntidadDeCargaJs rjs : rjsList) {
            try {
                String vRut = String.valueOf(new Date().getTime());
                vRut = "SR" + vRut.substring(5, 13);
                vRut = (rjs.getRUT() == null || rjs.getRUT().length() <= 1)
                        ? vRut
                        : rjs.getRUT();
                vRut = vRut.toUpperCase();

                // obtiene el rut sin DV
                String vRutSDV = null;
                vRutSDV = ValidaRutProspeccion.obtener(vRut);

                if (rjs.getNOMBRE().length() <= 1) {
                    throw new NullPointerException("Nombre es requerido");
                }

                Referidos r = new Referidos();
                Referidos referidoExiste;

                VerificarReferenciaActivaReferido vrar = new VerificarReferenciaActivaReferido();
                OutVerificaReferenciaActiva oVrar = vrar.verificar(vRut, utx, em);

                referidoExiste = oVrar.getReferido();
                if (referidoExiste.getId() == null) {
                    referidoExiste = null;
                }

                boolean referenciaAlMenosUnaAbierta = oVrar.isTieneReferenciaActiva();

                if (referenciaAlMenosUnaAbierta == true) {
                    ErrorCrearReferidoReferenciaBitacoraOut ecrrb = new ErrorCrearReferidoReferenciaBitacoraOut();
                    ecrrb.setRUT(rjs.getRUT());
                    ecrrb.setAPELLIDOS(rjs.getAPELLIDOS());
                    ecrrb.setCANAL(rjs.getCANALNAME());
                    ecrrb.setCOMUNA(rjs.getCOMUNA());
                    ecrrb.setE_MAIL(rjs.getCORREO());
                    ecrrb.setNOMBRE(rjs.getNOMBRE());
                    ecrrb.setRUT(rjs.getRUT());
                    ecrrb.setTELEFONO(rjs.getTELEFONO());
                    ecrrb.setTELEFONO2(rjs.getTELEFONO2());
                    ecrrb.setTELEFONO3(rjs.getTELEFONO3());
                    ecrrb.setERROR(noSePudoInsertar
                            + rjs.getNOMBRE()
                            + ", ERROR -> Ya posee una referencia activa.");
                    ecrrbList.add(ecrrb);
                    continue;
                }

                Date fechaRecepcion = continuar2(referenciaAlMenosUnaAbierta, rjs, r, vRut);

                continuar(index, bc, refc, a, rjs, vRutSDV, vRut, referidoExiste, rc, r, fechaRecepcion);
            } catch (Exception e) {
                LOGGER.info(e);
                ErrorCrearReferidoReferenciaBitacoraOut ecrrb = new ErrorCrearReferidoReferenciaBitacoraOut();
                ecrrb.setRUT(rjs.getRUT());
                ecrrb.setAPELLIDOS(rjs.getAPELLIDOS());
                ecrrb.setCANAL(rjs.getCANALNAME());
                ecrrb.setCOMUNA(rjs.getCOMUNA());
                ecrrb.setE_MAIL(rjs.getCORREO());
                ecrrb.setNOMBRE(rjs.getNOMBRE());
                ecrrb.setRUT(rjs.getRUT());
                ecrrb.setTELEFONO(rjs.getTELEFONO());
                ecrrb.setTELEFONO2(rjs.getTELEFONO2());
                ecrrb.setTELEFONO3(rjs.getTELEFONO3());
                ecrrb.setERROR(
                        noSePudoInsertar + rjs.getNOMBRE() + ", ERROR -> " + e.toString());
                ecrrbList.add(ecrrb);
            }
        }

        return ecrrbList;
    }

    private void continuar(Integer index, BitacorasJpaController bc, ReferenciasJpaController refc,
            AccionesJpaController a, EntidadDeCargaJs rjs, String vRutSDV, String vRut, Referidos referidoExiste,
            ReferidosJpaControllerOLD rc, Referidos r, Date fechaRecepcion) throws RollbackFailureException, Exception {
        // actualizar prospeccion
        if ((vRutSDV.length() > 0) && (!vRut.contains("SR"))) {
        }

        if (referidoExiste == null) {
            rc.create(r);
            Resources.listaParaProspeccionConHilo.add(r);
        }

        Acciones acc = a.findAcciones(BigDecimal.ONE);

        Referencias rf = new Referencias();
        rf.setAccionId(acc);
        rf.setCanalname(rjs.getCANALNAME());
        rf.setFecha(fechaRecepcion);
        rf.setVersion(BigInteger.ONE);
        rf.setReferidoId(r);
        rf.setUsuario(rjs.getUSUARIO());
        rf.setOwnere(rjs.getOWNERE());
        rf.setOwnerename(rjs.getOWNERENAME());

        refc.create(rf);

        Bitacoras b = new Bitacoras();
        b.setFecha(new Date());
        String comentario = "REFERENCIA CARGADA MEDIANTE ARCHIVO DE CARGA PARA REFERIDOS -> ";
        b.setComentarios((rjs.getCOMENTARIOS() != null) ? rjs.getCOMENTARIOS() : comentario);
        b.setVersion(BigInteger.ONE);
        b.setUsuario(rjs.getUSUARIO());
        b.setReferenciaId(rf);

        bc.create(b);
        index++;
        LOGGER.info("** ["
                + index.toString()
                + "]  -> Referido insertado  -> "
                + r.getRut()
                + " -> "
                + " -> "
                + vRutSDV
                + " -> "
                + r.getNombre()
                + " -> "
                + r.getScore());
    }

    private Date continuar2(boolean referenciaAlMenosUnaAbierta, EntidadDeCargaJs rjs, Referidos r, String vRut) {

        r.setApellido(((rjs.getAPELLIDOS() == null ? "" : rjs.getAPELLIDOS())).trim()
                .toUpperCase());
        r.setCalle(rjs.getCALLE());
        r.setComuna(rjs.getCOMUNA());
        r.setCorreo(rjs.getCORREO());
        r.setDptoCasa(rjs.getDPTO_CASA());
        if (rjs.getFECHA_RECEPCION() == null) {
            throw new NullPointerException("Formato de fecha incorrecto.");
        }
        Date fechaRecepcion = rjs.getFECHA_RECEPCION();
        r.setFechaIngreso(fechaRecepcion);
        r.setNombre(rjs.getNOMBRE().trim().toUpperCase());
        r.setNumDptoCasa(rjs.getNUM_DPTO_CASA());
        r.setRegion(rjs.getREGION());
        r.setRut(vRut);
        r.setScore(rjs.getSCORE());
        r.setTelefonos(rjs.getTELEFONO());
        r.setTelefonos2(rjs.getTELEFONO2());
        r.setTelefonos3(rjs.getTELEFONO3());
        r.setVersion(BigInteger.ONE);
        r.setFechanac(rjs.getFECHANAC());

        r.setPensionarse(rjs.getPensionarse());
        r.setClienteSolicito(rjs.getClienteSolicito());
        r.setAccionRealizo(rjs.getAccionRealizo());
        r.setTipoPension(rjs.getTipoPension());
        r.setSexo(rjs.getSexo());

        return fechaRecepcion;

    }

    public static class ErrorCrearReferidoReferenciaBitacoraOut {

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

        public ErrorCrearReferidoReferenciaBitacoraOut() {
            // constructor vacio
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
    }
}
