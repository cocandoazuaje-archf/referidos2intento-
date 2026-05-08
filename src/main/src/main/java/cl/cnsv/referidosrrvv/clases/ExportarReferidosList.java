/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.AccionesJpaController;
import cl.cnsv.referidosrrvv.controller.BitacorasJpaController;
import cl.cnsv.referidosrrvv.controller.ReferenciasJpaController;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Referencias;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author cow
 */
public class ExportarReferidosList {

    public ExportarReferidosList() {
        // constructor vacio
    }

    public List<Referencias> exportar(
            UserTransaction utx,
            EntityManager em,
            String usuario) throws NonexistentEntityException, RollbackFailureException, Exception {
        String varname1 = Resources.sqlEnviarAlGenesis();

        Query q = em.createNativeQuery(varname1, Referencias.class);
        List r = q.getResultList();

        ReferenciasJpaController rcjpa = new ReferenciasJpaController(
                utx,
                null,
                em);
        AccionesJpaController acjpa = new AccionesJpaController(utx, null, em);
        Acciones a = acjpa.findAcciones(new BigDecimal("9"));
        BitacorasJpaController bcjpa = new BitacorasJpaController(utx, null, em);

        for (Iterator it = r.iterator(); it.hasNext();) {
            Referencias ref = (Referencias) it.next();
            ref.setAccionId(a);

            Bitacoras b = new Bitacoras();
            b.setComentarios("ENVIADO A CONTACT CENTER (GENESIS) -> ");
            b.setFecha(new Date());
            b.setReferenciaId(ref);
            b.setVersion(BigInteger.ONE);
            b.setUsuario(usuario);
            b.setReferenciaId(ref);

            rcjpa.edit(ref);
            bcjpa.create(b);
        }

        return r;
    }
}
