/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.controller;

import cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Rolesusuarios;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author cow
 */
public class RolesusuariosJpaController implements Serializable {

    private final transient EntityManager em;
    private String valNoLonger = " no longer exists.";

    public RolesusuariosJpaController(EntityManager em) {
        this.em = em;
    }

    public void create(Rolesusuarios rolesusuarios)
            throws PreexistingEntityException, RollbackFailureException {
        try {
            em.persist(rolesusuarios);
        } catch (Exception ex) {
            if (findRolesusuarios(rolesusuarios.getId()) != null) {
                throw new PreexistingEntityException(
                        "Rolesusuarios " + rolesusuarios + " already exists.",
                        ex);
            }
            throw ex;
        }
    }

    public void edit(Rolesusuarios rolesusuarios)
            throws NonexistentEntityException, RollbackFailureException {
        try {
            em.merge(rolesusuarios);
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = rolesusuarios.getId();
                if (findRolesusuarios(id) == null) {
                    throw new NonexistentEntityException(
                            "The rolesusuarios with id " + id + valNoLonger);
                }
            }
            throw ex;
        }
    }

    public void destroy(BigDecimal id)
            throws NonexistentEntityException, RollbackFailureException {
        try {
            Rolesusuarios rolesusuarios;
            rolesusuarios = getRolesusuarios(id);
            em.remove(rolesusuarios);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Rolesusuarios getRolesusuarios(BigDecimal id)
            throws NonexistentEntityException {
        Rolesusuarios rolesusuarios;
        try {
            rolesusuarios = em.getReference(Rolesusuarios.class, id);
            rolesusuarios.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException(
                    "The rolesusuarios with id " + id + valNoLonger,
                    enfe);
        }
        return rolesusuarios;
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
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Rolesusuarios.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Rolesusuarios findRolesusuarios(BigDecimal id) {
        return em.find(Rolesusuarios.class, id);
    }

    public int getRolesusuariosCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Rolesusuarios> rt = cq.from(Rolesusuarios.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
