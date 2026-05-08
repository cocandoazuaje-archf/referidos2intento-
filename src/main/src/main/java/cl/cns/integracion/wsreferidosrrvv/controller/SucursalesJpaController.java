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
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Sucursales;
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
public class SucursalesJpaController implements Serializable {

    private final transient EntityManager em;
    private String valNoLonger = " no longer exists.";
    private String valTheSuc;

    public SucursalesJpaController(EntityManager em) {
        this.em = em;
    }

    public void create(Sucursales sucursales)
            throws PreexistingEntityException, RollbackFailureException {
        if (sucursales.getEjecutivosCollection() == null) {
            sucursales.setEjecutivosCollection(new ArrayList<Ejecutivos>());
        }
        try {
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
                Sucursales oldSucursalIdOfEjecutivosCollectionEjecutivos = ejecutivosCollectionEjecutivos
                        .getSucursalId();
                ejecutivosCollectionEjecutivos.setSucursalId(sucursales);
                ejecutivosCollectionEjecutivos = em.merge(ejecutivosCollectionEjecutivos);
                if (oldSucursalIdOfEjecutivosCollectionEjecutivos != null) {
                    oldSucursalIdOfEjecutivosCollectionEjecutivos
                            .getEjecutivosCollection()
                            .remove(ejecutivosCollectionEjecutivos);
                    em.merge(oldSucursalIdOfEjecutivosCollectionEjecutivos);
                }
            }
        } catch (Exception ex) {
            if (findSucursales(sucursales.getId()) != null) {
                throw new PreexistingEntityException(
                        "Sucursales " + sucursales + " already exists.",
                        ex);
            }
            throw ex;
        }
    }

    public void edit(Sucursales sucursales)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        try {
            Sucursales persistentSucursales = em.find(
                    Sucursales.class,
                    sucursales.getId());
            Collection<Ejecutivos> ejecutivosCollectionOld = persistentSucursales.getEjecutivosCollection();
            Collection<Ejecutivos> ejecutivosCollectionNew = sucursales.getEjecutivosCollection();
            List<String> illegalOrphanMessages = null;
            for (Ejecutivos ejecutivosCollectionOldEjecutivos : ejecutivosCollectionOld) {
                continuar2(
                        ejecutivosCollectionNew,
                        ejecutivosCollectionOldEjecutivos,
                        illegalOrphanMessages);
            }
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
            for (Ejecutivos ejecutivosCollectionNewEjecutivos : ejecutivosCollectionNew) {
                continuar(
                        ejecutivosCollectionOld,
                        ejecutivosCollectionNewEjecutivos,
                        sucursales);
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = sucursales.getId();
                if (findSucursales(id) == null) {
                    throw new NonexistentEntityException(
                            valTheSuc + id + valNoLonger);
                }
            }
            throw ex;
        }
    }

    public void destroy(BigDecimal id)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        try {
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
                        "This Sucursales ("
                                + sucursales
                                + ") cannot be destroyed since the Ejecutivos "
                                + ejecutivosCollectionOrphanCheckEjecutivos
                                + " in its ejecutivosCollection field has a non-nullable sucursalId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(sucursales);
        } catch (Exception ex) {
            throw ex;
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
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Sucursales.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
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
        return em.find(Sucursales.class, id);
    }

    public int getSucursalesCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Sucursales> rt = cq.from(Sucursales.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    private void continuar(
            Collection<Ejecutivos> ejecutivosCollectionOld,
            Ejecutivos ejecutivosCollectionNewEjecutivos,
            Sucursales sucursales) {
        if (!ejecutivosCollectionOld.contains(ejecutivosCollectionNewEjecutivos)) {
            Sucursales oldSucursalIdOfEjecutivosCollectionNewEjecutivos = ejecutivosCollectionNewEjecutivos
                    .getSucursalId();
            ejecutivosCollectionNewEjecutivos.setSucursalId(sucursales);
            ejecutivosCollectionNewEjecutivos = em.merge(ejecutivosCollectionNewEjecutivos);
            if (oldSucursalIdOfEjecutivosCollectionNewEjecutivos != null
                    && !oldSucursalIdOfEjecutivosCollectionNewEjecutivos.equals(sucursales)) {
                oldSucursalIdOfEjecutivosCollectionNewEjecutivos
                        .getEjecutivosCollection()
                        .remove(ejecutivosCollectionNewEjecutivos);
                em.merge(oldSucursalIdOfEjecutivosCollectionNewEjecutivos);
            }
        }
    }

    private void continuar2(
            Collection<Ejecutivos> ejecutivosCollectionNew,
            Ejecutivos ejecutivosCollectionOldEjecutivos,
            List<String> illegalOrphanMessages) {
        if (!ejecutivosCollectionNew.contains(ejecutivosCollectionOldEjecutivos)) {
            if (illegalOrphanMessages == null) {
                illegalOrphanMessages = new ArrayList<String>();
            }
            illegalOrphanMessages.add(
                    "You must retain Ejecutivos "
                            + ejecutivosCollectionOldEjecutivos
                            + " since its sucursalId field is not nullable.");
        }
    }
}
