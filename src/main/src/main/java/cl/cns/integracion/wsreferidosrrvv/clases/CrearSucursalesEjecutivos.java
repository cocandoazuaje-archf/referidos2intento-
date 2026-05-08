/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.EjecutivosJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.SucursalesJpaController;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Sucursales;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class CrearSucursalesEjecutivos {

    private static final Logger LOGGER = Logger.getLogger(
            CrearSucursalesEjecutivos.class.getName());

    public CrearSucursalesEjecutivos() {
        // constructor vacio
    }

    public void cargar(
            EntidadDeCargaJs rjs,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {
        SucursalesJpaController sc = new SucursalesJpaController(em);
        EjecutivosJpaController ec = new EjecutivosJpaController(em);
        Sucursales s = new Sucursales();
        Ejecutivos e;

        try {
            s = sc.findSucursalesByNomnre(rjs.getSUCURSAL());
        } catch (Exception ex) {
            LOGGER.info(ex);
            if (rjs.getSUCURSAL() != null && !rjs.getSUCURSAL().isEmpty()) {
                s = new Sucursales();
                s.setVersion(BigInteger.ONE);
                s.setNombre(rjs.getSUCURSAL());
                sc.create(s);
            }
        }

        if (rjs.getEJECUTIVO() != null && !rjs.getEJECUTIVO().isEmpty()) {
            e = new Ejecutivos();
            if (rjs.getCODEJECUTIVO() != null && !rjs.getCODEJECUTIVO().isEmpty()) {
                rjs.setCODEJECUTIVO(rjs.getCODEJECUTIVO().toLowerCase());
                e.setCodigo(rjs.getCODEJECUTIVO());
            } else {
                UUID idOne = UUID.randomUUID();
                e.setCodigo(idOne.toString());
            }

            try {
                e = ec.findByCodEjecutiva(rjs.getCODEJECUTIVO());
                e.setNombre(rjs.getEJECUTIVO());
                e.setSucursalId(s);
                e.setCorreo(rjs.getCORREO());
                ec.edit(e);
            } catch (Exception ex) {
                e = new Ejecutivos();
                LOGGER.info(ex);
                e.setCodigo(rjs.getCODEJECUTIVO());
                e.setNombre(rjs.getEJECUTIVO());
                e.setSucursalId(s);
                e.setCorreo(rjs.getCORREO());
                e.setVersion(BigInteger.ONE);
                e.setReferenciasCollection(new ArrayList<Referencias>());
                ec.create(e);
            }
        }
    }
}
