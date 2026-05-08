/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.BitacorasJpaController;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.RespEncuesta;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.transaction.UserTransaction;

/**
 *
 * @author cow
 */
public class ExportarDataTable {

    private String undef = "undefined";
    private String valAnd = " and ";

    public ExportarDataTable() {
        // constructor vacio
    }

    public List<ReferenciasExport> exportar(
            String suc,
            String anio,
            String mes,
            String sup,
            String id,
            UserTransaction utx,
            EntityManager em) throws UnsupportedEncodingException, IOException, ServletException {
        String varname = Resources.sqlExportarDataTable();
        String search = id;
        search = search.replace(" ", "%");
        String varname1 = varname.replace("?1", search);

        // condicion para supervisor
        String supParam = sup;
        if (!undef.equals(supParam)) {
            supParam = " AND OWNERE IN (" + supParam + ")";
        } else {
            supParam = "";
        }
        String varname2 = varname1.replace("?6", supParam);

        // condicion para mes y año
        String varname3;
        String cExtract = " EXTRACT(year FROM FECHA) = "
                + anio
                + ((!undef.equals(mes)) ? " and EXTRACT(month FROM FECHA) = " + mes : "");

        String valorAnioMes = (undef.equals(anio)) ? "" : valAnd + cExtract;
        varname3 = varname2.replace("?7", valorAnioMes);

        // condicion para sucursal ejecutivo
        String varname4;
        String cSentenciaSuc = "  sucursal_id=" + suc;

        String valorSuc = (undef.equals(suc)) ? "" : valAnd + cSentenciaSuc;
        varname4 = varname3.replace("?8", valorSuc);

        // System.out.println("************ varname4 ****************");
        // System.out.println(varname4);
        // System.out.println("****************************");
        Query q = em.createNativeQuery(varname4, ReferenciasExport.class);
        List<ReferenciasExport> result = new LinkedList<>();

        result = q.getResultList();

        return result;
    }

    public List<ReferenciasExport> exportar2(
            String suc,
            String anio,
            String mes,
            String sup,
            String id,
            UserTransaction utx,
            EntityManager em) throws UnsupportedEncodingException, IOException, ServletException {
        String varname = Resources.sqlDerivarMasivoDetalExportarDataTable();
        String search = id;
        search = search.replace(" ", "%");
        String varname1 = varname.replace("?1", search);

        // condicion para supervisor
        String supParam = sup;
        if (!undef.equals(supParam)) {
            supParam = " AND OWNERE IN (" + supParam + ")";
        } else {
            supParam = "";
        }
        String varname2 = varname1.replace("?6", supParam);

        // condicion para mes y año
        String varname3;
        String cExtract = " EXTRACT(year FROM FECHA) = "
                + anio
                + ((!undef.equals(mes)) ? " and EXTRACT(month FROM FECHA) = " + mes : "");

        String valorAnioMes = (undef.equals(anio)) ? "" : valAnd + cExtract;
        varname3 = varname2.replace("?7", valorAnioMes);

        // condicion para sucursal ejecutivo
        String varname4;
        String cSentenciaSuc = "  sucursal_id=" + suc;

        String valorSuc = (undef.equals(suc)) ? "" : valAnd + cSentenciaSuc;
        varname4 = varname3.replace("?8", valorSuc);

        Query q = em.createNativeQuery(varname4, Referencias.class);
        List<Referencias> result = new LinkedList<>();
        ArmarRespuestasDeEncuesta arde = new ArmarRespuestasDeEncuesta();
        BitacorasJpaController bc2 = new BitacorasJpaController(utx, null, em);

        result = q.getResultList();

        ReferenciasExport re;
        List<ReferenciasExport> result2 = new LinkedList<>();
        String nombre = null;

        for (Referencias r : result) {
            re = new ReferenciasExport();
            continuar3(r, re, bc2, nombre, arde);

            String vSexo = ("1".equals(r.getReferidoId().getSexo()))
                    ? "Masculino"
                    : ("2".equals(r.getReferidoId().getSexo()))
                            ? "Femenino"
                            : "Sin Información";
            re.setSexo(vSexo);

            q = em.createNativeQuery(
                    Resources.sql2UltimosComentarios(),
                    Bitacoras.class);
            q.setParameter(1, r.getId());
            Collection<Bitacoras> bc = q.getResultList();

            q = em.createNativeQuery(
                    Resources.sql2UltimosComentarios2(),
                    Bitacoras.class);
            q.setParameter(1, r.getId());
            Collection<Bitacoras> bc12 = q.getResultList();

            continuar2(bc, bc12, re);

            continuar(r, re);

            // parte de encuesta digital
            result2.add(re);
        }

        return result2;
    }

