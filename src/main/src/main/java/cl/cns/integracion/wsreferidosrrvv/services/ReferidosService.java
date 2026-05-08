package cl.cns.integracion.wsreferidosrrvv.services;

import cl.cns.integracion.wsreferidosrrvv.clases.ActualizarDatosReferidos;
import cl.cns.integracion.wsreferidosrrvv.clases.CrearReferidoReferenciaBitacora;
import cl.cns.integracion.wsreferidosrrvv.clases.DerivarMasivo2;
import cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cns.integracion.wsreferidosrrvv.util.LogInterceptor;
import cl.cns.integracion.wsreferidosrrvv.vo.DatosReferidos;
import cl.cns.integracion.wsreferidosrrvv.vo.ErrorCrearReferidoReferenciaBitacoraOut;
import cl.cns.integracion.wsreferidosrrvv.vo.ReferidosRRVV_Out;
import cl.cnsv.referidosrrvv.models.Referencias;
import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

@Stateless
public class ReferidosService {

  private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(
      CrearReferidoReferenciaBitacora.class.getName());

  @PersistenceContext(unitName = "com.cox_referidos_war_1.0PU")
  private transient EntityManager em;

  @Interceptors(LogInterceptor.class)
  public Referencias findReferidoActivo(String rut) {
    Query cnq = em.createNamedQuery(
        "Referidos.findActivoByRut",
        Referencias.class);
    cnq.setParameter("rut", rut);
    List<Referencias> lista = cnq.getResultList();
    return (!lista.isEmpty()) ? lista.get(0) : null;
  }

  public ErrorCrearReferidoReferenciaBitacoraOut CreaReferidoReferenciaBitacora(
      DatosReferidos entity,
      UserTransaction utx,
      EntityManager em) throws Exception {
    ErrorCrearReferidoReferenciaBitacoraOut resp;

    try {
      CrearReferidoReferenciaBitacora crrb = new CrearReferidoReferenciaBitacora();
      resp = crrb.cargarReferidoReferenciaBitacora(entity, utx, em);
    } catch (Exception e) {
      LOGGER.error("Error:", e);
      throw e;
    }

    return resp;
  }

  public ReferidosRRVV_Out actualizarReferido(
      DatosReferidos entity,
      UserTransaction utx,
      EntityManager em)
      throws NonexistentEntityException, RollbackFailureException,
      cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException,
      cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException {
    ReferidosRRVV_Out respuesta = new ReferidosRRVV_Out();

    ActualizarDatosReferidos adr = new ActualizarDatosReferidos();
    respuesta = adr.actualizarReferido(entity, em);
    return respuesta;
  }

  public ReferidosRRVV_Out ActualizarAsignarEjecutivo(
      DatosReferidos entity,
      UserTransaction utx,
      EntityManager em) throws Exception {
    ReferidosRRVV_Out salida = new ReferidosRRVV_Out();

    DerivarMasivo2 dm = new DerivarMasivo2();
    salida = dm.asignarEjecutivo(entity, utx, em);

    return salida;
  }

  public Referencias findReferidoActivoRut(String rut) {
    Query cnq = em.createNamedQuery(
        "Referidos.findActivoByRutVigente",
        Referencias.class);
    cnq.setParameter("rut", rut);
    List<Referencias> lista = cnq.getResultList();
    Referencias result = (lista.size() > 0) ? lista.get(0) : null;
    return result;
  }
}
