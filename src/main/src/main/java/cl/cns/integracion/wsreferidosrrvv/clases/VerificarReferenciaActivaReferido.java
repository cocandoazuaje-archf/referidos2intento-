/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.ReferenciasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferidosJpaController;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Referidos;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author carlos
 */
public class VerificarReferenciaActivaReferido {

    private static final Logger LOGGER = Logger.getLogger(
            VerificarReferenciaActivaReferido.class.getName());

    public VerificarReferenciaActivaReferido() {
        // constructor vacio
    }

    public OutVerificaReferenciaActiva verificar(
            String rut,
            UserTransaction utx,
            EntityManager em) {
        OutVerificaReferenciaActiva resp;
        Referidos referidoExiste;
        ReferidosJpaController rc = new ReferidosJpaController(em);
        ReferenciasJpaController refc = new ReferenciasJpaController(em);
        Referidos r = new Referidos();

        String rutmodoConDv = (rut.contains("-")) ? rut.replace("-", "") : rut;
        String parte1, parte2;
        String rutmodoSinDv = null;

        if (!rut.contains("-")) {
            parte1 = rut.substring(0, rut.length() - 1);
            parte2 = rut.substring(rut.length() - 1);
            rutmodoSinDv = parte1 + "-" + parte2;
        } else {
            rutmodoSinDv = rut;
        }

        try {
            referidoExiste = rc.findByRut(rutmodoConDv);
            if (referidoExiste == null) {
                referidoExiste = rc.findByRut(rutmodoSinDv);
            }
        } catch (NoResultException e) {
            referidoExiste = null;
            LOGGER.info(e);
        }

        boolean referenciaAlMenosUnaAbierta = continuar(referidoExiste, em, r);
        resp = new OutVerificaReferenciaActiva(r, referenciaAlMenosUnaAbierta);

        return resp;
    }

    private boolean continuar(
            Referidos referidoExiste,
            EntityManager em,
            Referidos r) {
        boolean referenciaAlMenosUnaAbierta = false;
        if (referidoExiste != null) {
            r = referidoExiste;
            Query sql = em.createNativeQuery(
                    "SELECT * FROM RFD_REFERENCIAS WHERE REFERIDO_ID=?",
                    Referencias.class);
            sql.setParameter(1, r.getId());
            Collection<Referencias> referenciasCollections = sql.getResultList();

            if (referenciasCollections != null && referenciasCollections.size() > 0) {
                for (Referencias refcollect : referenciasCollections) {
                    if (!("6".equals(refcollect.getAccionId().getId().toString())
                            || "7".equals(refcollect.getAccionId().getId().toString()))) {
                        referenciaAlMenosUnaAbierta = true;
                    }
                }
            }
        }
        return referenciaAlMenosUnaAbierta;
    }
}
