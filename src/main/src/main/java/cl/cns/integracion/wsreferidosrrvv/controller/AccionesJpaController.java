/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.controller;

import cl.cns.integracion.wsreferidosrrvv.exceptions.IllegalOrphanException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Referencias;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author cow
 */
public class AccionesJpaController implements Serializable {

    private final transient EntityManager em;
    private String valNoLonger = " no longer exists.";

    public AccionesJpaController(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public void create(Acciones acciones)
            throws PreexistingEntityException, RollbackFailureException {
        if (acciones.getReferenciasCollection() == null) {
            acciones.setReferenciasCollection(new ArrayList<Referencias>());
        }
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManager();
            Collection<Referencias> attachedReferenciasCollection = new ArrayList<Referencias>();
            for (Referencias referenciasCollectionReferenciasToAttach : acciones.getReferenciasCollection()) {
                referenciasCollectionReferenciasToAttach = entityManager.getReference(
                        referenciasCollectionReferenciasToAttach.getClass(),
                        referenciasCollectionReferenciasToAttach.getId());
                attachedReferenciasCollection.add(
                        referenciasCollectionReferenciasToAttach);
            }
            acciones.setReferenciasCollection(attachedReferenciasCollection);
            entityManager.persist(acciones);
            for (Referencias referenciasCollectionReferencias : acciones.getReferenciasCollection()) {
                Acciones oldAccionIdOfReferenciasCollectionReferencias = referenciasCollectionReferencias.getAccionId();
                referenciasCollectionReferencias.setAccionId(acciones);
                referenciasCollectionReferencias = entityManager.merge(referenciasCollectionReferencias);
                if (oldAccionIdOfReferenciasCollectionReferencias != null) {
                    oldAccionIdOfReferenciasCollectionReferencias
                            .getReferenciasCollection()
                            .remove(referenciasCollectionReferencias);
                    entityManager.merge(oldAccionIdOfReferenciasCollectionReferencias);
                }
            }
        } catch (Exception ex) {
            if (findAcciones(acciones.getId()) != null) {
                throw new PreexistingEntityException(
                        "Acciones " + acciones + " already exists.",
                        ex);
            }
            throw ex;
        }
    }

    public void edit(Acciones acciones)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManager();
            Acciones persistentAcciones = entityManager.find(
                    Acciones.class,
                    acciones.getId());
            Collection<Referencias> referenciasCollectionOld = persistentAcciones.getReferenciasCollection();
            Collection<Referencias> referenciasCollectionNew = acciones.getReferenciasCollection();
            List<String> illegalOrphanMessages = null;
            illegalOrphanMessages = verificaReferencias(
                    referenciasCollectionOld,
                    referenciasCollectionNew,
                    illegalOrphanMessages);
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Referencias> attachedReferenciasCollectionNew = new ArrayList<Referencias>();
            for (Referencias referenciasCollectionNewReferenciasToAttach : referenciasCollectionNew) {
                referenciasCollectionNewReferenciasToAttach = entityManager.getReference(
                        referenciasCollectionNewReferenciasToAttach.getClass(),
                        referenciasCollectionNewReferenciasToAttach.getId());
                attachedReferenciasCollectionNew.add(
                        referenciasCollectionNewReferenciasToAttach);
            }
            referenciasCollectionNew = attachedReferenciasCollectionNew;
            acciones.setReferenciasCollection(referenciasCollectionNew);
            acciones = entityManager.merge(acciones);
            actualizaReferencias(
                    acciones,
                    entityManager,
                    referenciasCollectionOld,
                    referenciasCollectionNew);
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = acciones.getId();
                if (findAcciones(id) == null) {
                    throw new NonexistentEntityException(
                            "The acciones with id " + id + valNoLonger);
                }
            }
            throw ex;
        }
    }

    private void actualizaReferencias(
            Acciones acciones,
            EntityManager em,
            Collection<Referencias> referenciasCollectionOld,
            Collection<Referencias> referenciasCollectionNew) {
        for (Referencias referenciasCollectionNewReferencias : referenciasCollectionNew) {
            if (!referenciasCollectionOld.contains(referenciasCollectionNewReferencias)) {
                Acciones oldAccionIdOfReferenciasCollectionNewReferencias = referenciasCollectionNewReferencias
                        .getAccionId();
                referenciasCollectionNewReferencias.setAccionId(acciones);
                referenciasCollectionNewReferencias = em.merge(referenciasCollectionNewReferencias);
                if (oldAccionIdOfReferenciasCollectionNewReferencias != null
                        && !oldAccionIdOfReferenciasCollectionNewReferencias.equals(acciones)) {
                    oldAccionIdOfReferenciasCollectionNewReferencias
                            .getReferenciasCollection()
                            .remove(referenciasCollectionNewReferencias);
                    em.merge(oldAccionIdOfReferenciasCollectionNewReferencias);
                }
            }
        }
    }

    private List<String> verificaReferencias(
            Collection<Referencias> referenciasCollectionOld,
            Collection<Referencias> referenciasCollectionNew,
            List<String> illegalOrphanMessages) {
        for (Referencias referenciasCollectionOldReferencias : referenciasCollectionOld) {
            if (!referenciasCollectionNew.contains(referenciasCollectionOldReferencias)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add(
                        "You must retain Referencias "
                                + referenciasCollectionOldReferencias
                                + " since its accionId field is not nullable.");
            }
        }
        return illegalOrphanMessages;
    }

    public void destroy(BigDecimal id)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManager();
            Acciones acciones;
            acciones = getAcciones(id, entityManager);
            List<String> illegalOrphanMessages = null;
            Collection<Referencias> referenciasCollectionOrphanCheck = acciones.getReferenciasCollection();
            for (Referencias referenciasCollectionOrphanCheckReferencias : referenciasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add(
                        "This Acciones ("
                                + acciones
                                + ") cannot be destroyed since the Referencias "
                                + referenciasCollectionOrphanCheckReferencias
                                + " in its referenciasCollection field has a non-nullable accionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            entityManager.remove(acciones);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Acciones getAcciones(BigDecimal id, EntityManager entityManager)
            throws NonexistentEntityException {
        Acciones acciones;
        try {
            acciones = entityManager.getReference(Acciones.class, id);
            acciones.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException(
                    "The acciones with id " + id + valNoLonger,
                    enfe);
        }
        return acciones;
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
            int firstResult) {
        EntityManager entityManager = getEntityManager();
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Acciones.class));
        Query q = entityManager.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Acciones findAcciones(BigDecimal id) {
        EntityManager entityManager = getEntityManager();
        return entityManager.find(Acciones.class, id);
    }

    public int getAccionesCount() {
        EntityManager entityManager = getEntityManager();
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        Root<Acciones> rt = cq.from(Acciones.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        Query q = entityManager.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Acciones findByNombre(String nombre) {
        Acciones resp = null;
        EntityManager entityManager = getEntityManager();
        TypedQuery<Acciones> q = entityManager.createNamedQuery(
                "Acciones.findByNombre",
                Acciones.class);
        q.setParameter("nombre", nombre);
        resp = q.getSingleResult();

        return resp;
    }
}
