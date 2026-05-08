/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.controller;

import cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Referencias;
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
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class EjecutivosJpaController implements Serializable {

    private transient EntityManager em;
    private String valNoLonger = " no longer exists.";
    private static final Logger LOGGER = Logger.getLogger(
            EjecutivosJpaController.class.getName());

    public EjecutivosJpaController(EntityManager em) {
        this.em = em;
    }

    public void create(Ejecutivos ejecutivos)
            throws PreexistingEntityException, RollbackFailureException {
        if (ejecutivos.getReferenciasCollection() == null) {
            ejecutivos.setReferenciasCollection(new ArrayList<Referencias>());
        }
        try {
            Sucursales sucursalId = ejecutivos.getSucursalId();
            if (sucursalId != null) {
                sucursalId = em.getReference(sucursalId.getClass(), sucursalId.getId());
                ejecutivos.setSucursalId(sucursalId);
            }
            Collection<Referencias> attachedReferenciasCollection = new ArrayList<Referencias>();
            for (Referencias referenciasCollectionReferenciasToAttach : ejecutivos.getReferenciasCollection()) {
                referenciasCollectionReferenciasToAttach = em.getReference(
                        referenciasCollectionReferenciasToAttach.getClass(),
                        referenciasCollectionReferenciasToAttach.getId());
                attachedReferenciasCollection.add(
                        referenciasCollectionReferenciasToAttach);
            }
            ejecutivos.setReferenciasCollection(attachedReferenciasCollection);
            em.persist(ejecutivos);
            if (sucursalId != null) {
                sucursalId.getEjecutivosCollection().add(ejecutivos);
                em.merge(sucursalId);
            }
            for (Referencias referenciasCollectionReferencias : ejecutivos.getReferenciasCollection()) {
                Ejecutivos oldEjecutivoIdOfReferenciasCollectionReferencias = referenciasCollectionReferencias
                        .getEjecutivoId();
                referenciasCollectionReferencias.setEjecutivoId(ejecutivos);
                referenciasCollectionReferencias = em.merge(referenciasCollectionReferencias);
                if (oldEjecutivoIdOfReferenciasCollectionReferencias != null) {
                    oldEjecutivoIdOfReferenciasCollectionReferencias
                            .getReferenciasCollection()
                            .remove(referenciasCollectionReferencias);
                    em.merge(oldEjecutivoIdOfReferenciasCollectionReferencias);
                }
            }
        } catch (Exception ex) {
            if (findEjecutivos(ejecutivos.getId()) != null) {
                throw new PreexistingEntityException(
                        "Ejecutivos " + ejecutivos + " already exists.",
                        ex);
            }
            throw ex;
        }
    }

    public void edit(Ejecutivos ejecutivos)
            throws NonexistentEntityException, RollbackFailureException {
        try {
            Ejecutivos persistentEjecutivos = em.find(
                    Ejecutivos.class,
                    ejecutivos.getId());
            Sucursales sucursalIdOld = persistentEjecutivos.getSucursalId();
            Sucursales sucursalIdNew = ejecutivos.getSucursalId();
            Collection<Referencias> referenciasCollectionOld = persistentEjecutivos.getReferenciasCollection();
            Collection<Referencias> referenciasCollectionNew = ejecutivos.getReferenciasCollection();
            if (sucursalIdNew != null) {
                sucursalIdNew = em.getReference(sucursalIdNew.getClass(), sucursalIdNew.getId());
                ejecutivos.setSucursalId(sucursalIdNew);
            }
            Collection<Referencias> attachedReferenciasCollectionNew = new ArrayList<Referencias>();
            for (Referencias referenciasCollectionNewReferenciasToAttach : referenciasCollectionNew) {
                referenciasCollectionNewReferenciasToAttach = em.getReference(
                        referenciasCollectionNewReferenciasToAttach.getClass(),
                        referenciasCollectionNewReferenciasToAttach.getId());
                attachedReferenciasCollectionNew.add(
                        referenciasCollectionNewReferenciasToAttach);
            }
            continuar2(
                    referenciasCollectionNew,
                    ejecutivos,
                    sucursalIdOld,
                    attachedReferenciasCollectionNew,
                    sucursalIdNew,
                    referenciasCollectionOld);
            for (Referencias referenciasCollectionNewReferencias : referenciasCollectionNew) {
                continuar(
                        referenciasCollectionOld,
                        referenciasCollectionNewReferencias,
                        ejecutivos);
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = ejecutivos.getId();
                if (findEjecutivos(id) == null) {
                    throw new NonexistentEntityException(
                            "The ejecutivos with id " + id + valNoLonger);
                }
            }
            throw ex;
        }
    }

    public void destroy(BigDecimal id)
            throws NonexistentEntityException, RollbackFailureException {
        try {
            Ejecutivos ejecutivos;
            ejecutivos = getEjecutivos(id);
            Sucursales sucursalId = ejecutivos.getSucursalId();
            if (sucursalId != null) {
                sucursalId.getEjecutivosCollection().remove(ejecutivos);
                em.merge(sucursalId);
            }
            Collection<Referencias> referenciasCollection = ejecutivos.getReferenciasCollection();
            for (Referencias referenciasCollectionReferencias : referenciasCollection) {
                referenciasCollectionReferencias.setEjecutivoId(null);
                em.merge(referenciasCollectionReferencias);
            }
            em.remove(ejecutivos);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Ejecutivos getEjecutivos(BigDecimal id)
            throws NonexistentEntityException {
        Ejecutivos ejecutivos;
        try {
            ejecutivos = em.getReference(Ejecutivos.class, id);
            ejecutivos.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException(
                    "The ejecutivos with id " + id + valNoLonger,
                    enfe);
        }
        return ejecutivos;
    }

    public List<Ejecutivos> findEjecutivosEntities() {
        return findEjecutivosEntities(true, -1, -1);
    }

    public List<Ejecutivos> findEjecutivosEntities(
            int maxResults,
            int firstResult) {
        return findEjecutivosEntities(false, maxResults, firstResult);
    }

    private List<Ejecutivos> findEjecutivosEntities(
            boolean all,
            int maxResults,
            int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Ejecutivos.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Ejecutivos findEjecutivos(BigDecimal id) {
        return em.find(Ejecutivos.class, id);
    }

    public int getEjecutivosCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Ejecutivos> rt = cq.from(Ejecutivos.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Ejecutivos findByCodEjecutiva(String codEjecutiva) {
        Ejecutivos resp = null;

        TypedQuery<Ejecutivos> q = em.createNamedQuery(
                "Ejecutivos.findByCodigo",
                Ejecutivos.class);
        q.setParameter("codigo", codEjecutiva);
        try {
            resp = q.getSingleResult();
        } catch (Exception e) {
            resp = null;
            LOGGER.info(e);
        }

        return resp;
    }

    private void continuar(
            Collection<Referencias> referenciasCollectionOld,
            Referencias referenciasCollectionNewReferencias,
            Ejecutivos ejecutivos) {
        if (!referenciasCollectionOld.contains(referenciasCollectionNewReferencias)) {
            Ejecutivos oldEjecutivoIdOfReferenciasCollectionNewReferencias = referenciasCollectionNewReferencias
                    .getEjecutivoId();
            referenciasCollectionNewReferencias.setEjecutivoId(ejecutivos);
            referenciasCollectionNewReferencias = em.merge(referenciasCollectionNewReferencias);
            if (oldEjecutivoIdOfReferenciasCollectionNewReferencias != null
                    && !oldEjecutivoIdOfReferenciasCollectionNewReferencias.equals(ejecutivos)) {
                oldEjecutivoIdOfReferenciasCollectionNewReferencias
                        .getReferenciasCollection()
                        .remove(referenciasCollectionNewReferencias);
                em.merge(oldEjecutivoIdOfReferenciasCollectionNewReferencias);
            }
        }
    }

    private void continuar2(
            Collection<Referencias> referenciasCollectionNew,
            Ejecutivos ejecutivos,
            Sucursales sucursalIdOld,
            Collection<Referencias> attachedReferenciasCollectionNew,
            Sucursales sucursalIdNew,
            Collection<Referencias> referenciasCollectionOld) {
        referenciasCollectionNew = attachedReferenciasCollectionNew;
        ejecutivos.setReferenciasCollection(referenciasCollectionNew);
        ejecutivos = em.merge(ejecutivos);
        if (sucursalIdOld != null && !sucursalIdOld.equals(sucursalIdNew)) {
            sucursalIdOld.getEjecutivosCollection().remove(ejecutivos);
            sucursalIdOld = em.merge(sucursalIdOld);
        }
        if (sucursalIdNew != null && !sucursalIdNew.equals(sucursalIdOld)) {
            sucursalIdNew.getEjecutivosCollection().add(ejecutivos);
            em.merge(sucursalIdNew);
        }
        for (Referencias referenciasCollectionOldReferencias : referenciasCollectionOld) {
            if (!referenciasCollectionNew.contains(referenciasCollectionOldReferencias)) {
                referenciasCollectionOldReferencias.setEjecutivoId(null);
                em.merge(referenciasCollectionOldReferencias);
            }
        }
    }
}
