/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.ReferidosJpaControllerOLD;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Referidos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

/**
 *
 * @author cow
 */
public class ActualizarDatosReferidos {

    public void actualizar7(
            List<EntidadDeCargaJs> entity,
            UserTransaction utx,
            EntityManager em) throws NonexistentEntityException, RollbackFailureException, Exception {
        ReferidosJpaControllerOLD rc = new ReferidosJpaControllerOLD(utx, null, em);

        for (EntidadDeCargaJs e : entity) {
            Referidos r = rc.findReferidos(e.getID());
            r.setFechanac(e.getFECHANAC());
            rc.edit(r);
        }
    }
}
