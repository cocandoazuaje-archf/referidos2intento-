/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.services;

import cl.cnsv.referidosrrvv.controller.EjecutivosJpaController;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author cow
 */
@Stateless
@Path("cl.cnsv.referidosrrvv.models.ejecutivos")
public class EjecutivosFacadeREST extends AbstractFacade<Ejecutivos> {

  private UserTransaction utx;

  @PersistenceContext(unitName = "com.cox_referidos_war_1.0PU")
  private transient EntityManager em;

  public EjecutivosFacadeREST() {
    super(Ejecutivos.class);
  }

  @POST
  @Override
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void create(Ejecutivos entity) {
    super.create(entity);
  }

  @PUT
  @Path("{id}")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void edit(@PathParam("id") BigDecimal id, Ejecutivos entity)
      throws Exception {
    super.edit(entity);
  }

  @DELETE
  @Path("{id}")
  public void remove(@PathParam("id") BigDecimal id) {
    super.remove(super.find(id));
  }

  @DELETE
  @Path("/deletePorCodigo/{codigo}")
  public void removeByCodigo(@PathParam("codigo") String codigo) {
    EjecutivosJpaController ec = new EjecutivosJpaController(
        utx,
        null,
        getEntityManager());
    Ejecutivos eje = ec.findByCodEjecutiva(codigo);
    super.remove(super.find(eje.getId()));
  }

  @GET
  @Path("{id}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Ejecutivos find(@PathParam("id") BigDecimal id) {
    return super.find(id);
  }

  @GET
  @Override
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Ejecutivos> findAll() {
    return super.findAll();
  }

  @GET
  @Path("{from}/{to}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Ejecutivos> findRange(
      @PathParam("from") Integer from,
      @PathParam("to") Integer to) {
    return super.findRange(new int[] { from, to });
  }

  @GET
  @Path("count")
  @Produces(MediaType.TEXT_PLAIN)
  public String countREST() {
    return String.valueOf(super.count());
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  protected UserTransaction getUtx() {
    return utx;
  }
}
