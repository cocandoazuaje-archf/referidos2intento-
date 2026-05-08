/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.services;

import cl.cnsv.referidosrrvv.clases.CrearReferidoReferenciaBitacora;
import cl.cnsv.referidosrrvv.clases.CrearSucursalesEjecutivos;
import cl.cnsv.referidosrrvv.clases.EntidadDeCargaJs;
import cl.cnsv.referidosrrvv.clases.OutActualizaProspeccion;
import cl.cnsv.referidosrrvv.clases.OutVerificaReferenciaActiva;
import cl.cnsv.referidosrrvv.models.Referidos;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
@Stateless
@Path("cl.cnsv.referidosrrvv.models.referidos")
public class ReferidosFacadeREST extends AbstractFacade<Referidos> {

  private static final Logger LOGGER = Logger.getLogger(
      ReferidosFacadeREST.class);

  @PersistenceContext(unitName = "com.cox_referidos_war_1.0PU")
  private transient EntityManager em;

  // @Resource
  private UserTransaction utx;

  public ReferidosFacadeREST() {
    super(Referidos.class);
  }

  @POST
  @Override
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void create(Referidos entity) {
    super.create(entity);
  }

  @POST
  @Path("/importarsucursalesejecutivos")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<CrearSucursalesEjecutivos.ErrorCrearEjecutivosOut> importarSucursalesEjecuvitos(
      List<EntidadDeCargaJs> entity) throws Exception {
    return super.importarSucursalesEjecuvitos(
        entity,
        getUtx(),
        getEntityManager());
  }

  @POST
  @Path("/importarreferidos")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<CrearReferidoReferenciaBitacora.ErrorCrearReferidoReferenciaBitacoraOut> importarReferidos(
      List<EntidadDeCargaJs> entity) throws Exception {
    return super.importarReferidos(entity, getUtx(), getEntityManager());
  }

  @GET
  @Path("/actualizaProspeccion")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public OutActualizaProspeccion ActualizaProspeccion() {
    List<Referidos> entity = new ArrayList<>();
    return super.actualizarProspeccionListTh2(entity);
  }

  @GET
  @Path("/verificaReferenciaActiva/{rut}")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public OutVerificaReferenciaActiva verificaReferenciaActiva(
      @PathParam("rut") String rut) throws Exception {
    return super.verificarReferencia(rut, getUtx(), getEntityManager());
  }

  @POST
  @Path("/actualizarreferidosi")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void actualizarReferidosSi(EntidadDeCargaJs entity) throws Exception {
    super.actualizarReferidosSi(entity, getUtx(), getEntityManager());
  }

  @POST
  @Path("/actualizardatosreferidos")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void actualizarDatosReferidos(List<EntidadDeCargaJs> entity)
      throws Exception {
    super.actualizarDatosReferidos(entity, getUtx(), getEntityManager());
  }

  @POST
  @Path("/actualizarreferidono")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void actualizarReferidosNo(EntidadDeCargaJs entity) throws Exception {
    super.actualizarReferidosNo(entity, getUtx(), getEntityManager());
  }

  @PUT
  @Path("{id}")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void edit(@PathParam("id") BigDecimal id, Referidos entity)
      throws Exception {
    super.edit(entity);
  }

  @DELETE
  @Path("{id}")
  public void remove(@PathParam("id") BigDecimal id) {
    super.remove(super.find(id));
  }

  @GET
  @Path("/idpili/{id}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referidos> find(@PathParam("id") int id) {
    return super.findIdpili(id);
  }

  @GET
  @Path("{id}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Referidos find(@PathParam("id") BigDecimal id) {
    return super.find(id);
  }

  @GET
  @Path("/rut/{id}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referidos> find(@PathParam("id") String id) {
    return super.findRut(id);
  }

  @GET
  @Override
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referidos> findAll() {
    return super.findAll();
  }

  @GET
  @Path("{from}/{to}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referidos> findRange(
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
