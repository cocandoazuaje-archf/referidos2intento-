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
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Sucursales;
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
public class SucursalesJpaController implements Serializable {

  private transient EntityManager em;
  private String valAnError = "An error occurred attempting to roll back the transaction.";

  public SucursalesJpaController(
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
  private String valTheSuc = "The sucursales with id ";

  public EntityManager getEntityManager() {
    return em;
  }

  public void create(Sucursales sucursales)
      throws PreexistingEntityException, RollbackFailureException, Exception {
    if (sucursales.getEjecutivosCollection() == null) {
      sucursales.setEjecutivosCollection(new ArrayList<Ejecutivos>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      Collection<Ejecutivos> attachedEjecutivosCollection = new ArrayList<Ejecutivos>();
      for (Ejecutivos ejecutivosCollectionEjecutivosToAttach : sucursales.getEjecutivosCollection()) {
        ejecutivosCollectionEjecutivosToAttach = em.getReference(
            ejecutivosCollectionEjecutivosToAttach.getClass(),
            ejecutivosCollectionEjecutivosToAttach.getId());
        attachedEjecutivosCollection.add(
            ejecutivosCollectionEjecutivosToAttach);
      }
      sucursales.setEjecutivosCollection(attachedEjecutivosCollection);
      em.persist(sucursales);
      for (Ejecutivos ejecutivosCollectionEjecutivos : sucursales.getEjecutivosCollection()) {
        Sucursales oldSucursalIdOfEjecutivosCollectionEjecutivos = ejecutivosCollectionEjecutivos.getSucursalId();
        ejecutivosCollectionEjecutivos.setSucursalId(sucursales);
        ejecutivosCollectionEjecutivos = em.merge(ejecutivosCollectionEjecutivos);
        if (oldSucursalIdOfEjecutivosCollectionEjecutivos != null) {
          oldSucursalIdOfEjecutivosCollectionEjecutivos
              .getEjecutivosCollection()
              .remove(ejecutivosCollectionEjecutivos);
          oldSucursalIdOfEjecutivosCollectionEjecutivos = em.merge(oldSucursalIdOfEjecutivosCollectionEjecutivos);
        }
      }
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      if (findSucursales(sucursales.getId()) != null) {
        throw new PreexistingEntityException(
            "Sucursales " + sucursales + " already exists.",
            ex);
      }
      throw ex;
    } finally {
      if (em != null) {
      }
    }
  }

  public void edit(Sucursales sucursales)
      throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      Sucursales persistentSucursales = em.find(
          Sucursales.class,
          sucursales.getId());

      Collection<Ejecutivos> ejecutivosCollectionOld = persistentSucursales.getEjecutivosCollection();
      Collection<Ejecutivos> ejecutivosCollectionNew = sucursales.getEjecutivosCollection();

      List<String> illegalOrphanMessages = continuar(
          ejecutivosCollectionOld,
          ejecutivosCollectionNew);

      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      Collection<Ejecutivos> attachedEjecutivosCollectionNew = new ArrayList<Ejecutivos>();
      for (Ejecutivos ejecutivosCollectionNewEjecutivosToAttach : ejecutivosCollectionNew) {
        ejecutivosCollectionNewEjecutivosToAttach = em.getReference(
            ejecutivosCollectionNewEjecutivosToAttach.getClass(),
            ejecutivosCollectionNewEjecutivosToAttach.getId());
        attachedEjecutivosCollectionNew.add(
            ejecutivosCollectionNewEjecutivosToAttach);
      }
      ejecutivosCollectionNew = attachedEjecutivosCollectionNew;
      sucursales.setEjecutivosCollection(ejecutivosCollectionNew);
      sucursales = em.merge(sucursales);

      continuarFor(
          ejecutivosCollectionNew,
          ejecutivosCollectionOld,
          sucursales);
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        BigDecimal id = sucursales.getId();
        if (findSucursales(id) == null) {
          throw new NonexistentEntityException(valTheSuc + id + valNoLonger);
        }
      }
      throw ex;
    } finally {
      if (em != null) {
      }
    }
  }

  // inicio
  public void create2(Sucursales sucursales)
      throws PreexistingEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.persist(sucursales);
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      if (findSucursales(sucursales.getId()) != null) {
        throw new PreexistingEntityException(
            "Sucursales " + sucursales + " already exists.",
            ex);
      }
      throw ex;
    } finally {
      if (em != null) {
      }
    }
  }

  public void edit2(Sucursales sucursales)
      throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      sucursales = em.merge(sucursales);
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        BigDecimal id = sucursales.getId();
        if (findSucursales(id) == null) {
          throw new NonexistentEntityException(valTheSuc + id + valNoLonger);
        }
      }
      throw ex;
    } finally {
      if (em != null) {
      }
    }
  }

  // fin
  public void destroy(BigDecimal id)
      throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      Sucursales sucursales;
      try {
        sucursales = em.getReference(Sucursales.class, id);
        sucursales.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException(
            valTheSuc + id + valNoLonger,
            enfe);
      }
      List<String> illegalOrphanMessages = null;
      Collection<Ejecutivos> ejecutivosCollectionOrphanCheck = sucursales.getEjecutivosCollection();
      for (Ejecutivos ejecutivosCollectionOrphanCheckEjecutivos : ejecutivosCollectionOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
            "This Sucursales (" +
                sucursales +
                ") cannot be destroyed since the Ejecutivos " +
                ejecutivosCollectionOrphanCheckEjecutivos +
                " in its ejecutivosCollection field has a non-nullable sucursalId field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      em.remove(sucursales);
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

  public List<Sucursales> findSucursalesEntities() {
    return findSucursalesEntities(true, -1, -1);
  }

  public List<Sucursales> findSucursalesEntities(
      int maxResults,
      int firstResult) {
    return findSucursalesEntities(false, maxResults, firstResult);
  }

  private List<Sucursales> findSucursalesEntities(
      boolean all,
      int maxResults,
      int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Sucursales.class));
      Query q = em.createQuery(cq);
      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }
      return q.getResultList();
    } finally {
    }
  }

  public Sucursales findSucursalesByNomnre(String nombre) {
    TypedQuery<Sucursales> q = em.createNamedQuery(
        "Sucursales.findByNombre",
        Sucursales.class);
    q.setParameter("nombre", nombre);
    Sucursales suc = q.getSingleResult();

    return suc;
  }

  public Sucursales findSucursales(BigDecimal id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Sucursales.class, id);
    } finally {
    }
  }

  public int getSucursalesCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Sucursales> rt = cq.from(Sucursales.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
    }
  }

  private void continuarFor(
      Collection<Ejecutivos> ejecutivosCollectionNew,
      Collection<Ejecutivos> ejecutivosCollectionOld,
      Sucursales sucursales) {
    for (Ejecutivos ejecutivosCollectionNewEjecutivos : ejecutivosCollectionNew) {
      if (!ejecutivosCollectionOld.contains(ejecutivosCollectionNewEjecutivos)) {
        Sucursales oldSucursalIdOfEjecutivosCollectionNewEjecutivos = ejecutivosCollectionNewEjecutivos.getSucursalId();
        ejecutivosCollectionNewEjecutivos.setSucursalId(sucursales);
        ejecutivosCollectionNewEjecutivos = em.merge(ejecutivosCollectionNewEjecutivos);
        if (oldSucursalIdOfEjecutivosCollectionNewEjecutivos != null &&
            !oldSucursalIdOfEjecutivosCollectionNewEjecutivos.equals(sucursales)) {
          oldSucursalIdOfEjecutivosCollectionNewEjecutivos
              .getEjecutivosCollection()
              .remove(ejecutivosCollectionNewEjecutivos);
          oldSucursalIdOfEjecutivosCollectionNewEjecutivos = em.merge(oldSucursalIdOfEjecutivosCollectionNewEjecutivos);
        }
      }
    }
  }

  private List<String> continuar(
      Collection<Ejecutivos> ejecutivosCollectionOld,
      Collection<Ejecutivos> ejecutivosCollectionNew) {
    List<String> illegalOrphanMessages = null;
    for (Ejecutivos ejecutivosCollectionOldEjecutivos : ejecutivosCollectionOld) {
      if (!ejecutivosCollectionNew.contains(ejecutivosCollectionOldEjecutivos)) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
            "You must retain Ejecutivos " +
                ejecutivosCollectionOldEjecutivos +
                " since its sucursalId field is not nullable.");
      }
    }
    return illegalOrphanMessages;
  }
}
