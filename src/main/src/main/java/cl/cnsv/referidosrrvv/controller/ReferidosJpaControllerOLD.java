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
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Referidos;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author cow
 */
public class ReferidosJpaControllerOLD implements Serializable {

  private transient EntityManager em;
  private String valAnError = "An error occurred attempting to roll back the transaction.";

  public ReferidosJpaControllerOLD(
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
    return em;
  }

  public void create(Referidos referidos)
      throws PreexistingEntityException, RollbackFailureException, Exception {
    if (referidos.getReferenciasCollection() == null) {
      referidos.setReferenciasCollection(new ArrayList<Referencias>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      Collection<Referencias> attachedReferenciasCollection = new ArrayList<>();
      for (Referencias referenciasCollectionReferenciasToAttach : referidos.getReferenciasCollection()) {
        referenciasCollectionReferenciasToAttach = em.getReference(
            referenciasCollectionReferenciasToAttach.getClass(),
            referenciasCollectionReferenciasToAttach.getId());
        attachedReferenciasCollection.add(
            referenciasCollectionReferenciasToAttach);
      }
      referidos.setReferenciasCollection(attachedReferenciasCollection);
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
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      if (findReferidos(referidos.getId()) != null) {
        throw new PreexistingEntityException(
            "Referidos " + referidos + " already exists.",
            ex);
      }
      throw ex;
    } finally {
      if (em != null) {
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
      List<String> illegalOrphanMessages = null;

      continuar(
          referenciasCollectionOld,
          referenciasCollectionNew,
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
      referidos = em.merge(referidos);

      continuar2(referenciasCollectionNew, referenciasCollectionOld, referidos);
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
    }
  }

  public Referidos findReferidos(BigDecimal id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Referidos.class, id);
    } finally {
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
    }
  }

  public Referidos findByRut(String rut) {
    TypedQuery<Referidos> q = em.createNamedQuery(
        "Referidos.findByRut",
        Referidos.class);
    q.setParameter("rut", rut);
    List<Referidos> resp = q.getResultList();

    return (resp.size() > 0) ? resp.get(0) : null;
  }

  public Referencias getReferenciaActiva(Referidos referido) {
    Referencias resp = null;

    Collection<Referencias> refCollect = referido.getReferenciasCollection();

    for (Referencias referencias : refCollect) {
      if ("6".equals(referencias.getAccionId().toString()) ||
          "7".equals(referencias.getAccionId().toString())) {
        resp = referencias;
      }
    }

    return resp;
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
}
