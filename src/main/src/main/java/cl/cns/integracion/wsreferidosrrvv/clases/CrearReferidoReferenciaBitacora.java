/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.AccionesJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.BitacorasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.EjecutivosJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferenciasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferidosJpaController;
import cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cns.integracion.wsreferidosrrvv.vo.DatosReferidos;
import cl.cns.integracion.wsreferidosrrvv.vo.ErrorCrearReferidoReferenciaBitacoraOut;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Referidos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        ReferidosJpaController rc = new ReferidosJpaController(em);
        AccionesJpaController a = new AccionesJpaController(em);
        ReferenciasJpaController refc = new ReferenciasJpaController(em);
        BitacorasJpaController bc = new BitacorasJpaController(em);
        List<ErrorCrearReferidoReferenciaBitacoraOut> ecrrbList = new ArrayList<>();

        for (EntidadDeCargaJs rjs : rjsList) {
            try {
                // segmentando complejidad
                continuar(rjs, utx, em);

                String vRut = String.valueOf(new Date().getTime());

                // segmentando complejidad
                Object[] valores = continuar2(rjs, utx, em, vRut);

                boolean referenciaAlMenosUnaAbierta = ((OutVerificaReferenciaActiva) valores[0])
                        .isTieneReferenciaActiva();

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
                    ecrrb.setERROR(
                            noSePudoInsertar
                                    + rjs.getNOMBRE()
                                    + ", ERROR -> Ya posee una referencia activa.");
                    ecrrbList.add(ecrrb);
                    continue;
                }

                Referidos r = continuar8(
                        referenciaAlMenosUnaAbierta,
                        rjs,
                        ecrrbList,
                        rc,
                        valores,
                        vRut);

                // segmentando complejidad
                continuar4(rjs, refc, a, r, bc);
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

    private Object[] continuar2(
            EntidadDeCargaJs rjs,
            UserTransaction utx,
            EntityManager em,
            String vRut) {
        vRut = "SR" + vRut.substring(5, 13);
        vRut = (rjs.getRUT() == null || rjs.getRUT().length() <= 1)
                ? vRut
                : rjs.getRUT();
        vRut = vRut.toUpperCase();

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

        Object[] regresar = new Object[2];
        regresar[0] = oVrar;
        regresar[1] = r;
        regresar[2] = referidoExiste;

        return regresar;
    }

    private void continuar(
            EntidadDeCargaJs rjs,
            UserTransaction utx,
            EntityManager em) {
        String vRut = String.valueOf(new Date().getTime());
        vRut = "SR" + vRut.substring(5, 13);
        vRut = (rjs.getRUT() == null || rjs.getRUT().length() <= 1)
                ? vRut
                : rjs.getRUT();
        vRut = vRut.toUpperCase();

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
    }

    public ErrorCrearReferidoReferenciaBitacoraOut cargarReferidoReferenciaBitacora(
            DatosReferidos referencia,
            UserTransaction utx,
            EntityManager em) throws Exception {
        ReferidosJpaController rc = new ReferidosJpaController(em);
        AccionesJpaController a = new AccionesJpaController(em);
        ReferenciasJpaController refc = new ReferenciasJpaController(em);
        BitacorasJpaController bc = new BitacorasJpaController(em);
        List<ErrorCrearReferidoReferenciaBitacoraOut> ecrrbList = new ArrayList<>();
        ErrorCrearReferidoReferenciaBitacoraOut ecrrb = new ErrorCrearReferidoReferenciaBitacoraOut();
        Date datefechaNacimiento = null;

        BigInteger Version = (referencia.getVersion() != null
                ? referencia.getVersion()
                : BigInteger.ONE);

        BigDecimal acciones = (referencia.getIdsucursal() != null
                ? referencia.getAcciones()
                : BigDecimal.ONE);

        try {
            String vRut = String.valueOf(new Date().getTime());
            vRut = "SR" + vRut.substring(5, 13);
            vRut = (referencia.getRut() == null || referencia.getRut().length() <= 1)
                    ? vRut
                    : referencia.getRut();
            vRut = vRut.toUpperCase().replace(".", "");

            String strCampos = "";

            try {
                Date datefechaingreso = new Date();

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                String fechaNacimiento = referencia.getFechanacimiento() != null
                        ? referencia.getFechanacimiento()
                        : " ";

                if (!fechaNacimiento.trim().equals("")) {
                    datefechaNacimiento = formatter.parse(fechaNacimiento);
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
                    continuar7(ecrrb, referencia);
                } else {
                    continuar6(
                            referencia,
                            r,
                            datefechaingreso,
                            vRut,
                            Version,
                            fechaNacimiento,
                            datefechaNacimiento,
                            referidoExiste,
                            rc);

                    continuar5(
                            a,
                            acciones,
                            referencia,
                            datefechaingreso,
                            Version,
                            r,
                            ecrrb,
                            em,
                            refc,
                            bc);
                }
            } catch (ParseException e) {
                LOGGER.error("Error :", e);
                ecrrb.setERROR("Problema del formato de la fecha");
            }
        } catch (Exception e) {
            LOGGER.error("Error :", e);

            ecrrb.setRUT(referencia.getRut());
            ecrrb.setAPELLIDOS(referencia.getApellidos());
            ecrrb.setCANAL(referencia.getCanal());
            ecrrb.setCOMUNA(referencia.getComuna());
            ecrrb.setE_MAIL(referencia.getCorreo());
            ecrrb.setNOMBRE(referencia.getNombre());
            ecrrb.setRUT(referencia.getRut());
            ecrrb.setTELEFONO(referencia.getTelefono());
            ecrrb.setTELEFONO2(referencia.getTelefono2());
            ecrrb.setTELEFONO3(referencia.getTelefono3());
            ecrrb.setERROR(
                    noSePudoInsertar + referencia.getNombre() + ", ERROR -> " + e.toString());
        }

        return ecrrb;
    }

    private void continuar7(
            ErrorCrearReferidoReferenciaBitacoraOut ecrrb,
            DatosReferidos referencia) {
        ecrrb.setRUT(referencia.getRut());
        ecrrb.setAPELLIDOS(referencia.getApellidos());
        ecrrb.setCANAL(referencia.getCanal());
        ecrrb.setCOMUNA(referencia.getComuna());
        ecrrb.setE_MAIL(referencia.getCorreo());
        ecrrb.setNOMBRE(referencia.getNombre());
        ecrrb.setRUT(referencia.getRut());
        ecrrb.setTELEFONO(referencia.getTelefono());
        ecrrb.setTELEFONO2(referencia.getTelefono2());
        ecrrb.setTELEFONO3(referencia.getTelefono3());
        ecrrb.setERROR(
                noSePudoInsertar
                        + referencia.getNombre()
                        + ", ERROR -> Ya posee una referencia activa.");
    }

    private void continuar3(Referidos r, EntidadDeCargaJs rjs, String vRut) {
        r.setApellido(
                ((rjs.getAPELLIDOS() == null ? "" : rjs.getAPELLIDOS())).trim()
                        .toUpperCase());
        r.setCalle(rjs.getCALLE());
        r.setComuna(rjs.getCOMUNA());
        r.setCorreo(rjs.getCORREO());
        r.setDptoCasa(rjs.getDPTO_CASA());
        if (rjs.getFECHA_RECEPCION() == null) {
            throw new NullPointerException("Formato de fecha incorrecto.");
        }
        r.setFechaIngreso(rjs.getFECHA_RECEPCION());
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
    }

    private void continuar4(
            EntidadDeCargaJs rjs,
            ReferenciasJpaController refc,
            AccionesJpaController a,
            Referidos r,
            BitacorasJpaController bc) throws PreexistingEntityException, RollbackFailureException {
        Acciones acc = a.findAcciones(BigDecimal.ONE);

        Referencias rf = new Referencias();
        rf.setAccionId(acc);
        rf.setCanalname(rjs.getCANALNAME());
        rf.setFecha(rjs.getFECHA_RECEPCION());
        rf.setVersion(BigInteger.ONE);
        rf.setReferidoId(r);
        rf.setUsuario(rjs.getUSUARIO());
        rf.setOwnere(rjs.getOWNERE());
        rf.setOwnerename(rjs.getOWNERENAME());

        refc.create(rf);

        Bitacoras b = new Bitacoras();
        b.setFecha(new Date());
        String comentario = "REFERENCIA CARGADA MEDIANTE ARCHIVO DE CARGA PARA REFERIDOS -> ";
        b.setComentarios(
                (rjs.getCOMENTARIOS() != null) ? rjs.getCOMENTARIOS() : comentario);
        b.setVersion(BigInteger.ONE);
        b.setUsuario(rjs.getUSUARIO());
        b.setReferenciaId(rf);
        bc.create(b);
    }

    private void continuar5(
            AccionesJpaController a,
            BigDecimal acciones,
            DatosReferidos referencia,
            Date datefechaingreso,
            BigInteger Version,
            Referidos r,
            ErrorCrearReferidoReferenciaBitacoraOut ecrrb,
            EntityManager em,
            ReferenciasJpaController refc,
            BitacorasJpaController bc) throws PreexistingEntityException, RollbackFailureException {
        Acciones acc = a.findAcciones(acciones);
        Referencias rf = new Referencias();
        rf.setAccionId(acc);
        rf.setCanalname(referencia.getCanal());
        rf.setFecha(datefechaingreso);
        rf.setVersion(Version);
        rf.setReferidoId(r);
        rf.setUsuario(referencia.getUsuario());
        if (referencia.getIdejecutivo() != null) {
            Ejecutivos ejecutivo = new Ejecutivos();
            EjecutivosJpaController ec = new EjecutivosJpaController(em);
            Ejecutivos eje = ec.findEjecutivos(referencia.getIdejecutivo());
            ejecutivo.setId(referencia.getIdejecutivo());
            rf.setEjecutivoId(ejecutivo);
            rf.setOwnere(eje.getCodigo());
            rf.setOwnerename(eje.getNombre());
            ecrrb.setEjecutivo(ejecutivo);
        }
        refc.create(rf);
        Bitacoras b = new Bitacoras();
        b.setFecha(new Date());
        String comentario = "REFERENCIA CARGADA MEDIANTE MANUAL DEL PENSIONADO  -> ";
        b.setComentarios(
                (referencia.getComentarios() != null)
                        ? referencia.getComentarios()
                        : comentario);
        b.setVersion(Version);
        b.setUsuario(referencia.getUsuario());
        b.setReferenciaId(rf);

        bc.create(b);
    }

    private void continuar6(
            DatosReferidos referencia,
            Referidos r,
            Date datefechaingreso,
            String vRut,
            BigInteger Version,
            String fechaNacimiento,
            Date datefechaNacimiento,
            Referidos referidoExiste,
            ReferidosJpaController rc) throws PreexistingEntityException, RollbackFailureException {
        r.setApellido(
                ((referencia.getApellidos() == null ? "" : referencia.getApellidos())).trim()
                        .toUpperCase());
        r.setCalle(referencia.getCalle());
        r.setComuna(referencia.getComuna());
        r.setCorreo(referencia.getCorreo());
        r.setDptoCasa(referencia.getDpto_casa());
        r.setFechaIngreso(datefechaingreso);
        r.setNombre(referencia.getNombre().trim().toUpperCase());
        r.setNumDptoCasa(referencia.getDpto_casa());
        r.setRegion(referencia.getRegion());
        r.setRut(vRut);
        r.setScore(referencia.getScore());
        r.setTelefonos(referencia.getTelefono());
        r.setTelefonos2(referencia.getTelefono2());
        r.setTelefonos3(referencia.getTelefono3());
        r.setVersion(Version);
        r.setFechanac(!fechaNacimiento.equals("") ? datefechaNacimiento : null);

        r.setPensionarse(referencia.getPensionarse());
        r.setClienteSolicito(referencia.getClientesolicito());
        r.setAccionRealizo(referencia.getAccionrealizo());
        r.setTipoPension(referencia.getTipopension());
        r.setSexo(referencia.getSexo());

        if (referidoExiste == null) {
            rc.create(r);
        }
    }

    private Referidos continuar8(
            boolean referenciaAlMenosUnaAbierta,
            EntidadDeCargaJs rjs,
            List<ErrorCrearReferidoReferenciaBitacoraOut> ecrrbList,
            ReferidosJpaController rc,
            Object[] valores,
            String vRut) throws PreexistingEntityException, RollbackFailureException {
        Referidos r = (Referidos) valores[1];

        // segmentando complejidad
        continuar3(r, rjs, vRut);

        Referidos referidoExiste = (Referidos) valores[2];

        if (referidoExiste == null) {
            rc.create(r);
        }

        return r;
    }
}
