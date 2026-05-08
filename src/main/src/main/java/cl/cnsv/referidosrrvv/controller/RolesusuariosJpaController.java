/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.controller;

import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.PreexistingEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Rolesusuarios;
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
 * @author cow
 */
public class RolesusuariosJpaController implements Serializable {

  private transient EntityManager em;
  private String valAnError = "An error occurred attempting to roll back the transaction.";

  public RolesusuariosJpaController(
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

  public void create(Rolesusuarios rolesusuarios)
      throws PreexistingEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.persist(rolesusuarios);
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      if (findRolesusuarios(rolesusuarios.getId()) != null) {
        throw new PreexistingEntityException(
            "Rolesusuarios " + rolesusuarios + " already exists.",
            ex);
      }
      throw ex;
    } finally {
      if (em != null) {
      }
    }
  }

  public void edit(Rolesusuarios rolesusuarios)
      throws NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      rolesusuarios = em.merge(rolesusuarios);
    } catch (Exception ex) {
      try {
      } catch (Exception re) {
        throw new RollbackFailureException(valAnError, re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        BigDecimal id = rolesusuarios.getId();
        if (findRolesusuarios(id) == null) {
          throw new NonexistentEntityException(
              "The rolesusuarios with id " + id + valNoLonger);
        }
      }
      throw ex;
    } finally {
      if (em != null) {
      }
    }
  }

  public void destroy(BigDecimal id)
      throws NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      Rolesusuarios rolesusuarios;
      try {
        rolesusuarios = em.getReference(Rolesusuarios.class, id);
        rolesusuarios.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException(
            "The rolesusuarios with id " + id + valNoLonger,
            enfe);
      }
      em.remove(rolesusuarios);
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

  public List<Rolesusuarios> findRolesusuariosEntities() {
    return findRolesusuariosEntities(true, -1, -1);
  }

  public List<Rolesusuarios> findRolesusuariosEntities(
      int maxResults,
      int firstResult) {
    return findRolesusuariosEntities(false, maxResults, firstResult);
  }

  private List<Rolesusuarios> findRolesusuariosEntities(
      boolean all,
      int maxResults,
      int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Rolesusuarios.class));
      Query q = em.createQuery(cq);
      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }
      return q.getResultList();
    } finally {
    }
  }

  public Rolesusuarios findRolesusuarios(BigDecimal id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Rolesusuarios.class, id);
    } finally {
    }
  }

  public int getRolesusuariosCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Rolesusuarios> rt = cq.from(Rolesusuarios.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
    }
  }
}
