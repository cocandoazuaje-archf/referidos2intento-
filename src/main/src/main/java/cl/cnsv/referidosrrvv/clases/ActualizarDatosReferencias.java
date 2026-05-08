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
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class ActualizarDatosReferencias {

    private static final Logger LOGGER = Logger.getLogger(
            ActualizarDatosReferencias.class.getName());

    public List<ErrorActualizaDatosReferenciasOut> actualizar6(
            List<EntidadDeCargaJs> entity,
            UserTransaction utx,
            EntityManager em) throws NonexistentEntityException, RollbackFailureException {
        ReferenciasJpaController referenciasController = new ReferenciasJpaController(
                utx,
                null,
                em);
        BitacorasJpaController bc = new BitacorasJpaController(utx, null, em);
        AccionesJpaController ac = new AccionesJpaController(utx, null, em);
        List<ErrorActualizaDatosReferenciasOut> eadro = new ArrayList<>();
        for (EntidadDeCargaJs e : entity) {
            if (e.getID() != null) {
                continuar10(referenciasController, e, eadro, bc, ac);
            }
        }

        return eadro;
    }

    private void continuar2(
            EntidadDeCargaJs e,
            Referencias referencia,
            BitacorasJpaController bc,
            ReferenciasJpaController referenciasController,
            AccionesJpaController ac) throws NonexistentEntityException, RollbackFailureException, Exception {
        if ((e.getCANAL() != null) && (e.getCANAL().length() > 0)) {
            String canalOld;
            canalOld = (referencia.getCanalname() == null) ? "" : referencia.getCanalname();
            referencia.setCanalname(e.getCANAL());
            referenciasController.edit(referencia);

            Bitacoras b = new Bitacoras();
            String comentario = "CAMBIO DE CANAL : "
                    + canalOld.toUpperCase()
                    + " -> "
                    + e.getCANAL().toUpperCase()
                    + ",  mediante archivo de carga para actualizacion de datos de referencias. -> "
                    + ((e.getCOMENTARIOS() != null) ? e.getCOMENTARIOS() : "");
            b.setComentarios(comentario);
            b.setFecha(new Date());
            b.setReferenciaId(referencia);
            b.setVersion(BigInteger.ONE);
            b.setUsuario(e.getUSUARIO());
            bc.create(b);
        }
    }

    private void continuar(
            EntidadDeCargaJs e,
            Referencias referencia,
            BitacorasJpaController bc,
            ReferenciasJpaController referenciasController,
            AccionesJpaController ac) throws NonexistentEntityException, RollbackFailureException, Exception {
        if ((e.getESTADO() != null) && (e.getESTADO().length() > 0)) {
            Acciones nuevoEstado;
            String estadoOld = referencia.getAccionId().getNombre();

            nuevoEstado = ac.findByNombre(e.getESTADO());
            referencia.setAccionId(nuevoEstado);

            referenciasController.edit(referencia);

            Bitacoras b = new Bitacoras();
            b.setComentarios(
                    "CAMBIO DE ESTADO : "
                            + estadoOld.toUpperCase()
                            + " -> "
                            + referencia.getAccionId().getNombre().toUpperCase()
                            + ",  MEDIANTE ARCHIVO DE CARGA PARA ACTUALIZACION DE DATOS DE REFERENCIAS -> "
                            + ((e.getCOMENTARIOS() != null) ? e.getCOMENTARIOS() : ""));
            b.setFecha(new Date());
            b.setReferenciaId(referencia);
            b.setVersion(BigInteger.ONE);
            b.setUsuario(e.getUSUARIO());
            bc.create(b);
        }

        if (e.getFECHA_RECEPCION() != null) {
            Date nuevaFecha;
            Date fechaOld = referencia.getFecha();

            nuevaFecha = e.getFECHA_RECEPCION();
            referencia.setFecha(nuevaFecha);

            referenciasController.edit(referencia);

            Bitacoras b = new Bitacoras();
            b.setComentarios(
                    "CAMBIO DE FECHA DE RECEPCION : "
                            + new SimpleDateFormat("EEE, d MMM yyyy").format(fechaOld)
                            + " -> "
                            + new SimpleDateFormat("EEE, d MMM yyyy").format(nuevaFecha)
                            + ",  MEDIANTE ARCHIVO DE CARGA PARA ACTUALIZACION DE DATOS DE REFERENCIAS -> "
                            + ((e.getCOMENTARIOS() != null) ? e.getCOMENTARIOS() : ""));
            b.setFecha(new Date());
            b.setReferenciaId(referencia);
            b.setVersion(BigInteger.ONE);
            b.setUsuario(e.getUSUARIO());
            bc.create(b);
        }
    }

    private void continuar10(
            ReferenciasJpaController referenciasController, EntidadDeCargaJs e,
            List<ErrorActualizaDatosReferenciasOut> eadro, BitacorasJpaController bc, AccionesJpaController ac) {
        try {
            Referencias referencia = new Referencias();
            referencia = referenciasController.findReferencias(e.getID());
            if (referencia == null) {
                throw new NullPointerException(
                        "El numero de id del archivo no corresponde a ningun numero de id en la base de datos.");
            }

            continuar2(e, referencia, bc, referenciasController, ac);

            continuar(e, referencia, bc, referenciasController, ac);
        } catch (Exception ex) {
            String error = "*** No se pudo actualizar la Referencia de ID -> "
                    + ((e.getID() != null) ? e.getID() : " ID NULL ")
                    + " -> "
                    + ex;
            ErrorActualizaDatosReferenciasOut adro = new ErrorActualizaDatosReferenciasOut();
            adro.setId(((e.getID() != null) ? e.getID().toString() : " ID NULL "));
            adro.setEstado(e.getESTADO());
            adro.setCanal(e.getCANAL());
            adro.setError(error);
            eadro.add(adro);
            LOGGER.info(error);
        }
    }

    public static class ErrorActualizaDatosReferenciasOut {

        String id;
        String canal;
        String estado;
        String error;

        public ErrorActualizaDatosReferenciasOut() {
            // constructor vacio
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCanal() {
            return canal;
        }

        public void setCanal(String canal) {
            this.canal = canal;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
