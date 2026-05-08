/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.AccionesJpaController;
import cl.cnsv.referidosrrvv.controller.BitacorasJpaController;
import cl.cnsv.referidosrrvv.controller.ReferenciasJpaController;
import cl.cnsv.referidosrrvv.controller.ReferidosJpaControllerOLD;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
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
public class ActualizarReferidoReferenciaBitacora {

    public ActualizarReferidoReferenciaBitacora() {
        // constructor vacio
    }

    public void actualizarSinUso8(
            EntidadDeCargaJs rjs,
            UserTransaction utx,
            EntityManager em) throws NonexistentEntityException, RollbackFailureException, Exception {
        ReferidosJpaControllerOLD rc = new ReferidosJpaControllerOLD(utx, null, em);
        ReferenciasJpaController rfc = new ReferenciasJpaController(utx, null, em);
        BitacorasJpaController bc = new BitacorasJpaController(utx, null, em);
        AccionesJpaController ac = new AccionesJpaController(null, null, em);

        boolean archivoConectaSi = (rjs.getCONECTA_NOCONECTA() == null);

        int accion = ("NO".equals(rjs.getCONECTA_NOCONECTA())) ? 6 : 2;
        Acciones a = ac.findAcciones(BigDecimal.valueOf(accion));
        Referencias rf = rfc.findReferencias(rjs.getID());

        if ("9".equals(rf.getAccionId().getId().toString())) {
            Referidos r = rc.findReferidos(rf.getReferidoId().getId());
            rc.edit(r);

            rf.setAccionId(a);
            rf.setCanalname(rjs.getCANALNAME());
            rfc.edit(rf);

            String comentarios = (archivoConectaSi)
                    ? "REFERENCIA CARGADA DEL GENESIS CON ESTATUS 'SI'. -> "
                    : "REFERENCIA CARGADA DEL GENESIS CON ESTATUS 'NO'. -> ";

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
