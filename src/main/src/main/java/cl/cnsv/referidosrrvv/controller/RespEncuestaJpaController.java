/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.controller;

import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.PreexistingEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Pregunta;
import cl.cnsv.referidosrrvv.models.Referidos;
import cl.cnsv.referidosrrvv.models.RespEncuesta;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carlos Ocando
 */
public class RespEncuestaJpaController implements Serializable {

  private String valAnError = "An error occurred attempting to roll back the transaction.";

  public RespEncuestaJpaController(
      UserTransaction utx,
      EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  private transient UserTransaction utx = null;
  private transient EntityManagerFactory emf = null;
  private String valNoLonger = " no longer exists.";

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(RespEncuesta respEncuesta)
      throws PreexistingEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Pregunta preguntaId = respEncuesta.getPreguntaId();
      if (preguntaId != null) {
        preguntaId = em.getReference(preguntaId.getClass(), preguntaId.getId());
        respEncuesta.setPreguntaId(preguntaId);
      }
      Referidos referidoId = respEncuesta.getReferidoId();
      if (referidoId != null) {
        referidoId = em.getReference(referidoId.getClass(), referidoId.getId());
        respEncuesta.setReferidoId(referidoId);
      }
      em.persist(respEncuesta);
      if (preguntaId != null) {
        preguntaId.getRespEncuestaCollection().add(respEncuesta);
        preguntaId = em.merge(preguntaId);
      }
      if (referidoId != null) {
        referidoId.getRespEncuestaCollection().add(respEncuesta);
        referidoId = em.merge(referidoId);
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      if (findRespEncuesta(respEncuesta.getId()) != null) {
        throw new PreexistingEntityException(
            "RespEncuesta " + respEncuesta + " already exists.",
            ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(RespEncuesta respEncuesta)
      throws NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      RespEncuesta persistentRespEncuesta = em.find(
          RespEncuesta.class,
          respEncuesta.getId());
      Pregunta preguntaIdOld = persistentRespEncuesta.getPreguntaId();
      Pregunta preguntaIdNew = respEncuesta.getPreguntaId();
      Referidos referidoIdOld = persistentRespEncuesta.getReferidoId();
      Referidos referidoIdNew = respEncuesta.getReferidoId();
      if (preguntaIdNew != null) {
        preguntaIdNew = em.getReference(preguntaIdNew.getClass(), preguntaIdNew.getId());
        respEncuesta.setPreguntaId(preguntaIdNew);
      }
      if (referidoIdNew != null) {
        referidoIdNew = em.getReference(referidoIdNew.getClass(), referidoIdNew.getId());
        respEncuesta.setReferidoId(referidoIdNew);
      }
      respEncuesta = em.merge(respEncuesta);

      continuar(
          preguntaIdOld,
          respEncuesta,
          preguntaIdNew,
          em,
          referidoIdOld,
          referidoIdNew);

      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        BigDecimal id = respEncuesta.getId();
        if (findRespEncuesta(id) == null) {
          throw new NonexistentEntityException(
              "The respEncuesta with id " + id + valNoLonger);
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(BigDecimal id)
      throws NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      RespEncuesta respEncuesta;
      try {
        respEncuesta = em.getReference(RespEncuesta.class, id);
        respEncuesta.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException(
            "The respEncuesta with id " + id + valNoLonger,
            enfe);
      }
      Pregunta preguntaId = respEncuesta.getPreguntaId();
      if (preguntaId != null) {
        preguntaId.getRespEncuestaCollection().remove(respEncuesta);
        preguntaId = em.merge(preguntaId);
      }
      Referidos referidoId = respEncuesta.getReferidoId();
      if (referidoId != null) {
        referidoId.getRespEncuestaCollection().remove(respEncuesta);
        referidoId = em.merge(referidoId);
      }
      em.remove(respEncuesta);
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<RespEncuesta> findRespEncuestaEntities() {
    return findRespEncuestaEntities(true, -1, -1);
  }

  public List<RespEncuesta> findRespEncuestaEntities(
      int maxResults,
      int firstResult) {
    return findRespEncuestaEntities(false, maxResults, firstResult);
  }

  private List<RespEncuesta> findRespEncuestaEntities(
      boolean all,
      int maxResults,
      int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(RespEncuesta.class));
      Query q = em.createQuery(cq);
      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }
      return q.getResultList();
    } finally {
      em.close();
    }
  }

  public RespEncuesta findRespEncuesta(BigDecimal id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(RespEncuesta.class, id);
    } finally {
      em.close();
    }
  }

  public int getRespEncuestaCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<RespEncuesta> rt = cq.from(RespEncuesta.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  private void continuar(
      Pregunta preguntaIdOld,
      RespEncuesta respEncuesta,
      Pregunta preguntaIdNew,
      EntityManager em,
      Referidos referidoIdOld,
      Referidos referidoIdNew) {
    if (preguntaIdOld != null && !preguntaIdOld.equals(preguntaIdNew)) {
      preguntaIdOld.getRespEncuestaCollection().remove(respEncuesta);
      preguntaIdOld = em.merge(preguntaIdOld);
    }
    if (preguntaIdNew != null && !preguntaIdNew.equals(preguntaIdOld)) {
      preguntaIdNew.getRespEncuestaCollection().add(respEncuesta);
      preguntaIdNew = em.merge(preguntaIdNew);
    }
    if (referidoIdOld != null && !referidoIdOld.equals(referidoIdNew)) {
      referidoIdOld.getRespEncuestaCollection().remove(respEncuesta);
      referidoIdOld = em.merge(referidoIdOld);
    }
    if (referidoIdNew != null && !referidoIdNew.equals(referidoIdOld)) {
      referidoIdNew.getRespEncuestaCollection().add(respEncuesta);
      referidoIdNew = em.merge(referidoIdNew);
    }
  }
}
