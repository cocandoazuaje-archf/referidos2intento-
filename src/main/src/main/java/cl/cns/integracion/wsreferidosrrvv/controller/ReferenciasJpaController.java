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
import cl.cns.integracion.wsreferidosrrvv.models.*;
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
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author cow
 */
public class ReferenciasJpaController implements Serializable {

    private final transient EntityManager em;
    private String valNoLonger = " no longer exists.";

    public ReferenciasJpaController(EntityManager em) {
        this.em = em;
    }

    public void create(Referencias referencias)
            throws PreexistingEntityException, RollbackFailureException {
        if (referencias.getBitacorasCollection() == null) {
            referencias.setBitacorasCollection(new ArrayList<Bitacoras>());
        }
        try {
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
            if (accionId != null) {
                accionId.getReferenciasCollection().add(referencias);
                em.merge(accionId);
            }
            if (ejecutivoId != null) {
                ejecutivoId.getReferenciasCollection().add(referencias);
                em.merge(ejecutivoId);
            }
            if (referidoId != null) {
                referidoId.getReferenciasCollection().add(referencias);
                em.merge(referidoId);
            }
            for (Bitacoras bitacorasCollectionBitacoras : referencias.getBitacorasCollection()) {
                Referencias oldReferenciaIdOfBitacorasCollectionBitacoras = bitacorasCollectionBitacoras
                        .getReferenciaId();
                bitacorasCollectionBitacoras.setReferenciaId(referencias);
                bitacorasCollectionBitacoras = em.merge(bitacorasCollectionBitacoras);
                if (oldReferenciaIdOfBitacorasCollectionBitacoras != null) {
                    oldReferenciaIdOfBitacorasCollectionBitacoras
                            .getBitacorasCollection()
                            .remove(bitacorasCollectionBitacoras);
                    em.merge(oldReferenciaIdOfBitacorasCollectionBitacoras);
                }
            }
        } catch (Exception ex) {
            if (findReferencias(referencias.getId()) != null) {
                throw new PreexistingEntityException(
                        "Referencias " + referencias + " already exists.",
                        ex);
            }
            throw ex;
        }
    }

    public void edit(Referencias referencias)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        try {
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
            List<String> illegalOrphanMessages = null;
            for (Bitacoras bitacorasCollectionOldBitacoras : bitacorasCollectionOld) {
                if (!bitacorasCollectionNew.contains(bitacorasCollectionOldBitacoras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add(
                            "You must retain Bitacoras "
                                    + bitacorasCollectionOldBitacoras
                                    + " since its referenciaId field is not nullable.");
                }
            }
            Collection<Bitacoras> attachedBitacorasCollectionNew = continuar3(
                    illegalOrphanMessages,
                    accionIdNew,
                    referencias,
                    ejecutivoIdNew,
                    referidoIdNew,
                    bitacorasCollectionNew);
            bitacorasCollectionNew = attachedBitacorasCollectionNew;
            referencias.setBitacorasCollection(bitacorasCollectionNew);
            referencias = em.merge(referencias);
            continuar2(
                    accionIdOld,
                    accionIdNew,
                    referencias,
                    ejecutivoIdOld,
                    ejecutivoIdNew,
                    referidoIdOld,
                    referidoIdNew);
            for (Bitacoras bitacorasCollectionNewBitacoras : bitacorasCollectionNew) {
                continuar(
                        bitacorasCollectionOld,
                        bitacorasCollectionNewBitacoras,
                        referencias);
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = referencias.getId();
                if (findReferencias(id) == null) {
                    throw new NonexistentEntityException(
                            "The referencias with id " + id + valNoLonger);
                }
            }
            throw ex;
        }
    }

