/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.controller;

import cl.cnsv.referidosrrvv.controller.exceptions.IllegalOrphanException;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.PreexistingEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Pregunta;
import cl.cnsv.referidosrrvv.models.RespEncuesta;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
public class PreguntaJpaController implements Serializable {

  private String valAnError = "An error occurred attempting to roll back the transaction.";

  public PreguntaJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  private transient UserTransaction utx = null;
  private transient EntityManagerFactory emf = null;
  private String valNoLonger = " no longer exists.";

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Pregunta pregunta)
      throws PreexistingEntityException, RollbackFailureException, Exception {
    if (pregunta.getRespEncuestaCollection() == null) {
      pregunta.setRespEncuestaCollection(new ArrayList<RespEncuesta>());
    }
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Collection<RespEncuesta> attachedRespEncuestaCollection = new ArrayList<RespEncuesta>();
      for (RespEncuesta respEncuestaCollectionRespEncuestaToAttach : pregunta.getRespEncuestaCollection()) {
        respEncuestaCollectionRespEncuestaToAttach = em.getReference(
            respEncuestaCollectionRespEncuestaToAttach.getClass(),
            respEncuestaCollectionRespEncuestaToAttach.getId());
        attachedRespEncuestaCollection.add(
            respEncuestaCollectionRespEncuestaToAttach);
      }
      pregunta.setRespEncuestaCollection(attachedRespEncuestaCollection);
      em.persist(pregunta);
      for (RespEncuesta respEncuestaCollectionRespEncuesta : pregunta.getRespEncuestaCollection()) {
        Pregunta oldPreguntaIdOfRespEncuestaCollectionRespEncuesta = respEncuestaCollectionRespEncuesta.getPreguntaId();
        respEncuestaCollectionRespEncuesta.setPreguntaId(pregunta);
        respEncuestaCollectionRespEncuesta = em.merge(respEncuestaCollectionRespEncuesta);
        if (oldPreguntaIdOfRespEncuestaCollectionRespEncuesta != null) {
          oldPreguntaIdOfRespEncuestaCollectionRespEncuesta
              .getRespEncuestaCollection()
              .remove(respEncuestaCollectionRespEncuesta);
          oldPreguntaIdOfRespEncuestaCollectionRespEncuesta = em
              .merge(oldPreguntaIdOfRespEncuestaCollectionRespEncuesta);
        }
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      if (findPregunta(pregunta.getId()) != null) {
        throw new PreexistingEntityException(
            "Pregunta " + pregunta + " already exists.",
            ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Pregunta pregunta)
      throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Pregunta persistentPregunta = em.find(Pregunta.class, pregunta.getId());
      Collection<RespEncuesta> respEncuestaCollectionOld = persistentPregunta.getRespEncuestaCollection();
      Collection<RespEncuesta> respEncuestaCollectionNew = pregunta.getRespEncuestaCollection();
      List<String> illegalOrphanMessages = null;

      continuar2(
          respEncuestaCollectionOld,
          respEncuestaCollectionNew,
          illegalOrphanMessages);

      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      Collection<RespEncuesta> attachedRespEncuestaCollectionNew = new ArrayList<RespEncuesta>();
      for (RespEncuesta respEncuestaCollectionNewRespEncuestaToAttach : respEncuestaCollectionNew) {
        respEncuestaCollectionNewRespEncuestaToAttach = em.getReference(
            respEncuestaCollectionNewRespEncuestaToAttach.getClass(),
            respEncuestaCollectionNewRespEncuestaToAttach.getId());
        attachedRespEncuestaCollectionNew.add(
            respEncuestaCollectionNewRespEncuestaToAttach);
      }
      respEncuestaCollectionNew = attachedRespEncuestaCollectionNew;
      pregunta.setRespEncuestaCollection(respEncuestaCollectionNew);
      pregunta = em.merge(pregunta);

      continuar(
          respEncuestaCollectionNew,
          respEncuestaCollectionOld,
          pregunta,
          em);

      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        BigDecimal id = pregunta.getId();
        if (findPregunta(id) == null) {
          throw new NonexistentEntityException(
              "The pregunta with id " + id + valNoLonger);
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
      throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Pregunta pregunta;
      try {
        pregunta = em.getReference(Pregunta.class, id);
        pregunta.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException(
            "The pregunta with id " + id + valNoLonger,
            enfe);
      }
      List<String> illegalOrphanMessages = null;
      Collection<RespEncuesta> respEncuestaCollectionOrphanCheck = pregunta.getRespEncuestaCollection();
      for (RespEncuesta respEncuestaCollectionOrphanCheckRespEncuesta : respEncuestaCollectionOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
            "This Pregunta (" +
                pregunta +
                ") cannot be destroyed since the RespEncuesta " +
                respEncuestaCollectionOrphanCheckRespEncuesta +
                " in its respEncuestaCollection field has a non-nullable preguntaId field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      em.remove(pregunta);
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

  public List<Pregunta> findPreguntaEntities() {
    return findPreguntaEntities(true, -1, -1);
  }

  public List<Pregunta> findPreguntaEntities(int maxResults, int firstResult) {
    return findPreguntaEntities(false, maxResults, firstResult);
  }

  private List<Pregunta> findPreguntaEntities(
      boolean all,
      int maxResults,
      int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Pregunta.class));
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

  public Pregunta findPregunta(BigDecimal id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Pregunta.class, id);
    } finally {
      em.close();
    }
  }

  public int getPreguntaCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Pregunta> rt = cq.from(Pregunta.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  private void continuar(
      Collection<RespEncuesta> respEncuestaCollectionNew,
      Collection<RespEncuesta> respEncuestaCollectionOld,
      Pregunta pregunta,
      EntityManager em) {
    for (RespEncuesta respEncuestaCollectionNewRespEncuesta : respEncuestaCollectionNew) {
      if (!respEncuestaCollectionOld.contains(
          respEncuestaCollectionNewRespEncuesta)) {
        Pregunta oldPreguntaIdOfRespEncuestaCollectionNewRespEncuesta = respEncuestaCollectionNewRespEncuesta
            .getPreguntaId();
        respEncuestaCollectionNewRespEncuesta.setPreguntaId(pregunta);
        respEncuestaCollectionNewRespEncuesta = em.merge(respEncuestaCollectionNewRespEncuesta);
        if (oldPreguntaIdOfRespEncuestaCollectionNewRespEncuesta != null &&
            !oldPreguntaIdOfRespEncuestaCollectionNewRespEncuesta.equals(pregunta)) {
          oldPreguntaIdOfRespEncuestaCollectionNewRespEncuesta
              .getRespEncuestaCollection()
              .remove(respEncuestaCollectionNewRespEncuesta);
          oldPreguntaIdOfRespEncuestaCollectionNewRespEncuesta = em
              .merge(oldPreguntaIdOfRespEncuestaCollectionNewRespEncuesta);
        }
      }
    }
  }

  private void continuar2(
      Collection<RespEncuesta> respEncuestaCollectionOld,
      Collection<RespEncuesta> respEncuestaCollectionNew,
      List<String> illegalOrphanMessages) {
    for (RespEncuesta respEncuestaCollectionOldRespEncuesta : respEncuestaCollectionOld) {
      if (!respEncuestaCollectionNew.contains(
          respEncuestaCollectionOldRespEncuesta)) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
            "You must retain RespEncuesta " +
                respEncuestaCollectionOldRespEncuesta +
                " since its preguntaId field is not nullable.");
      }
    }
  }
}
