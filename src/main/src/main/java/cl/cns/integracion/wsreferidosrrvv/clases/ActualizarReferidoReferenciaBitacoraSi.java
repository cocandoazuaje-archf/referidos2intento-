/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.AccionesJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.BitacorasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferenciasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferidosJpaController;
import cl.cns.integracion.wsreferidosrrvv.exceptions.IllegalOrphanException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Referidos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

/**
 *
 * @author cow
 */
public class ActualizarReferidoReferenciaBitacoraSi {

    public ActualizarReferidoReferenciaBitacoraSi() {
        // constructor vacio
    }

    public void actualizarSinUso5(
            EntidadDeCargaJs rjs,
            UserTransaction utx,
            EntityManager em)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException,
            PreexistingEntityException {
        ReferidosJpaController rc = new ReferidosJpaController(em);
        ReferenciasJpaController rfc = new ReferenciasJpaController(em);
        BitacorasJpaController bc = new BitacorasJpaController(em);
        AccionesJpaController ac = new AccionesJpaController(em);

        Acciones a = ac.findAcciones(BigDecimal.valueOf(2));
        Referencias rf = rfc.findReferencias(rjs.getID());

        if ("9".equals(rf.getAccionId().getId().toString())) {
            Referidos r = rc.findReferidos(rf.getReferidoId().getId());
            rc.edit(r);

            rf.setAccionId(a);
            rfc.edit(rf);

            String comentarios = "REFERENCIA CARGADA DEL GENESIS CON ESTATUS 'SI'. -> ";

            Bitacoras b = new Bitacoras();
            b.setFecha(new Date());
            b.setComentarios(
                    comentarios
                            + ((rjs.getCOMENTARIOS() != null) ? rjs.getCOMENTARIOS() : ""));
            b.setVersion(new BigInteger("1"));
            b.setUsuario(rjs.getUSUARIO());
            b.setReferenciaId(rf);
            bc.create(b);
        }
    }
}