    private void continuar(Referencias r, ReferenciasExport re) {
        // parte de encuesta digital
        if (r.getReferidoId().getRespEncuestaCollection().size() > 0) {
            Collection<RespEncuesta> respuestas = r
                    .getReferidoId()
                    .getRespEncuestaCollection();
            for (RespEncuesta resp : respuestas) {
                if (resp.getPreguntaId().getId().equals(new BigDecimal("1"))) {
                    re.setEncDigitalPreg1(resp.getPreguntaId().getDescripcion());
                    re.setEncDigitalResp1(resp.getRespuestaBool());
                }
                if (resp.getPreguntaId().getId().equals(new BigDecimal("2"))) {
                    re.setEncDigitalPreg2(resp.getPreguntaId().getDescripcion());
                    re.setEncDigitalResp2(resp.getRespuestaBool());
                }
                continuar10(resp, re);
            }
        }
    }

    private void continuar2(
            Collection<Bitacoras> bc,
            Collection<Bitacoras> bc12,
            ReferenciasExport re) {
        for (Bitacoras bitacoras : bc12) {
            bc.add(bitacoras);
        }

        Collections.sort(
                (List<Bitacoras>) bc,
                new Comparator<Bitacoras>() {
                    @Override
                    public int compare(Bitacoras a1, Bitacoras a2) {
                        int i = a1.getFecha().compareTo(a2.getFecha());
                        if (i != 0) {
                            return -i;
                        } else {
                            return a1.getFecha().compareTo(a2.getFecha());
                        }
                    }
                });

        String b = "";

        int x = 0;
        for (Bitacoras bitacoras : bc) {
            try {
                b = b + bitacoras.getComentarios().split("->")[1];
            } catch (Exception e) {
                b = b + bitacoras.getComentarios();
            }
            x++;
            if (x == 1) {
                break;
            }
        }
        re.setUltimos2Coments(b);
    }

    private void continuar3(
            Referencias r,
            ReferenciasExport re,
            BitacorasJpaController bc2,
            String nombre,
            ArmarRespuestasDeEncuesta arde) {
        re.setCanal((r.getCanalname() != null) ? r.getCanalname() : "");
        re.setComuna(
                (r.getReferidoId().getComuna() != null)
                        ? r.getReferidoId().getComuna()
                        : "");
        re.setCorreo(
                (r.getReferidoId().getCorreo() != null)
                        ? r.getReferidoId().getCorreo()
                        : "");
        re.setEjecutivo(r.getOwnerename());
        re.setEstado(
                (r.getAccionId().getNombre() != null) ? r.getAccionId().getNombre() : "");
        re.setFECHANAC(r.getReferidoId().getFechanac());
        re.setFecha(r.getFecha());
        re.setFechaAccion(r.getFechaAccion());

        Bitacoras bit = bc2.findBitacorasLastFicha(r);
        String fechaFicha = (bit == null)
                ? ""
                : new SimpleDateFormat("dd-MM-yyyy").format(bit.getFecha());

        re.setFechaFicha(fechaFicha);
        re.setID(r.getId());
        nombre = (r.getReferidoId().getNombre() != null)
                ? r.getReferidoId().getNombre()
                : "";
        nombre = nombre
                + " "
                + ((r.getReferidoId().getApellido() != null)
                        ? r.getReferidoId().getApellido()
                        : "");
        re.setNombre(nombre);
        re.setRegion(
                (r.getReferidoId().getRegion() != null)
                        ? r.getReferidoId().getRegion()
                        : "");
        re.setRut(
                (r.getReferidoId().getRut() != null) ? r.getReferidoId().getRut() : "");
        re.setTelefonos(r.getReferidoId().getTelefonos());
        re.setTelefonos2(r.getReferidoId().getTelefonos2());
        re.setTelefonos3(r.getReferidoId().getTelefonos3());

        re.setPensionarse(arde.pensionarse(r.getReferidoId()));
        re.setClienteSolicito(arde.clienteSolicito(r.getReferidoId()));
        re.setAccionRealizo(arde.accionRealizo(r.getReferidoId()));
        re.setTipoPension(arde.tipoPension(r.getReferidoId()));
    }

    private void continuar10(RespEncuesta resp, ReferenciasExport re) {
        if (resp.getPreguntaId().getId().equals(new BigDecimal("3"))) {
            re.setEncDigitalPreg3(resp.getPreguntaId().getDescripcion());
            re.setEncDigitalResp3(resp.getRespuestaBool());
        }
        if (resp.getPreguntaId().getId().equals(new BigDecimal("4"))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            re.setEncDigitalPreg4(resp.getPreguntaId().getDescripcion());
            if (resp.getRespuestaFecha() == null) {
                re.setEncDigitalResp4("");
            } else {
                re.setEncDigitalResp4(sdf.format(resp.getRespuestaFecha()));
            }
        }
    }
}
