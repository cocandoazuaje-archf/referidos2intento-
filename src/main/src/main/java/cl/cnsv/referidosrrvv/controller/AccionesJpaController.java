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
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Referencias;
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
public class AccionesJpaController implements Serializable {

  private transient EntityManager em;

  private String valAnError =
    "An error occurred attempting to roll back the transaction.";

  public AccionesJpaController(
    UserTransaction utx,
    EntityManagerFactory emf,
    EntityManager em
  ) {
    this.utx = utx;
    this.emf = emf;
    this.em = em;
  }

  private transient UserTransaction utx = null;
  private transient EntityManagerFactory emf = null;
  private transient String valNoLonger = " no longer exists.";

  public EntityManager getEntityManager() {
    return em;
  }

  public void create(Acciones acciones)
    throws PreexistingEntityException, RollbackFailureException, Exception {
    if (acciones.getReferenciasCollection() == null) {
      acciones.setReferenciasCollection(new ArrayList<Referencias>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      Collection<Referencias> attachedReferenciasCollection = new ArrayList<Referencias>();
      for (Referencias referenciasCollectionReferenciasToAttach : acciones.getReferenciasCollection()) {
        referenciasCollectionReferenciasToAttach =
          em.getReference(
            referenciasCollectionReferenciasToAttach.getClass(),
            referenciasCollectionReferenciasToAttach.getId()
          );
        attachedReferenciasCollection.add(
          referenciasCollectionReferenciasToAttach
        );
      }
      acciones.setReferenciasCollection(attachedReferenciasCollection);
      em.persist(acciones);
      for (Referencias referenciasCollectionReferencias : acciones.getReferenciasCollection()) {
        Acciones oldAccionIdOfReferenciasCollectionReferencias = referenciasCollectionReferencias.getAccionId();
        referenciasCollectionReferencias.setAccionId(acciones);
        referenciasCollectionReferencias =
          em.merge(referenciasCollectionReferencias);
        if (oldAccionIdOfReferenciasCollectionReferencias != null) {
          oldAccionIdOfReferenciasCollectionReferencias
            .getReferenciasCollection()
            .remove(referenciasCollectionReferencias);
          oldAccionIdOfReferenciasCollectionReferencias =
            em.merge(oldAccionIdOfReferenciasCollectionReferencias);
        }
      }
    } catch (Exception ex) {
      try {} catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      if (findAcciones(acciones.getId()) != null) {
        throw new PreexistingEntityException(
          "Acciones " + acciones + " already exists.",
          ex
        );
      }
      throw ex;
    } finally {
      if (em != null) {}
    }
  }

  public void edit(Acciones acciones)
    throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      Acciones persistentAcciones = em.find(Acciones.class, acciones.getId());
      Collection<Referencias> referenciasCollectionOld = persistentAcciones.getReferenciasCollection();
      Collection<Referencias> referenciasCollectionNew = acciones.getReferenciasCollection();
      List<String> illegalOrphanMessages = null;

      continuar2(
        referenciasCollectionOld,
        referenciasCollectionNew,
        illegalOrphanMessages
      );

      Collection<Referencias> attachedReferenciasCollectionNew = new ArrayList<Referencias>();
      for (Referencias referenciasCollectionNewReferenciasToAttach : referenciasCollectionNew) {
        referenciasCollectionNewReferenciasToAttach =
          em.getReference(
            referenciasCollectionNewReferenciasToAttach.getClass(),
            referenciasCollectionNewReferenciasToAttach.getId()
          );
        attachedReferenciasCollectionNew.add(
          referenciasCollectionNewReferenciasToAttach
        );
      }
      referenciasCollectionNew = attachedReferenciasCollectionNew;
      acciones.setReferenciasCollection(referenciasCollectionNew);
      acciones = em.merge(acciones);

      continuar(referenciasCollectionNew, referenciasCollectionOld, acciones);
    } catch (Exception ex) {
      try {} catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        BigDecimal id = acciones.getId();
        if (findAcciones(id) == null) {
          throw new NonexistentEntityException(
            "The acciones with id " + id + valNoLonger
          );
        }
      }
      throw ex;
    } finally {
      if (em != null) {}
    }
  }

  public void destroy(BigDecimal id)
    throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      Acciones acciones;
      try {
        acciones = em.getReference(Acciones.class, id);
        acciones.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException(
          "The acciones with id " + id + valNoLonger,
          enfe
        );
      }
      List<String> illegalOrphanMessages = null;
      Collection<Referencias> referenciasCollectionOrphanCheck = acciones.getReferenciasCollection();
      for (Referencias referenciasCollectionOrphanCheckReferencias : referenciasCollectionOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
          "This Acciones (" +
          acciones +
          ") cannot be destroyed since the Referencias " +
          referenciasCollectionOrphanCheckReferencias +
          " in its referenciasCollection field has a non-nullable accionId field."
        );
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      em.remove(acciones);
    } catch (Exception ex) {
      try {} catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      throw ex;
    } finally {
      if (em != null) {}
    }
  }

  public List<Acciones> findAccionesEntities() {
    return findAccionesEntities(true, -1, -1);
  }

  public List<Acciones> findAccionesEntities(int maxResults, int firstResult) {
    return findAccionesEntities(false, maxResults, firstResult);
  }

  private List<Acciones> findAccionesEntities(
    boolean all,
    int maxResults,
    int firstResult
  ) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Acciones.class));
      Query q = em.createQuery(cq);
      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }
      return q.getResultList();
    } finally {}
  }

  public Acciones findAcciones(BigDecimal id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Acciones.class, id);
    } finally {}
  }

  public int getAccionesCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Acciones> rt = cq.from(Acciones.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {}
  }

  public Acciones findByNombre(String nombre) {
    Acciones resp = null;
    EntityManager em = getEntityManager();
    TypedQuery<Acciones> q = em.createNamedQuery(
      "Acciones.findByNombre",
      Acciones.class
    );
    q.setParameter("nombre", nombre);
    resp = q.getSingleResult();

    return resp;
  }

  private void continuar(
    Collection<Referencias> referenciasCollectionNew,
    Collection<Referencias> referenciasCollectionOld,
    Acciones acciones
  ) {
    for (Referencias referenciasCollectionNewReferencias : referenciasCollectionNew) {
      if (
        !referenciasCollectionOld.contains(referenciasCollectionNewReferencias)
      ) {
        Acciones oldAccionIdOfReferenciasCollectionNewReferencias = referenciasCollectionNewReferencias.getAccionId();
        referenciasCollectionNewReferencias.setAccionId(acciones);
        referenciasCollectionNewReferencias =
          em.merge(referenciasCollectionNewReferencias);
        if (
          oldAccionIdOfReferenciasCollectionNewReferencias != null &&
          !oldAccionIdOfReferenciasCollectionNewReferencias.equals(acciones)
        ) {
          oldAccionIdOfReferenciasCollectionNewReferencias
            .getReferenciasCollection()
            .remove(referenciasCollectionNewReferencias);
          oldAccionIdOfReferenciasCollectionNewReferencias =
            em.merge(oldAccionIdOfReferenciasCollectionNewReferencias);
        }
      }
    }
  }

  private void continuar2(
    Collection<Referencias> referenciasCollectionOld,
    Collection<Referencias> referenciasCollectionNew,
    List<String> illegalOrphanMessages
  ) throws IllegalOrphanException {
    for (Referencias referenciasCollectionOldReferencias : referenciasCollectionOld) {
      if (
        !referenciasCollectionNew.contains(referenciasCollectionOldReferencias)
      ) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
          "You must retain Referencias " +
          referenciasCollectionOldReferencias +
          " since its accionId field is not nullable."
        );
      }
    }
    if (illegalOrphanMessages != null) {
      throw new IllegalOrphanException(illegalOrphanMessages);
    }
  }
}
