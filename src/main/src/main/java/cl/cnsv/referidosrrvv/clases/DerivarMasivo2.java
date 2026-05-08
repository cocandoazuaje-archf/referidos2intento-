/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.AccionesJpaController;
import cl.cnsv.referidosrrvv.controller.BitacorasJpaController;
import cl.cnsv.referidosrrvv.controller.EjecutivosJpaController;
import cl.cnsv.referidosrrvv.controller.ReferenciasJpaController;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.util.construirBodyEnviarMail;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class DerivarMasivo2 {

    private static final Logger LOGGER = Logger.getLogger(DerivarMasivo2.class);

    public void derivarList(
            List<EntidadDeCargaJs> entity,
            UserTransaction utx,
            EntityManager em) throws NonexistentEntityException, RollbackFailureException, Exception {
        EjecutivosJpaController ec = new EjecutivosJpaController(utx, null, em);
        ReferenciasJpaController rc = new ReferenciasJpaController(utx, null, em);
        BitacorasJpaController bc = new BitacorasJpaController(utx, null, em);
        AccionesJpaController ac = new AccionesJpaController(utx, null, em);

        for (EntidadDeCargaJs e : entity) {
            Referencias r = rc.findReferencias(e.getID());
            Ejecutivos eje = ec.findEjecutivos(e.getIDEJECUTIVO());
            Acciones a = ac.findAcciones(new BigDecimal("5"));

            r.setAccionId(a);
            r.setEjecutivoId(eje);
            r.setOwnere(eje.getCodigo());
            r.setOwnerename(eje.getNombre());

            rc.edit(r);

            Bitacoras b = new Bitacoras();
            String comentarios = "REFERENCIA DERIVADA EN PROCESO MASIVO A EJECUTIVO -> "
                    + eje.getNombre();
            b.setComentarios(
                    (e.getCOMENTARIOS() != null) ? e.getCOMENTARIOS() : comentarios);
            b.setFecha(new Date());
            b.setReferenciaId(r);
            b.setUsuario(e.getUSUARIO());
            b.setVersion(BigInteger.ONE);
            bc.create(b);

            // enviar correo a ejecutiva
            try {
                construirBodyEnviarMail.construir(
                        eje.getCorreo(),
                        r.getReferidoId().getRut(),
                        r.getReferidoId().getNombre(),
                        r.getReferidoId().getRegion(),
                        r.getReferidoId().getComuna(),
                        r.getCanalname(),
                        0);
            } catch (NoResultException ex) {
                LOGGER.info(ex);
            }
            // enviar correo a ejecutiva

        }
    }
}