    public void destroy(BigDecimal id)
            throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException {
        try {
            Referencias referencias;
            referencias = getReferencias(id);
            List<String> illegalOrphanMessages = null;
            Collection<Bitacoras> bitacorasCollectionOrphanCheck = referencias.getBitacorasCollection();
            for (Bitacoras bitacorasCollectionOrphanCheckBitacoras : bitacorasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add(
                        "This Referencias ("
                                + referencias
                                + ") cannot be destroyed since the Bitacoras "
                                + bitacorasCollectionOrphanCheckBitacoras
                                + " in its bitacorasCollection field has a non-nullable referenciaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Acciones accionId = referencias.getAccionId();
            if (accionId != null) {
                accionId.getReferenciasCollection().remove(referencias);
                em.merge(accionId);
            }
            Ejecutivos ejecutivoId = referencias.getEjecutivoId();
            if (ejecutivoId != null) {
                ejecutivoId.getReferenciasCollection().remove(referencias);
                em.merge(ejecutivoId);
            }
            Referidos referidoId = referencias.getReferidoId();
            if (referidoId != null) {
                referidoId.getReferenciasCollection().remove(referencias);
                em.merge(referidoId);
            }
            em.remove(referencias);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Referencias getReferencias(BigDecimal id)
            throws NonexistentEntityException {
        Referencias referencias;
        try {
            referencias = em.getReference(Referencias.class, id);
            referencias.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException(
                    "The referencias with id " + id + valNoLonger,
                    enfe);
        }
        return referencias;
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
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Referencias.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Referencias findReferencias(BigDecimal id) {
        try {
            return em.find(Referencias.class, id);
        } finally {
        }
    }

    public int getReferenciasCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Referencias> rt = cq.from(Referencias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
        }
    }

    public Referencias findReferenciasIdReferido(Referidos referidoId) {
        Query cnq = em.createNamedQuery(
                "Referidos.findReferenciaReferido",
                Referencias.class);

        cnq.setParameter("referidoId", referidoId.getId());
        List<Referencias> lista = cnq.getResultList();
        Referencias result = (lista.size() > 0) ? lista.get(0) : null;
        return result;
    }

    private void continuar(
            Collection<Bitacoras> bitacorasCollectionOld,
            Bitacoras bitacorasCollectionNewBitacoras,
            Referencias referencias) {
        if (!bitacorasCollectionOld.contains(bitacorasCollectionNewBitacoras)) {
            Referencias oldReferenciaIdOfBitacorasCollectionNewBitacoras = bitacorasCollectionNewBitacoras
                    .getReferenciaId();
            bitacorasCollectionNewBitacoras.setReferenciaId(referencias);
            bitacorasCollectionNewBitacoras = em.merge(bitacorasCollectionNewBitacoras);
            if (oldReferenciaIdOfBitacorasCollectionNewBitacoras != null
                    && !oldReferenciaIdOfBitacorasCollectionNewBitacoras.equals(referencias)) {
                oldReferenciaIdOfBitacorasCollectionNewBitacoras
                        .getBitacorasCollection()
                        .remove(bitacorasCollectionNewBitacoras);
                em.merge(oldReferenciaIdOfBitacorasCollectionNewBitacoras);
            }
        }
    }

    private void continuar2(
            Acciones accionIdOld,
            Acciones accionIdNew,
            Referencias referencias,
            Ejecutivos ejecutivoIdOld,
            Ejecutivos ejecutivoIdNew,
            Referidos referidoIdOld,
            Referidos referidoIdNew) {
        if (accionIdOld != null && !accionIdOld.equals(accionIdNew)) {
            accionIdOld.getReferenciasCollection().remove(referencias);
            accionIdOld = em.merge(accionIdOld);
        }
        if (accionIdNew != null && !accionIdNew.equals(accionIdOld)) {
            accionIdNew.getReferenciasCollection().add(referencias);
            em.merge(accionIdNew);
        }
        if (ejecutivoIdOld != null && !ejecutivoIdOld.equals(ejecutivoIdNew)) {
            ejecutivoIdOld.getReferenciasCollection().remove(referencias);
            em.merge(ejecutivoIdOld);
        }
        if (ejecutivoIdNew != null && !ejecutivoIdNew.equals(ejecutivoIdOld)) {
            ejecutivoIdNew.getReferenciasCollection().add(referencias);
            em.merge(ejecutivoIdNew);
        }
        if (referidoIdOld != null && !referidoIdOld.equals(referidoIdNew)) {
            referidoIdOld.getReferenciasCollection().remove(referencias);
            referidoIdOld = em.merge(referidoIdOld);
        }
        if (referidoIdNew != null && !referidoIdNew.equals(referidoIdOld)) {
            referidoIdNew.getReferenciasCollection().add(referencias);
            em.merge(referidoIdNew);
        }
    }

    private Collection<Bitacoras> continuar3(
            List<String> illegalOrphanMessages,
            Acciones accionIdNew,
            Referencias referencias,
            Ejecutivos ejecutivoIdNew,
            Referidos referidoIdNew,
            Collection<Bitacoras> bitacorasCollectionNew) throws IllegalOrphanException {
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
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
        Collection<Bitacoras> attachedBitacorasCollectionNew = new ArrayList<Bitacoras>();
        for (Bitacoras bitacorasCollectionNewBitacorasToAttach : bitacorasCollectionNew) {
            bitacorasCollectionNewBitacorasToAttach = em.getReference(
                    bitacorasCollectionNewBitacorasToAttach.getClass(),
                    bitacorasCollectionNewBitacorasToAttach.getId());
            attachedBitacorasCollectionNew.add(
                    bitacorasCollectionNewBitacorasToAttach);
        }
        return bitacorasCollectionNew;
    }
}
