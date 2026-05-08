/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.controller;

import cl.cns.integracion.wsreferidosrrvv.clases.Resources;
import cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Referencias;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class BitacorasJpaController implements Serializable {

    private final transient EntityManager em;

    private String valNoLonger = " no longer exists.";
    private static final Logger LOGGER = Logger.getLogger(
            BitacorasJpaController.class.getName());

    public BitacorasJpaController(EntityManager em) {
        this.em = em;
    }

    public void create(Bitacoras bitacoras)
            throws PreexistingEntityException, RollbackFailureException {
        try {
            Referencias referenciaId = bitacoras.getReferenciaId();
            if (referenciaId != null) {
                referenciaId = em.getReference(referenciaId.getClass(), referenciaId.getId());
                bitacoras.setReferenciaId(referenciaId);
            }
            em.persist(bitacoras);
            if (referenciaId != null) {
                referenciaId.getBitacorasCollection().add(bitacoras);
                em.merge(referenciaId);
            }
        } catch (Exception ex) {
            if (findBitacoras(bitacoras.getId()) != null) {
                throw new PreexistingEntityException(
                        "Bitacoras " + bitacoras + " already exists.",
                        ex);
            }
            throw ex;
        }
    }

    public void edit(Bitacoras bitacoras)
            throws NonexistentEntityException, RollbackFailureException {
        try {
            Bitacoras persistentBitacoras = em.find(
                    Bitacoras.class,
                    bitacoras.getId());
            Referencias referenciaIdOld = persistentBitacoras.getReferenciaId();
            Referencias referenciaIdNew = bitacoras.getReferenciaId();
            if (referenciaIdNew != null) {
                referenciaIdNew = em.getReference(referenciaIdNew.getClass(), referenciaIdNew.getId());
                bitacoras.setReferenciaId(referenciaIdNew);
            }
            bitacoras = em.merge(bitacoras);
            if (referenciaIdOld != null && !referenciaIdOld.equals(referenciaIdNew)) {
                referenciaIdOld.getBitacorasCollection().remove(bitacoras);
                referenciaIdOld = em.merge(referenciaIdOld);
            }
            if (referenciaIdNew != null && !referenciaIdNew.equals(referenciaIdOld)) {
                referenciaIdNew.getBitacorasCollection().add(bitacoras);
                em.merge(referenciaIdNew);
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = bitacoras.getId();
                if (findBitacoras(id) == null) {
                    throw new NonexistentEntityException(
                            "The bitacoras with id " + id + valNoLonger);
                }
            }
            throw ex;
        }
    }

    public void destroy(BigDecimal id)
            throws NonexistentEntityException, RollbackFailureException {
        try {
            Bitacoras bitacoras;
            bitacoras = getBitacoras(id);
            Referencias referenciaId = bitacoras.getReferenciaId();
            if (referenciaId != null) {
                referenciaId.getBitacorasCollection().remove(bitacoras);
                em.merge(referenciaId);
            }
            em.remove(bitacoras);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Bitacoras getBitacoras(BigDecimal id)
            throws NonexistentEntityException {
        Bitacoras bitacoras;
        try {
            bitacoras = em.getReference(Bitacoras.class, id);
            bitacoras.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException(
                    "The bitacoras with id " + id + valNoLonger,
                    enfe);
        }
        return bitacoras;
    }

    public List<Bitacoras> findBitacorasEntities() {
        return findBitacorasEntities(true, -1, -1);
    }

    public List<Bitacoras> findBitacorasEntities(
            int maxResults,
            int firstResult) {
        return findBitacorasEntities(false, maxResults, firstResult);
    }

    private List<Bitacoras> findBitacorasEntities(
            boolean all,
            int maxResults,
            int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Bitacoras.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Bitacoras findBitacoras(BigDecimal id) {
        return em.find(Bitacoras.class, id);
    }

    public int getBitacorasCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Bitacoras> rt = cq.from(Bitacoras.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Bitacoras findBitacorasLastFicha(Referencias ref) {
        Bitacoras resp;

        String sqlString = Resources.sqlFechaFicha();
        Query q = em.createNativeQuery(sqlString, Bitacoras.class);
        q.setParameter(1, ref.getId());

        try {
            resp = (Bitacoras) q.getSingleResult();
        } catch (Exception e) {
            LOGGER.info(e);
            resp = null;
        }

        return resp;
    }
}
