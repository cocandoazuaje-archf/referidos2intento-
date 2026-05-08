/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.AccionesJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.BitacorasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.EjecutivosJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferenciasJpaController;
import cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cns.integracion.wsreferidosrrvv.util.construirBodyEnviarMail;
import cl.cns.integracion.wsreferidosrrvv.vo.DatosReferidos;
import cl.cns.integracion.wsreferidosrrvv.vo.ReferidosRRVV_Out;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Referencias;
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
                EjecutivosJpaController ec = new EjecutivosJpaController(em);
                ReferenciasJpaController rc = new ReferenciasJpaController(em);
                BitacorasJpaController bc = new BitacorasJpaController(em);
                AccionesJpaController ac = new AccionesJpaController(em);

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

        public ReferidosRRVV_Out asignarEjecutivo(
                        DatosReferidos entity,
                        UserTransaction utx,
                        EntityManager em) throws NonexistentEntityException, RollbackFailureException, Exception {
                ReferidosRRVV_Out salida = new ReferidosRRVV_Out();
                try {
                        EjecutivosJpaController ec = new EjecutivosJpaController(em);
                        ReferenciasJpaController rc = new ReferenciasJpaController(em);
                        BitacorasJpaController bc = new BitacorasJpaController(em);
                        AccionesJpaController ac = new AccionesJpaController(em);
                        BigInteger Version = (entity.getVersion() != null ? entity.getVersion() : BigInteger.ONE);
                        BigDecimal acciones = (entity.getAcciones() != null ? entity.getAcciones() : BigDecimal.ONE);

                        Referencias r = rc.findReferencias(entity.getIdreferencia());
                        Ejecutivos eje = ec.findEjecutivos(entity.getIdejecutivo());
                        Acciones a = ac.findAcciones(acciones);

                        r.setAccionId(a);
                        r.setEjecutivoId(eje);
                        r.setOwnere(eje.getCodigo());
                        r.setOwnerename(eje.getNombre());

                        rc.edit(r);

                        Bitacoras b = new Bitacoras();
                        String comentarios = "REFERENCIA DERIVADA EN PROCESO MASIVO A EJECUTIVO -> "
                                        + eje.getNombre();
                        b.setComentarios(
                                        (entity.getComentarios() != null)
                                                        ? entity.getComentarios()
                                                        : comentarios);
                        b.setFecha(new Date());
                        b.setReferenciaId(r);
                        b.setUsuario(entity.getUsuario());
                        b.setVersion(Version);
                        bc.create(b);

                        salida.setEstado(a.getNombre());
                        salida.setCodigo(0);
                        salida.setMensaje("OK.");
                } catch (Exception e) {
                        salida.setCodigo(-1);
                        salida.setMensaje("Error" + e);
                }

                return salida;
        }
}
