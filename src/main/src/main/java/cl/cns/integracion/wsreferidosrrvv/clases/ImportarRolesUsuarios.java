/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.RolesusuariosJpaController;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Rolesusuarios;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class ImportarRolesUsuarios {

    private static final Logger LOGGER = Logger.getLogger(
            ImportarRolesUsuarios.class.getName());

    public ImportarRolesUsuarios() {
        super();
    }

    public void cargar(
            EntidadDeCargaJs rjs,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {
        RolesusuariosJpaController rujc = new RolesusuariosJpaController(em);

        Query q = em.createNamedQuery("Rolesusuarios.findByNombre");
        q.setParameter("nombre", rjs.getUSUARIO().toLowerCase());

        try {
            Rolesusuarios u = (Rolesusuarios) q.getSingleResult();
            u.setNombre(rjs.getUSUARIO());
            u.setRol(rjs.getROL());
            u.setAst(rjs.getAST());
            u.setSup(rjs.getSUP());
            rujc.edit(u);
        } catch (NoResultException e) {
            LOGGER.info(e);
            Rolesusuarios ou = new Rolesusuarios();
            ou.setVersion(BigInteger.ONE);
            ou.setNombre(rjs.getUSUARIO());
            ou.setRol(rjs.getROL());
            ou.setAst(rjs.getAST());
            ou.setSup(rjs.getSUP());
            rujc.create(ou);
        }
    }
}
