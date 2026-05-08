/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.BitacorasJpaController;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Referencias;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class ExportarDataTable {

        private String undefi = "undefined";
        private static final Logger LOGGER = Logger.getLogger(
                        ActualizarDatosReferencias.class.getName());

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
                String varname = Resources.sqlDerivarMasivoDetalExportarDataTable();
                String search = id;
                search = search.replace(" ", "%");
                String varname1 = varname.replace("?1", search);

                // condicion para supervisor
                String supParam = sup;
                if (!undefi.equals(supParam)) {
                        supParam = " AND OWNERE IN (" + supParam + ")";
                } else {
                        supParam = "";
                }
                String varname2 = varname1.replace("?6", supParam);
                // condicion para mes y año
                String varname3;
                String cExtract = " EXTRACT(year FROM FECHA) = "
                                + anio
                                + ((!undefi.equals(mes)) ? " and EXTRACT(month FROM FECHA) = " + mes : "");
                String valorAnioMes = (undefi.equals(anio)) ? "" : " and " + cExtract;
                varname3 = varname2.replace("?7", valorAnioMes);
                // condicion para sucursal ejecutivo
                String varname4;
                String cSentenciaSuc = "  sucursal_id=" + suc;
                String valorSuc = (undefi.equals(suc)) ? "" : " and " + cSentenciaSuc;
                varname4 = varname3.replace("?8", valorSuc);
                Query q = em.createNativeQuery(varname4, Referencias.class);
                List<Referencias> result = new LinkedList<>();
                ArmarRespuestasDeEncuesta arde = new ArmarRespuestasDeEncuesta();
                BitacorasJpaController bc2 = new BitacorasJpaController(em);
                result = q.getResultList();

                ReferenciasExport re = null;
                List<ReferenciasExport> result2 = new LinkedList<>();
                String nombre = null;

                for (Referencias r : result) {
                        continuar5(re, r);

                        Bitacoras bit = bc2.findBitacorasLastFicha(r);

                        continuar4(bit, re, r, nombre, arde);

                        Collection<Bitacoras> bc = continuar3(r, re, em, q);

                        Collection<Bitacoras> bc12 = q.getResultList();

                        continuar2(bc12, bc);

                        continuar(bc, re, result2);
                }

                return result2;
        }

        private void continuar(
                        Collection<Bitacoras> bc,
                        ReferenciasExport re,
                        List<ReferenciasExport> result2) {
                String b = "";

                int x = 0;
                for (Bitacoras bitacoras : bc) {
                        try {
                                b = b + bitacoras.getComentarios().split("->")[1];
                        } catch (Exception e) {
                                LOGGER.error(e);
                                b = b + bitacoras.getComentarios();
                        }
                        x++;
                        if (x == 1) {
                                break;
                        }
                }
                re.setUltimos2Coments(b);
                result2.add(re);
        }

        private void continuar2(
                        Collection<Bitacoras> bc12,
                        Collection<Bitacoras> bc) {
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
        }

        private Collection<Bitacoras> continuar3(
                        Referencias r,
                        ReferenciasExport re,
                        EntityManager em,
                        Query q) {
                String vSexo = ("1".equals(r.getReferidoId().getSexo()))
                                ? "Masculino"
                                : ("2".equals(r.getReferidoId().getSexo()))
                                                ? "Femenino"
                                                : "Sin Información";
                re.setSexo(vSexo);

                q = em.createNativeQuery(Resources.sql2UltimosComentarios(), Bitacoras.class);
                q.setParameter(1, r.getId());
                Collection<Bitacoras> bc = q.getResultList();

                q = em.createNativeQuery(
                                Resources.sql2UltimosComentarios2(),
                                Bitacoras.class);
                q.setParameter(1, r.getId());

                return bc;
        }

        private void continuar4(
                        Bitacoras bit,
                        ReferenciasExport re,
                        Referencias r,
                        String nombre,
                        ArmarRespuestasDeEncuesta arde) {
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

        private void continuar5(ReferenciasExport re, Referencias r) {
                re = new ReferenciasExport();
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
        }

        // private void continuar6(
        // String anio,
        // String mes,
        // String suc,
        // EntityManager em,
        // String varname2
        // ) {
        //
        // }
}
