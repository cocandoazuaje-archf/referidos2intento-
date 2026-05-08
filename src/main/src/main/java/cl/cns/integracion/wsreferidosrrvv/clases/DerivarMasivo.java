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
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cns.integracion.wsreferidosrrvv.util.construirBodyEnviarMail;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Referencias;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class DerivarMasivo {

    private String undefi = "undefined";
    private static final Logger LOGGER = Logger.getLogger(DerivarMasivo.class);

    public DerivarMasivo() {
        // constructor vacio
    }

    public void derivar(
            String suc,
            String anio,
            String mes,
            String sup,
            String usr,
            String id,
            String eje,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {
        EjecutivosJpaController e = new EjecutivosJpaController(em);
        Ejecutivos e2 = e.findEjecutivos(BigDecimal.valueOf(new Long(eje)));

        String varname = Resources.sqlDerivarMasivoDetalExportarDataTable();
        String search = id;
        search = search.replace(" ", "%");
        String varname1 = varname.replace("?1", search);

        // condicion para supervidor
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
        String cSentencia = " sucursal_id=" + suc;

        String valorSucursal = (undefi.equals(suc)) ? "" : " and " + cSentencia;
        varname4 = varname3.replace("?8", valorSucursal);

        Query q = em.createNativeQuery(varname4, Referencias.class);
        List<Referencias> result = new LinkedList<>();
        result = q.getResultList();

        ReferenciasJpaController rc = new ReferenciasJpaController(em);
        BitacorasJpaController bc = new BitacorasJpaController(em);
        AccionesJpaController ac = new AccionesJpaController(em);
        Acciones a = ac.findAcciones(new BigDecimal("5"));

        for (Referencias r : result) {
            r.setAccionId(a);
            r.setEjecutivoId(e2);
            r.setOwnere(e2.getCodigo());
            r.setOwnerename(e2.getNombre());

            rc.edit(r);

            Bitacoras b = new Bitacoras();
            b.setComentarios(
                    "REFERENCIA DERIVADA EN PROCESO MASIVO A EJECUTIVO -> " + e2.getNombre());
            b.setFecha(new Date());
            b.setReferenciaId(r);
            b.setUsuario(usr);
            b.setVersion(BigInteger.ONE);
            bc.create(b);

            // enviar correo a ejecutiva
            if (result.size() <= Resources.CANT_CORREOS_MAX_POR_EJECUTIVA) {
                try {
                    construirBodyEnviarMail.construir(
                            e2.getCorreo(),
                            r.getReferidoId().getRut(),
                            r.getReferidoId().getNombre(),
                            r.getReferidoId().getRegion(),
                            r.getReferidoId().getComuna(),
                            r.getCanalname(),
                            result.size());
                } catch (NoResultException ex) {
                    LOGGER.info(ex);
                }
            }
            // enviar correo a ejecutiva

        }
        // enviar correo a ejecutiva (solo 1 si son mas de 2 referidos)
        if (result.size() > Resources.CANT_CORREOS_MAX_POR_EJECUTIVA) {
            try {
                construirBodyEnviarMail.construir(
                        e2.getCorreo(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        result.size());
            } catch (NoResultException ex) {
                LOGGER.info(ex);
            }
        }
        // enviar correo a ejecutiva (solo 1 si son mas de 2 referidos)

    }
}
