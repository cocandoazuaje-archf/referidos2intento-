package cl.cns.integracion.wsreferidosrrvv.services;

import cl.cns.integracion.wsreferidosrrvv.util.LogInterceptor;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class EjecutivosService {

  @PersistenceContext(unitName = "com.cox_referidos_war_1.0PU")
  private transient EntityManager em;

  @Interceptors(LogInterceptor.class)
  public Ejecutivos findEjecutivoPorRUtReferido(String rut) {
    Query cnq = em.createNamedQuery(
        "Ejecutivos.findByRutReferido",
        Ejecutivos.class);
    cnq.setParameter("rut", rut);
    List<Ejecutivos> lista = cnq.getResultList();
    return (!lista.isEmpty()) ? lista.get(0) : null;
  }

  @Interceptors(LogInterceptor.class)
  public Ejecutivos findEjecutivoPorSucursal(BigDecimal id) {
    Query cnq1 = em.createNamedQuery(
        "Ejecutivos.findEjecutivoContar",
        Ejecutivos.class);
    cnq1.setParameter("id", id);
    List<Ejecutivos> lista = cnq1.getResultList();

    int Menor = 0;
    int intBandera = 0;
    /* Caso que exista un ejecutivo sin referidos */
    Ejecutivos ejecutivoElemenosCasos = new Ejecutivos();
    for (int EjecutivosSinRef = 0; EjecutivosSinRef < lista.size(); EjecutivosSinRef++) {
      if (lista.get(EjecutivosSinRef).getReferenciasCollection().size() == 0 &&
          intBandera == 0) {
        intBandera = 1;
        Ejecutivos ejecutivoElemenosCasosaux = new Ejecutivos();
        ejecutivoElemenosCasosaux.setCodigo(
            lista.get(EjecutivosSinRef).getCodigo());
        ejecutivoElemenosCasosaux.setCorreo(
            lista.get(EjecutivosSinRef).getCorreo());
        ejecutivoElemenosCasosaux.setId(lista.get(EjecutivosSinRef).getId());
        ejecutivoElemenosCasosaux.setNombre(
            lista.get(EjecutivosSinRef).getNombre());
        ejecutivoElemenosCasos = ejecutivoElemenosCasosaux;
      }
    }

    if (intBandera == 0) {
      /* Todos los ejecutivos tienen al menos un referido */
      if (lista.size() > 1) {
        continuar(lista, ejecutivoElemenosCasos, Menor);
      } else {
        ejecutivoElemenosCasos = (!lista.isEmpty()) ? lista.get(0) : null;
      }
    }
    return ejecutivoElemenosCasos;
  }

  private void continuar(
      List<Ejecutivos> lista,
      Ejecutivos ejecutivoElemenosCasos,
      int Menor) {
    for (int Ejecutivos = 0; Ejecutivos < lista.size(); Ejecutivos++) {
      Query cnq2 = em.createNamedQuery(
          "Ejecutivos.findcontarVigentesPorEje",
          Ejecutivos.class);
      cnq2.setParameter("id", lista.get(Ejecutivos).getId());
      List<Ejecutivos> lista3 = cnq2.getResultList();

      if (Menor < lista3.size()) {
        Menor = lista3.size();
        Ejecutivos ejecutivoElemenosCasosaux = new Ejecutivos();
        ejecutivoElemenosCasosaux.setCodigo(lista.get(Ejecutivos).getCodigo());
        ejecutivoElemenosCasosaux.setCorreo(lista.get(Ejecutivos).getCorreo());
        ejecutivoElemenosCasosaux.setId(lista.get(Ejecutivos).getId());
        ejecutivoElemenosCasosaux.setNombre(lista.get(Ejecutivos).getNombre());
        ejecutivoElemenosCasos = ejecutivoElemenosCasosaux;
      }
    }
  }
}
