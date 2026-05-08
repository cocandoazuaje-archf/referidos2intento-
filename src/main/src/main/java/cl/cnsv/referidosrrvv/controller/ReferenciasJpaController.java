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
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author cow
 */
public class ReferenciasJpaController implements Serializable {

  private transient EntityManager em;
  private String valAnError = "An error occurred attempting to roll back the transaction.";

  public ReferenciasJpaController(
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

  public void create(Referencias referencias)
      throws PreexistingEntityException, RollbackFailureException, Exception {
    if (referencias.getBitacorasCollection() == null) {
      referencias.setBitacorasCollection(new ArrayList<Bitacoras>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      Acciones accionId = referencias.getAccionId();
      if (accionId != null) {
        accionId = em.getReference(accionId.getClass(), accionId.getId());
        referencias.setAccionId(accionId);
      }
      Ejecutivos ejecutivoId = referencias.getEjecutivoId();
      if (ejecutivoId != null) {
        ejecutivoId = em.getReference(ejecutivoId.getClass(), ejecutivoId.getId());
        referencias.setEjecutivoId(ejecutivoId);
      }
      Referidos referidoId = referencias.getReferidoId();
      if (referidoId != null) {
        referidoId = em.getReference(referidoId.getClass(), referidoId.getId());
        referencias.setReferidoId(referidoId);
      }
      Collection<Bitacoras> attachedBitacorasCollection = new ArrayList<Bitacoras>();
      for (Bitacoras bitacorasCollectionBitacorasToAttach : referencias.getBitacorasCollection()) {
        bitacorasCollectionBitacorasToAttach = em.getReference(
            bitacorasCollectionBitacorasToAttach.getClass(),
            bitacorasCollectionBitacorasToAttach.getId());
        attachedBitacorasCollection.add(bitacorasCollectionBitacorasToAttach);
      }
      referencias.setBitacorasCollection(attachedBitacorasCollection);
      em.persist(referencias);

      continuar(accionId, referencias, ejecutivoId, referidoId);

      for (Bitacoras bitacorasCollectionBitacoras : referencias.getBitacorasCollection()) {
        Referencias oldReferenciaIdOfBitacorasCollectionBitacoras = bitacorasCollectionBitacoras.getReferenciaId();
        bitacorasCollectionBitacoras.setReferenciaId(referencias);
        bitacorasCollectionBitacoras = em.merge(bitacorasCollectionBitacoras);
        if (oldReferenciaIdOfBitacorasCollectionBitacoras != null) {
          oldReferenciaIdOfBitacorasCollectionBitacoras
              .getBitacorasCollection()
              .remove(bitacorasCollectionBitacoras);
          oldReferenciaIdOfBitacorasCollectionBitacoras = em.merge(oldReferenciaIdOfBitacorasCollectionBitacoras);
        }
      }
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      if (findReferencias(referencias.getId()) != null) {
        throw new PreexistingEntityException(
            "Referencias " + referencias + " already exists.",
            ex);
      }
      throw ex;
    } finally {
      if (em != null) {
      }
    }
  }

  public void edit(Referencias referencias)
      throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      Referencias persistentReferencias = em.find(
          Referencias.class,
          referencias.getId());

      Acciones accionIdOld = persistentReferencias.getAccionId();
      Acciones accionIdNew = referencias.getAccionId();
      Ejecutivos ejecutivoIdOld = persistentReferencias.getEjecutivoId();
      Ejecutivos ejecutivoIdNew = referencias.getEjecutivoId();
      Referidos referidoIdOld = persistentReferencias.getReferidoId();
      Referidos referidoIdNew = referencias.getReferidoId();

      Collection<Bitacoras> bitacorasCollectionOld = persistentReferencias.getBitacorasCollection();
      Collection<Bitacoras> bitacorasCollectionNew = referencias.getBitacorasCollection();

      List<String> illegalOrphanMessages = continuar6(
          bitacorasCollectionNew,
          bitacorasCollectionOld);

      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }

      continuar4(referidoIdNew, accionIdNew, referencias, ejecutivoIdNew);

      Collection<Bitacoras> attachedBitacorasCollectionNew = new ArrayList<Bitacoras>();
      for (Bitacoras bitacorasCollectionNewBitacorasToAttach : bitacorasCollectionNew) {
        bitacorasCollectionNewBitacorasToAttach = em.getReference(
            bitacorasCollectionNewBitacorasToAttach.getClass(),
            bitacorasCollectionNewBitacorasToAttach.getId());
        attachedBitacorasCollectionNew.add(
            bitacorasCollectionNewBitacorasToAttach);
      }
      continuar5(
          bitacorasCollectionNew,
          attachedBitacorasCollectionNew,
          referencias,
          accionIdOld,
          accionIdNew,
          ejecutivoIdNew,
          ejecutivoIdOld);

      continuar3(
          referidoIdNew,
          ejecutivoIdNew,
          ejecutivoIdOld,
          referencias,
          referidoIdOld);

      continuar2(bitacorasCollectionNew, bitacorasCollectionOld, referencias);
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        BigDecimal id = referencias.getId();
        if (findReferencias(id) == null) {
          throw new NonexistentEntityException(
              "The referencias with id " + id + valNoLonger);
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
      Referencias referencias;
      try {
        referencias = em.getReference(Referencias.class, id);
        referencias.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException(
            "The referencias with id " + id + valNoLonger,
            enfe);
      }
      List<String> illegalOrphanMessages = null;
      Collection<Bitacoras> bitacorasCollectionOrphanCheck = referencias.getBitacorasCollection();
      for (Bitacoras bitacorasCollectionOrphanCheckBitacoras : bitacorasCollectionOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
            "This Referencias (" +
                referencias +
                ") cannot be destroyed since the Bitacoras " +
                bitacorasCollectionOrphanCheckBitacoras +
                " in its bitacorasCollection field has a non-nullable referenciaId field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      Acciones accionId = referencias.getAccionId();
      if (accionId != null) {
        accionId.getReferenciasCollection().remove(referencias);
        accionId = em.merge(accionId);
      }
      Ejecutivos ejecutivoId = referencias.getEjecutivoId();
      if (ejecutivoId != null) {
        ejecutivoId.getReferenciasCollection().remove(referencias);
        ejecutivoId = em.merge(ejecutivoId);
      }
      Referidos referidoId = referencias.getReferidoId();
      if (referidoId != null) {
        referidoId.getReferenciasCollection().remove(referencias);
        referidoId = em.merge(referidoId);
      }
      em.remove(referencias);
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

  public List<Referencias> findReferenciasEntities() {
    return findReferenciasEntities(true, -1, -1);
  }

  public List<Referencias> findReferenciasEntities(
      int maxResults,
      int firstResult) {
    return findReferenciasEntities(false, maxResults, firstResult);
  }

  private List<Referencias> findReferenciasEntities(
      boolean all,
      int maxResults,
      int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Referencias.class));
      Query q = em.createQuery(cq);
      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }
      return q.getResultList();
    } finally {
    }
  }

  public Referencias findReferencias(BigDecimal id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Referencias.class, id);
    } finally {
    }
  }

  public int getReferenciasCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Referencias> rt = cq.from(Referencias.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
    }
  }

  private void continuar(
      Acciones accionId,
      Referencias referencias,
      Ejecutivos ejecutivoId,
      Referidos referidoId) {
    if (accionId != null) {
      accionId.getReferenciasCollection().add(referencias);
      accionId = em.merge(accionId);
    }
    if (ejecutivoId != null) {
      ejecutivoId.getReferenciasCollection().add(referencias);
      ejecutivoId = em.merge(ejecutivoId);
    }
    if (referidoId != null) {
      referidoId.getReferenciasCollection().add(referencias);
      referidoId = em.merge(referidoId);
    }
  }

  private void continuar2(
      Collection<Bitacoras> bitacorasCollectionNew,
      Collection<Bitacoras> bitacorasCollectionOld,
      Referencias referencias) {
    for (Bitacoras bitacorasCollectionNewBitacoras : bitacorasCollectionNew) {
      if (!bitacorasCollectionOld.contains(bitacorasCollectionNewBitacoras)) {
        Referencias oldReferenciaIdOfBitacorasCollectionNewBitacoras = bitacorasCollectionNewBitacoras
            .getReferenciaId();
        bitacorasCollectionNewBitacoras.setReferenciaId(referencias);
        bitacorasCollectionNewBitacoras = em.merge(bitacorasCollectionNewBitacoras);
        if (oldReferenciaIdOfBitacorasCollectionNewBitacoras != null &&
            !oldReferenciaIdOfBitacorasCollectionNewBitacoras.equals(referencias)) {
          oldReferenciaIdOfBitacorasCollectionNewBitacoras
              .getBitacorasCollection()
              .remove(bitacorasCollectionNewBitacoras);
          oldReferenciaIdOfBitacorasCollectionNewBitacoras = em.merge(oldReferenciaIdOfBitacorasCollectionNewBitacoras);
        }
      }
    }
  }

  private void continuar3(
      Referidos referidoIdNew,
      Ejecutivos ejecutivoIdNew,
      Ejecutivos ejecutivoIdOld,
      Referencias referencias,
      Referidos referidoIdOld) {
    if (ejecutivoIdNew != null && !ejecutivoIdNew.equals(ejecutivoIdOld)) {
      ejecutivoIdNew.getReferenciasCollection().add(referencias);
      ejecutivoIdNew = em.merge(ejecutivoIdNew);
    }
    if (referidoIdOld != null && !referidoIdOld.equals(referidoIdNew)) {
      referidoIdOld.getReferenciasCollection().remove(referencias);
      referidoIdOld = em.merge(referidoIdOld);
    }
    if (referidoIdNew != null && !referidoIdNew.equals(referidoIdOld)) {
      referidoIdNew.getReferenciasCollection().add(referencias);
      referidoIdNew = em.merge(referidoIdNew);
    }
  }

  private void continuar4(
      Referidos referidoIdNew,
      Acciones accionIdNew,
      Referencias referencias,
      Ejecutivos ejecutivoIdNew) {
    if (accionIdNew != null) {
      accionIdNew = em.getReference(accionIdNew.getClass(), accionIdNew.getId());
      referencias.setAccionId(accionIdNew);
    }
    if (ejecutivoIdNew != null) {
      ejecutivoIdNew = em.getReference(ejecutivoIdNew.getClass(), ejecutivoIdNew.getId());
      referencias.setEjecutivoId(ejecutivoIdNew);
    }
    if (referidoIdNew != null) {
      referidoIdNew = em.getReference(referidoIdNew.getClass(), referidoIdNew.getId());
      referencias.setReferidoId(referidoIdNew);
    }
  }

  private void continuar5(
      Collection<Bitacoras> bitacorasCollectionNew,
      Collection<Bitacoras> attachedBitacorasCollectionNew,
      Referencias referencias,
      Acciones accionIdOld,
      Acciones accionIdNew,
      Ejecutivos ejecutivoIdNew,
      Ejecutivos ejecutivoIdOld) {
    bitacorasCollectionNew = attachedBitacorasCollectionNew;
    referencias.setBitacorasCollection(bitacorasCollectionNew);
    referencias = em.merge(referencias);
    if (accionIdOld != null && !accionIdOld.equals(accionIdNew)) {
      accionIdOld.getReferenciasCollection().remove(referencias);
      accionIdOld = em.merge(accionIdOld);
    }
    if (accionIdNew != null && !accionIdNew.equals(accionIdOld)) {
      accionIdNew.getReferenciasCollection().add(referencias);
      accionIdNew = em.merge(accionIdNew);
    }
    if (ejecutivoIdOld != null && !ejecutivoIdOld.equals(ejecutivoIdNew)) {
      ejecutivoIdOld.getReferenciasCollection().remove(referencias);
      ejecutivoIdOld = em.merge(ejecutivoIdOld);
    }
  }

  private List<String> continuar6(
      Collection<Bitacoras> bitacorasCollectionNew,
      Collection<Bitacoras> bitacorasCollectionOld) {
    List<String> illegalOrphanMessages = null;
    for (Bitacoras bitacorasCollectionOldBitacoras : bitacorasCollectionOld) {
      if (!bitacorasCollectionNew.contains(bitacorasCollectionOldBitacoras)) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add(
            "You must retain Bitacoras " +
                bitacorasCollectionOldBitacoras +
                " since its referenciaId field is not nullable.");
      }
    }

    return illegalOrphanMessages;
  }
}
