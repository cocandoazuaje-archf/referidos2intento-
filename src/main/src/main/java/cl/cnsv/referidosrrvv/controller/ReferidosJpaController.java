/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.controller;

import cl.cnsv.referidosrrvv.controller.exceptions.IllegalOrphanException;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Referidos;
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
public class ReferidosJpaController implements Serializable {

  private transient EntityManager em;
  private String valAnError = "An error occurred attempting to roll back the transaction.";

  public ReferidosJpaController(
      UserTransaction utx,
      EntityManagerFactory emf,
      EntityManager em) {
    this.utx = utx;
    this.emf = emf;
    this.em = em;
  }

  private transient UserTransaction utx = null;
  private transient EntityManagerFactory emf = null;
  private String valNoLonger = " no longer exists.";

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Referidos referidos)
      throws RollbackFailureException, Exception {
    if (referidos.getReferenciasCollection() == null) {
      referidos.setReferenciasCollection(new ArrayList<Referencias>());
    }
    if (referidos.getRespEncuestaCollection() == null) {
      referidos.setRespEncuestaCollection(new ArrayList<RespEncuesta>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      Collection<Referencias> attachedReferenciasCollection = new ArrayList<Referencias>();
      for (Referencias referenciasCollectionReferenciasToAttach : referidos.getReferenciasCollection()) {
        referenciasCollectionReferenciasToAttach = em.getReference(
            referenciasCollectionReferenciasToAttach.getClass(),
            referenciasCollectionReferenciasToAttach.getId());
        attachedReferenciasCollection.add(
            referenciasCollectionReferenciasToAttach);
      }
      referidos.setReferenciasCollection(attachedReferenciasCollection);
      Collection<RespEncuesta> attachedRespEncuestaCollection = new ArrayList<RespEncuesta>();
      for (RespEncuesta respEncuestaCollectionRespEncuestaToAttach : referidos.getRespEncuestaCollection()) {
        respEncuestaCollectionRespEncuestaToAttach = em.getReference(
            respEncuestaCollectionRespEncuestaToAttach.getClass(),
            respEncuestaCollectionRespEncuestaToAttach.getId());
        attachedRespEncuestaCollection.add(
            respEncuestaCollectionRespEncuestaToAttach);
      }
      referidos.setRespEncuestaCollection(attachedRespEncuestaCollection);
      em.persist(referidos);
      for (Referencias referenciasCollectionReferencias : referidos.getReferenciasCollection()) {
        Referidos oldReferidoIdOfReferenciasCollectionReferencias = referenciasCollectionReferencias.getReferidoId();
        referenciasCollectionReferencias.setReferidoId(referidos);
        referenciasCollectionReferencias = em.merge(referenciasCollectionReferencias);
        if (oldReferidoIdOfReferenciasCollectionReferencias != null) {
          oldReferidoIdOfReferenciasCollectionReferencias
              .getReferenciasCollection()
              .remove(referenciasCollectionReferencias);
          oldReferidoIdOfReferenciasCollectionReferencias = em.merge(oldReferidoIdOfReferenciasCollectionReferencias);
        }
      }
      for (RespEncuesta respEncuestaCollectionRespEncuesta : referidos.getRespEncuestaCollection()) {
        Referidos oldReferidoIdOfRespEncuestaCollectionRespEncuesta = respEncuestaCollectionRespEncuesta
            .getReferidoId();
        respEncuestaCollectionRespEncuesta.setReferidoId(referidos);
        respEncuestaCollectionRespEncuesta = em.merge(respEncuestaCollectionRespEncuesta);
        if (oldReferidoIdOfRespEncuestaCollectionRespEncuesta != null) {
          oldReferidoIdOfRespEncuestaCollectionRespEncuesta
              .getRespEncuestaCollection()
              .remove(respEncuestaCollectionRespEncuesta);
          oldReferidoIdOfRespEncuestaCollectionRespEncuesta = em
              .merge(oldReferidoIdOfRespEncuestaCollectionRespEncuesta);
        }
      }
    } catch (Exception ex) {
      try {
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

  public void edit(Referidos referidos)
      throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      Referidos persistentReferidos = em.find(
          Referidos.class,
          referidos.getId());
      Collection<Referencias> referenciasCollectionOld = persistentReferidos.getReferenciasCollection();
      Collection<Referencias> referenciasCollectionNew = referidos.getReferenciasCollection();
      Collection<RespEncuesta> respEncuestaCollectionOld = persistentReferidos.getRespEncuestaCollection();
      Collection<RespEncuesta> respEncuestaCollectionNew = referidos.getRespEncuestaCollection();
      List<String> illegalOrphanMessages = null;

      continuar(
          referenciasCollectionOld,
          referenciasCollectionNew,
          illegalOrphanMessages);

      continuar2(
          respEncuestaCollectionOld,
          respEncuestaCollectionNew,
          illegalOrphanMessages);

      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      Collection<Referencias> attachedReferenciasCollectionNew = new ArrayList<Referencias>();
      for (Referencias referenciasCollectionNewReferenciasToAttach : referenciasCollectionNew) {
        referenciasCollectionNewReferenciasToAttach = em.getReference(
            referenciasCollectionNewReferenciasToAttach.getClass(),
            referenciasCollectionNewReferenciasToAttach.getId());
        attachedReferenciasCollectionNew.add(
            referenciasCollectionNewReferenciasToAttach);
      }
      referenciasCollectionNew = attachedReferenciasCollectionNew;
      referidos.setReferenciasCollection(referenciasCollectionNew);
      Collection<RespEncuesta> attachedRespEncuestaCollectionNew = new ArrayList<RespEncuesta>();
      for (RespEncuesta respEncuestaCollectionNewRespEncuestaToAttach : respEncuestaCollectionNew) {
        respEncuestaCollectionNewRespEncuestaToAttach = em.getReference(
            respEncuestaCollectionNewRespEncuestaToAttach.getClass(),
            respEncuestaCollectionNewRespEncuestaToAttach.getId());
        attachedRespEncuestaCollectionNew.add(
            respEncuestaCollectionNewRespEncuestaToAttach);
      }
      respEncuestaCollectionNew = attachedRespEncuestaCollectionNew;
      referidos.setRespEncuestaCollection(respEncuestaCollectionNew);
      referidos = em.merge(referidos);

      continuar3(referenciasCollectionNew, referenciasCollectionOld, referidos);

      continuar4(
          respEncuestaCollectionNew,
          respEncuestaCollectionOld,
          referidos);
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        BigDecimal id = referidos.getId();
        if (findReferidos(id) == null) {
          throw new NonexistentEntityException(
              "The referidos with id " + id + valNoLonger);
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
      em = getEntityManager();
      Referidos referidos;
      try {
        referidos = em.getReference(Referidos.class, id);
        referidos.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException(
            "The referidos with id " + id + valNoLonger,
            enfe);
      }
      List<String> illegalOrphanMessages = null;
      Collection<Referencias> referenciasCollectionOrphanCheck = referidos.getReferenciasCollection();
      for (Referencias referenciasCollectionOrphanCheckReferencias : referenciasCollectionOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
            "This Referidos (" +
                referidos +
                ") cannot be destroyed since the Referencias " +
                referenciasCollectionOrphanCheckReferencias +
                " in its referenciasCollection field has a non-nullable referidoId field.");
      }
      Collection<RespEncuesta> respEncuestaCollectionOrphanCheck = referidos.getRespEncuestaCollection();
      for (RespEncuesta respEncuestaCollectionOrphanCheckRespEncuesta : respEncuestaCollectionOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
            "This Referidos (" +
                referidos +
                ") cannot be destroyed since the RespEncuesta " +
                respEncuestaCollectionOrphanCheckRespEncuesta +
                " in its respEncuestaCollection field has a non-nullable referidoId field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      em.remove(referidos);
    } catch (Exception ex) {
      try {
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

  public List<Referidos> findReferidosEntities() {
    return findReferidosEntities(true, -1, -1);
  }

  public List<Referidos> findReferidosEntities(
      int maxResults,
      int firstResult) {
    return findReferidosEntities(false, maxResults, firstResult);
  }

  private List<Referidos> findReferidosEntities(
      boolean all,
      int maxResults,
      int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Referidos.class));
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

  public Referidos findReferidos(BigDecimal id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Referidos.class, id);
    } finally {
      em.close();
    }
  }

  public int getReferidosCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Referidos> rt = cq.from(Referidos.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  private void continuar(
      Collection<Referencias> referenciasCollectionOld,
      Collection<Referencias> referenciasCollectionNew,
      List<String> illegalOrphanMessages) {
    for (Referencias referenciasCollectionOldReferencias : referenciasCollectionOld) {
      if (!referenciasCollectionNew.contains(referenciasCollectionOldReferencias)) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
            "You must retain Referencias " +
                referenciasCollectionOldReferencias +
                " since its referidoId field is not nullable.");
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
                " since its referidoId field is not nullable.");
      }
    }
  }

  private void continuar3(
      Collection<Referencias> referenciasCollectionNew,
      Collection<Referencias> referenciasCollectionOld,
      Referidos referidos) {
    for (Referencias referenciasCollectionNewReferencias : referenciasCollectionNew) {
      if (!referenciasCollectionOld.contains(referenciasCollectionNewReferencias)) {
        Referidos oldReferidoIdOfReferenciasCollectionNewReferencias = referenciasCollectionNewReferencias
            .getReferidoId();
        referenciasCollectionNewReferencias.setReferidoId(referidos);
        referenciasCollectionNewReferencias = em.merge(referenciasCollectionNewReferencias);
        if (oldReferidoIdOfReferenciasCollectionNewReferencias != null &&
            !oldReferidoIdOfReferenciasCollectionNewReferencias.equals(referidos)) {
          oldReferidoIdOfReferenciasCollectionNewReferencias
              .getReferenciasCollection()
              .remove(referenciasCollectionNewReferencias);
          oldReferidoIdOfReferenciasCollectionNewReferencias = em
              .merge(oldReferidoIdOfReferenciasCollectionNewReferencias);
        }
      }
    }
  }

  private void continuar4(
      Collection<RespEncuesta> respEncuestaCollectionNew,
      Collection<RespEncuesta> respEncuestaCollectionOld,
      Referidos referidos) {
    for (RespEncuesta respEncuestaCollectionNewRespEncuesta : respEncuestaCollectionNew) {
      if (!respEncuestaCollectionOld.contains(
          respEncuestaCollectionNewRespEncuesta)) {
        Referidos oldReferidoIdOfRespEncuestaCollectionNewRespEncuesta = respEncuestaCollectionNewRespEncuesta
            .getReferidoId();
        respEncuestaCollectionNewRespEncuesta.setReferidoId(referidos);
        respEncuestaCollectionNewRespEncuesta = em.merge(respEncuestaCollectionNewRespEncuesta);
        if (oldReferidoIdOfRespEncuestaCollectionNewRespEncuesta != null &&
            !oldReferidoIdOfRespEncuestaCollectionNewRespEncuesta.equals(
                referidos)) {
          oldReferidoIdOfRespEncuestaCollectionNewRespEncuesta
              .getRespEncuestaCollection()
              .remove(respEncuestaCollectionNewRespEncuesta);
          oldReferidoIdOfRespEncuestaCollectionNewRespEncuesta = em
              .merge(oldReferidoIdOfRespEncuestaCollectionNewRespEncuesta);
        }
      }
    }
  }
}
