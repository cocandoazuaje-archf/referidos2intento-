/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.AccionesJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.BitacorasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferenciasJpaController;
import cl.cnsv.referidosrrvv.controller.exceptions.IllegalOrphanException;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.PreexistingEntityException;
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
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class ActualizarDatosReferencias {

    private static final Logger LOGGER = Logger.getLogger(
            ActualizarDatosReferencias.class.getName());

    public List<ErrorActualizaDatosReferenciasOut> actualizarSinUso(
            List<EntidadDeCargaJs> entity,
            EntityManager em)
            throws NonexistentEntityException, RollbackFailureException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.IllegalOrphanException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException {
        ReferenciasJpaController referenciasController = new ReferenciasJpaController(
                em);
        BitacorasJpaController bc = new BitacorasJpaController(em);
        AccionesJpaController ac = new AccionesJpaController(em);
        List<ErrorActualizaDatosReferenciasOut> eadro = new ArrayList<>();
        for (EntidadDeCargaJs e : entity) {
            if (e.getID() != null) {
                try {
                    Referencias referencia = new Referencias();

                    referencia = referenciasController.findReferencias(e.getID());
                    if (referencia == null) {
                        throw new NullPointerException(
                                "El numero de id del archivo no corresponde a ningun numero de id en la base de datos.");
                    }

                    continuar4(referencia, referenciasController, e, bc);

                    continuar3(referencia, ac, referenciasController, e, bc);

                    continuar2(referencia, e, referenciasController, bc);
                } catch (IllegalOrphanException
                        | NonexistentEntityException
                        | PreexistingEntityException
                        | RollbackFailureException
                        | NullPointerException ex) {
                    continuar(ex, e, eadro);
                }
            }
        }

        return eadro;
    }

    private void continuar(
            Exception ex,
            EntidadDeCargaJs e,
            List<ErrorActualizaDatosReferenciasOut> eadro) {
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

    private void continuar2(
            Referencias referencia,
            EntidadDeCargaJs e,
            ReferenciasJpaController referenciasController,
            BitacorasJpaController bc)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException,
            PreexistingEntityException, cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.IllegalOrphanException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException {
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

    private void continuar3(
            Referencias referencia,
            AccionesJpaController ac,
            ReferenciasJpaController referenciasController,
            EntidadDeCargaJs e,
            BitacorasJpaController bc)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException,
            PreexistingEntityException, cl.cns.integracion.wsreferidosrrvv.exceptions.IllegalOrphanException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException {
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
    }

    private void continuar4(
            Referencias referencia,
            ReferenciasJpaController referenciasController,
            EntidadDeCargaJs e,
            BitacorasJpaController bc)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException,
            PreexistingEntityException, cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.IllegalOrphanException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException {
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
