/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.services;

import cl.cnsv.referidosrrvv.clases.ActualizarDatosReferencias;
import cl.cnsv.referidosrrvv.clases.ActualizarDatosReferidos;
import cl.cnsv.referidosrrvv.clases.ActualizarReferidoReferenciaBitacoraNo;
import cl.cnsv.referidosrrvv.clases.ActualizarReferidoReferenciaBitacoraSi;
import cl.cnsv.referidosrrvv.clases.CrearReferidoReferenciaBitacora;
import cl.cnsv.referidosrrvv.clases.CrearSucursales;
import cl.cnsv.referidosrrvv.clases.CrearSucursalesEjecutivos;
import cl.cnsv.referidosrrvv.clases.DerivarMasivo;
import cl.cnsv.referidosrrvv.clases.DerivarMasivo2;
import cl.cnsv.referidosrrvv.clases.EntidadDeCargaJs;
import cl.cnsv.referidosrrvv.clases.ExportarDataTable;
import cl.cnsv.referidosrrvv.clases.ExportarReferidosList;
import cl.cnsv.referidosrrvv.clases.ImportarRolesUsuarios;
import cl.cnsv.referidosrrvv.clases.OutActualizaProspeccion;
import cl.cnsv.referidosrrvv.clases.OutVerificaReferenciaActiva;
import cl.cnsv.referidosrrvv.clases.ReferenciasExport;
import cl.cnsv.referidosrrvv.clases.Resources;
import cl.cnsv.referidosrrvv.clases.RolesUsuariosNombre;
import cl.cnsv.referidosrrvv.clases.TotalesPanelEjecutivo;
import cl.cnsv.referidosrrvv.clases.UpProspeccionTh;
import cl.cnsv.referidosrrvv.clases.VerificarReferenciaActivaReferido;
import cl.cnsv.referidosrrvv.controller.EjecutivosJpaController;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Referidos;
import cl.cnsv.referidosrrvv.util.DataTablesParamUtility;
import cl.cnsv.referidosrrvv.util.JQueryDataTableParamModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public abstract class AbstractFacade<T> {

    private String valUndefine = "undefined";

    private static final Logger LOGGER = Logger.getLogger(
            AbstractFacade.class.getName());

    private Class<T> entityClass;

    @PersistenceContext(unitName = "com.cox_referidos_war_1.0PU")
    private transient EntityManager em;

    // @Resource
    private UserTransaction utx;
    private String valWhere = " where ";
    private String valAnd = " and ";

    private String valActualizando = "Actualizando prospeccion por servicio hilo ";
    private CharSequence sucParam;
    private String varname1;
    private String varname2;
    private String varname3;
    private String varname4;
    private String varname5;
    private String varname6;
    private String varname7;
    private String varname8;
    private String i = "";

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        try {
            getEntityManager().persist(entity);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }
    }

    public void edit(T entity) throws Exception {
        try {
            getEntityManager().merge(entity);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        } finally {
            if (entity instanceof Referidos) {
                List<Referidos> entityList = new ArrayList<>();
                entityList.add((Referidos) entity);
                Thread t = new Thread(
                        new UpProspeccionTh(
                                entityList,
                                "Actualizando prospeccion por cambio en la ficha ",
                                true));
                t.start();
            }
        }
    }

    public void remove(T entity) {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                .getCriteriaBuilder()
                .createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findAllNotCerrado(String id, String tipo) {
        String varname1 = Resources.sqlFindAllNotCerrado(tipo);

        Query cnq = getEntityManager().createNativeQuery(varname1, entityClass);
        cnq.setParameter(1, id);
        List<T> result = cnq.getResultList();

        return result;
    }
	
    public List<T> findAllNotCerrado2(String tipo) {
        String varname1 = Resources.sqlFindAllNotCerrado(tipo);

        Query cnq = getEntityManager().createNativeQuery(varname1, entityClass);
        //cnq.setParameter(1, id);
        List<T> result = cnq.getResultList();

        return result;
    }

    public List<T> findByReagendada(String id, String tipo) {
        Query cnq = getEntityManager()
                .createNativeQuery(Resources.sqlFindByReagendada(tipo), entityClass);

        cnq.setParameter(1, id);
        List<T> result = cnq.getResultList();
        return result;
    }

    public List<T> findByReferencia(int id) {
        Query cnq = getEntityManager()
                .createNativeQuery(Resources.sqlFindByReferencia(), entityClass);
        cnq.setParameter(1, id);
        cnq.setParameter(2, id);
        List<T> result = cnq.getResultList();
        return result;
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                .getCriteriaBuilder()
                .createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                .getCriteriaBuilder()
                .createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public TotalesPanelEjecutivo getTotalesPanel(
            String suc,
            String sup,
            String anio,
            String mes) {
        String varname1 = Resources.sqlPanelEjecutivo();
        String varname2, varname3, varname4, varname5, varname6 = null, varname7 = null;

        // insertando condicion supervisor
        String cOwnere = " ownere in (" + sup + ") ";
        String valorSup1 = (valUndefine.equals(sup)) ? "" : valWhere + cOwnere;
        String valorSup2 = (valUndefine.equals(sup)) ? "" : valAnd + cOwnere;
        varname2 = varname1.replace("?1", valorSup1);
        varname3 = varname2.replace("?2", valorSup2);

        // insertando condicion para mes y año
        String valorAnioMes, valorAnioMes2;
        String cExtract = " EXTRACT(year FROM FECHA) = "
                + anio
                + ((!valUndefine.equals(mes))
                        ? " and EXTRACT(month FROM FECHA) = " + mes
                        : "");
        if (!valUndefine.equals(sup)) {
            valorAnioMes = (valUndefine.equals(anio)) ? "" : valAnd + cExtract;
        } else {
            valorAnioMes = (valUndefine.equals(anio)) ? "" : valWhere + cExtract;
        }
        valorAnioMes2 = (valUndefine.equals(anio)) ? "" : valAnd + cExtract;
        varname4 = varname3.replace("?3", valorAnioMes);
        varname5 = varname4.replace("?4", valorAnioMes2);

        // insertando condicion para sucursal ejecutivo
        String valorSuc = null, valorSuc2 = null;
        String cSentencia = " s.id=" + suc;

        TotalesPanelEjecutivo result = continuar(
                sup,
                anio,
                valorSuc,
                suc,
                cSentencia,
                valorSuc2,
                varname6,
                varname5,
                varname7);

        return result;
    }

    public List<T> findRut(String id) {
        Query cnq = getEntityManager()
                .createNamedQuery("Referidos.findByRut", entityClass);
        cnq.setParameter("rut", id);
        List<T> result = cnq.getResultList();
        return result;
    }

    public List<T> findIdpili(int id) {
        Query cnq = getEntityManager()
                .createNamedQuery("Referidos.findById3", entityClass);
        cnq.setParameter("id3", id);
        List<T> result = cnq.getResultList();
        return result;
    }

    public List<T> findAllNotCerradoFull() {
        String varname1 = Resources.sqlFindAllNotCerradoFull();

        Query cnq = getEntityManager().createNativeQuery(varname1, entityClass);
        List<T> result = cnq.getResultList();
        return result;
    }

    public List<T> findByReferido(int id) {
        String varname1 = Resources.sqlFindByReferido();

        Query cnq = getEntityManager().createNativeQuery(varname1, entityClass);
        cnq.setParameter(1, id);
        List<T> result = cnq.getResultList();
        return result;
    }

    public List<T> findByReagendadaFull() {
        Query cnq = getEntityManager()
                .createNativeQuery(Resources.sqlFindByReagendadaFull(), entityClass);

        List<T> result = cnq.getResultList();

        return result;
    }

    public void actualizarReferidosSi(
            EntidadDeCargaJs entity,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {
        try {
            ActualizarReferidoReferenciaBitacoraSi arrb = new ActualizarReferidoReferenciaBitacoraSi();
            arrb.actualizar9(entity, utx, em);
        } catch (NonexistentEntityException | RollbackFailureException e) {
            LOGGER.info(e);
            throw e;
        }
    }

    public void actualizarReferidosNo(
            EntidadDeCargaJs entity,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {
        try {
            ActualizarReferidoReferenciaBitacoraNo arrb = new ActualizarReferidoReferenciaBitacoraNo();
            arrb.actualizar8(entity, utx, em);
        } catch (NonexistentEntityException | RollbackFailureException e) {
            LOGGER.info(e);
            throw e;
        }
    }

    public List<RolesUsuariosNombre> findNomnbre(String id) {
        String varname = Resources.sqlRolesUsuariosNombre();

        Query cnq = getEntityManager()
                .createNativeQuery(varname, RolesUsuariosNombre.class);
        cnq.setParameter(1, id);
        cnq.setParameter(2, id);
        cnq.setParameter(3, id);
        cnq.setParameter(4, id);
        cnq.setParameter(5, id);

        List<RolesUsuariosNombre> result = cnq.getResultList();

        return result;
    }

    public List<T> exportarReferidos(
            UserTransaction utx,
            EntityManager em,
            String usuario) throws RollbackFailureException, Exception {
        ExportarReferidosList listExport = new ExportarReferidosList();
        return (List<T>) listExport.exportar(utx, em, usuario);
    }

    public List<CrearSucursales.ErrorCrearSucursalesOut> importarSucursales(
            List<EntidadDeCargaJs> entity,
            UserTransaction utx,
            EntityManager em) throws Exception {
        List<CrearSucursales.ErrorCrearSucursalesOut> resp;
        try {
            CrearSucursales cse = new CrearSucursales();
            resp = cse.cargar(entity, utx, em);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }
        return resp;
    }

    public List<CrearSucursalesEjecutivos.ErrorCrearEjecutivosOut> importarSucursalesEjecuvitos(
            List<EntidadDeCargaJs> entity,
            UserTransaction utx,
            EntityManager em) throws Exception {
        List<CrearSucursalesEjecutivos.ErrorCrearEjecutivosOut> resp;
        try {
            CrearSucursalesEjecutivos cse = new CrearSucursalesEjecutivos();
            resp = cse.cargar(entity, utx, em);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }
        return resp;
    }

    public List<CrearReferidoReferenciaBitacora.ErrorCrearReferidoReferenciaBitacoraOut> importarReferidos(
            List<EntidadDeCargaJs> entity,
            UserTransaction utx,
            EntityManager em) throws Exception {
        List<CrearReferidoReferenciaBitacora.ErrorCrearReferidoReferenciaBitacoraOut> resp;

        try {
            CrearReferidoReferenciaBitacora crrb = new CrearReferidoReferenciaBitacora();
            resp = crrb.cargar(entity, utx, em);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        } finally {
            List<Referidos> entityList = Resources.listaParaProspeccionConHilo;
            Thread t = new Thread(
                    new UpProspeccionTh(
                            entityList,
                            "Actualizando prospeccion por carga de archivo o creacion de ficha  ",
                            true));
            t.start();
        }

        return resp;
    }

    public void importarRolesUsuarios(
            EntidadDeCargaJs entity,
            UserTransaction utx,
            EntityManager em) throws Exception {
        try {
            ImportarRolesUsuarios iru = new ImportarRolesUsuarios();
            iru.cargar(entity, utx, em);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }
    }

    public void derivarMasivo(
            String suc,
            String anio,
            String mes,
            String sup,
            String usr,
            String id,
            String eje,
            UserTransaction utx,
            EntityManager em) throws Exception {
        try {
            DerivarMasivo dm = new DerivarMasivo();
            dm.derivar(suc, anio, mes, sup, usr, id, eje, utx, em);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }
    }

    public String findAllNotCerradoFullAjax3(
            HttpServletRequest req,
            EntityManager em) throws IOException {
        String respuesta = null;
        EjecutivosJpaController ejeCtrl = new EjecutivosJpaController(
                null,
                null,
                em);

        try {
            JQueryDataTableParamModel param = DataTablesParamUtility.getParam(req);

            // System.out.println("***** pppp *********");
            // System.out.println(param.toString());
            // System.out.println("*********************");
            String varname = Resources.sqlCargarDataTable();

            int desde = param.iDisplayStart;
            int hasta = param.iDisplayStart + param.iDisplayLength;

            String varname0 = varname;

            continuarIF2(param, varname0, varname);

            // ordena por fecha por default
            if (param.iSortColumnIndex == 0) {
                varname0 = varname.replace("?4", "fecha");
            }

            param.sSearch = param.sSearch.replace(" ", "%");

            varname1 = varname0.replace("?1", param.sSearch.toLowerCase());
            varname2 = varname1.replace("?2", String.valueOf(hasta));
            varname3 = varname2.replace("?3", String.valueOf(desde));
            varname4 = " ";
            varname5 = " ";
            varname6 = " ";
            varname7 = " ";
            varname8 = " ";

            continuarIf3(param);

            varname8 = varname7.replace("?9", sucParam);

            javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                    .getCriteriaBuilder()
                    .createQuery();
            javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
            cq.select(getEntityManager().getCriteriaBuilder().count(rt));
            javax.persistence.Query q = getEntityManager().createQuery(cq);

            int iTotalRecords = ((Long) q.getSingleResult()).intValue();

            // varname8 = "SELECT t6.* FROM (SELECT t5.* FROM (SELECT t3.*, rownum n FROM
            // (SELECT t.*, rownum rn FROM (SELECT r.*, r2.nombre, r2.rut, r2.correo,
            // r2.comuna, r2.region, s.nombre sucursal, a.nombre AS nomestado FROM
            // RFD_REFERENCIAS r INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID)
            // INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID) left join rfd_ejecutivos e
            // on (e.codigo=r.ownere) left join rfd_sucursales s on (s.id=e.sucursal_id)) t)
            // t3 INNER JOIN ( (SELECT t2.rn, t2.id FROM (SELECT t.*, rownum rn FROM (SELECT
            // r.id, OWNERE, FECHA, e.sucursal_id,
            // concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(r2.NOMBRE,'
            // '),r2.APELLIDO),' '),r2.RUT),' '),to_char(r.FECHA,'Dy, dd mon YYYY')),'
            // '),r2.TELEFONOS), ' '),r2.CORREO),' '), r.CANALNAME),' '),r2.COMUNA), ' '),
            // r2.REGION),' '), r.OWNERENAME), ' '),a.NOMBRE), ' '), s.nombre) campos FROM
            // RFD_REFERENCIAS r INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID) left
            // join rfd_ejecutivos e on (e.codigo=r.ownere) left join rfd_sucursales s on
            // (s.id=e.sucursal_id)INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID)) t) t2
            // WHERE lower(campos) LIKE lower('%iniciado%') )) t4 ON (t3.id = t4.id) ORDER
            // BY fecha DESC) t5 WHERE n <=10 ) t6 WHERE n > 0";
            q = em.createNativeQuery(varname8, entityClass);

            List<Referencias> referencias = new LinkedList<Referencias>();

            referencias = q.getResultList();

            // System.out.println("***** rrrr *********");
            // System.out.println("ResultingJSONstring = " + jsonString);
            // System.out.println("**************");
            String draw = param.draw;
            int iTotalDisplayRecords = iTotalRecords; // value will be set when code filters companies by keyword
            JsonArray data = new JsonArray(); // data that will be shown in the table

            JsonObject jsonResponse = new JsonObject();

            jsonResponse.addProperty("draw", draw);
            jsonResponse.addProperty("recordsTotal", iTotalRecords);
            jsonResponse.addProperty("recordsFiltered", iTotalDisplayRecords);

            for (Referencias c : referencias) {
                // convert user object to json string and return it
                // System.out.println("****** ccc ********");
                // System.out.println(c.toString());
                // System.out.println("**************");

                Ejecutivos eje = ejeCtrl.findByCodEjecutiva(c.getOwnere());
                JsonArray row = new JsonArray();

                row.add(new JsonPrimitive(""));

                continuar11(row, c);

                row.add(
                        new JsonPrimitive(
                                (c.getReferidoId().getRegion() != null)
                                        ? c.getReferidoId().getRegion()
                                        : ""));

                row.add(
                        new JsonPrimitive(
                                (c.getOwnerename() != null) ? c.getOwnerename() : ""));

                row.add(
                        new JsonPrimitive(
                                (eje != null) ? eje.getSucursalId().getNombre() : ""));

                String funcionVip = "onclick=\"myPrioridad(" + c.getId().toString() + ")\"";
                String iVip = null;

                Boolean valorPriorizado = false;
                String descBotonVip = "";
                String styleColorVip = "";

                try {
                    valorPriorizado = c.getPrioritario();
                    descBotonVip = (valorPriorizado) ? "Priorizado" : "No Priorizado";
                    styleColorVip = (valorPriorizado)
                            ? "style='color: white; background: black'"
                            : "style='color: black; background: white'";
                } catch (NullPointerException e) {
                    valorPriorizado = false;
                    descBotonVip = "No Priorizado";
                    styleColorVip = "style='color: black; background: white'";
                }

                iVip = "<i class='fa fa-star'> </i> ";

                String botonVip = "<button  id='v"
                        + c.getId()
                        + "' "
                        + funcionVip
                        + " class='btn btn-success btn-outldine btn-block' type='button' "
                        + styleColorVip
                        + "> "
                        + iVip
                        + descBotonVip
                        + " </button>";

                String funcion = "onclick=\"myFunction(" + c.getId().toString() + ")\"";
                // String i = null;
                String descBoton = c.getAccionId().getNombre();

                continuarIF(c);

                String styleColor = "style='color: "
                        + c.getAccionId().getColor()
                        + "; background: "
                        + c.getAccionId().getColorb()
                        + "'";
                String boton = "<button  id='"
                        + c.getId()
                        + "' "
                        + funcion
                        + " class='btn btn-success btn-outldine btn-block' type='button' "
                        + styleColor
                        + "> "
                        + i
                        + descBoton
                        + " </button>";

                row.add(new JsonPrimitive(botonVip + boton));
                data.add(row);
                // System.out.println("*************** row");
                // System.out.println(row);
                // System.out.println("*************** row");
            }

            jsonResponse.add("data", data);

            respuesta = jsonResponse.toString();
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }

        return respuesta;
    }

    private void continuarIF2(
            JQueryDataTableParamModel param,
            String varname0,
            String varname) {
        if (param.iSortColumnIndex == 1) {
            varname0 = varname.replace("?4", "nombre");
        }
        if (param.iSortColumnIndex == 2) {
            varname0 = varname.replace("?4", "rut");
        }
        if (param.iSortColumnIndex == 3) {
            varname0 = varname.replace("?4", "correo");
        }
        if (param.iSortColumnIndex == 4) {
            varname0 = varname.replace("?4", "fecha");
        }
        if (param.iSortColumnIndex == 5) {
            varname0 = varname.replace("?4", "canalname");
        }
        if (param.iSortColumnIndex == 6) {
            varname0 = varname.replace("?4", "comuna");
        }
        if (param.iSortColumnIndex == 7) {
            varname0 = varname.replace("?4", "region");
        }
        if (param.iSortColumnIndex == 8) {
            varname0 = varname.replace("?4", "ownerename");
        }
        if (param.iSortColumnIndex == 9) {
            varname0 = varname.replace("?4", "sucursal");
        }
        if (param.iSortColumnIndex == 10) {
            varname0 = varname.replace("?4", "nomestado");
        }
    }

    public void derivarMasivoList(
            List<EntidadDeCargaJs> entity,
            UserTransaction utx,
            EntityManager em) throws Exception {
        try {
            DerivarMasivo2 dm = new DerivarMasivo2();
            dm.derivarList(entity, utx, em);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }
    }

    public void actualizarDatosReferidos(
            List<EntidadDeCargaJs> entity,
            UserTransaction utx,
            EntityManager em) throws Exception {
        try {
            ActualizarDatosReferidos adr = new ActualizarDatosReferidos();
            adr.actualizar7(entity, utx, em);
        } catch (NonexistentEntityException | RollbackFailureException e) {
            LOGGER.info(e);
            throw e;
        }
    }

    public List<ReferenciasExport> exportarDataTable(
            String suc,
            String anio,
            String mes,
            String sup,
            String id,
            UserTransaction utx,
            EntityManager em) throws Exception {
        List<ReferenciasExport> result;
        try {
            ExportarDataTable ed = new ExportarDataTable();
            result = ed.exportar(suc, anio, mes, sup, id, utx, em);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }

        return result;
    }

    public List<ActualizarDatosReferencias.ErrorActualizaDatosReferenciasOut> actualizarDatosReferencias(
            List<EntidadDeCargaJs> entity,
            UserTransaction utx,
            EntityManager em) throws Exception {
        List<ActualizarDatosReferencias.ErrorActualizaDatosReferenciasOut> resp;
        try {
            ActualizarDatosReferencias adr = new ActualizarDatosReferencias();
            resp = adr.actualizar6(entity, utx, em);
        } catch (NonexistentEntityException | RollbackFailureException e) {
            LOGGER.info(e);
            throw e;
        }
        return resp;
    }

    public List<Referencias> getCanales(EntityManager em) {
        String varname = Resources.sqlCanales();

        Query q = em.createNativeQuery(varname);
        List<Referencias> aReferencias = new ArrayList<>();

        List result = q.getResultList();

        for (Object o : result) {
            Referencias r = new Referencias();
            r.setCanalname((o != null) ? o.toString() : "");
            aReferencias.add(r);
        }

        return aReferencias;
    }

    OutVerificaReferenciaActiva verificarReferencia(
            String rut,
            UserTransaction utx,
            EntityManager em) {
        OutVerificaReferenciaActiva resp;

        try {
            VerificarReferenciaActivaReferido vrar = new VerificarReferenciaActivaReferido();
            resp = vrar.verificar(rut, utx, em);
        } catch (Exception e) {
            LOGGER.info(e);
            throw e;
        }

        return resp;
    }

    public OutActualizaProspeccion actualizarProspeccionListTh(
            List<Referidos> entityList) {
        OutActualizaProspeccion oap = new OutActualizaProspeccion();
        boolean pasaronDatos = true;

        if (entityList.isEmpty()) {
            pasaronDatos = false;
            Query Sql = em.createNativeQuery(
                    Resources.sqlUpdateProspeccion(),
                    Referidos.class);
            entityList = Sql.getResultList();
        }

        Thread t1 = new Thread(
                new UpProspeccionTh(entityList, valActualizando, pasaronDatos));
        t1.start();

        oap.setCodigo(0);
        oap.setMensaje("Ok");
        oap.setMensajeEx("Servicio en proceso, consulte log para ver el progreso.");

        return oap;
    }

    public OutActualizaProspeccion actualizarProspeccionListTh2(
            List<Referidos> entityList) {
        OutActualizaProspeccion oap = new OutActualizaProspeccion();
        boolean pasaronDatos = true;

        List<Referidos> entityList1 = new ArrayList<>();
        List<Referidos> entityList2 = new ArrayList<>();
        List<Referidos> entityList3 = new ArrayList<>();
        List<Referidos> entityList4 = new ArrayList<>();
        List<Referidos> entityList5 = new ArrayList<>();
        List<Referidos> entityList6 = new ArrayList<>();
        List<Referidos> entityList7 = new ArrayList<>();
        List<Referidos> entityList8 = new ArrayList<>();
        List<Referidos> entityList9 = new ArrayList<>();
        List<Referidos> entityList10 = new ArrayList<>();

        if (entityList.isEmpty()) {
            pasaronDatos = false;
            Query Sql = em.createNativeQuery(
                    Resources.sqlUpdateProspeccion(),
                    Referidos.class);
            entityList = Sql.getResultList();
        }
        int partes = entityList.size() / 10;

        int x3 = continuarFor(
                entityList,
                entityList1,
                entityList2,
                entityList3,
                partes);

        int x4 = 0, x4c = 0;
        for (Referidos o : entityList) {
            x4++;
            if (x4 >= x3) {
                x4c++;
                entityList4.add(o);
                if (x4c >= partes) {
                    break;
                }
            }
        }

        int x5 = 0, x5c = 0;
        for (Referidos o : entityList) {
            x5++;
            if (x5 >= x4) {
                x5c++;
                entityList5.add(o);
                if (x5c >= partes) {
                    break;
                }
            }
        }

        continuar15(
                entityList,
                x5,
                entityList6,
                partes,
                entityList7,
                entityList8,
                entityList9,
                entityList10);

        Thread t1 = new Thread(
                new UpProspeccionTh(entityList1, valActualizando, pasaronDatos));
        t1.start();

        Thread t2 = new Thread(
                new UpProspeccionTh(entityList2, valActualizando, pasaronDatos));
        t2.start();

        Thread t3 = new Thread(
                new UpProspeccionTh(entityList3, valActualizando, pasaronDatos));
        t3.start();

        Thread t4 = new Thread(
                new UpProspeccionTh(entityList4, valActualizando, pasaronDatos));
        t4.start();

        Thread t5 = new Thread(
                new UpProspeccionTh(entityList5, valActualizando, pasaronDatos));
        t5.start();

        Thread t6 = new Thread(
                new UpProspeccionTh(entityList6, valActualizando, pasaronDatos));
        t6.start();

        Thread t7 = new Thread(
                new UpProspeccionTh(entityList7, valActualizando, pasaronDatos));
        t7.start();

        Thread t8 = new Thread(
                new UpProspeccionTh(entityList8, valActualizando, pasaronDatos));
        t8.start();

        Thread t9 = new Thread(
                new UpProspeccionTh(entityList9, valActualizando, pasaronDatos));
        t9.start();

        Thread t10 = new Thread(
                new UpProspeccionTh(entityList10, valActualizando, pasaronDatos));
        t10.start();

        oap.setCodigo(0);
        oap.setMensaje("Ok");
        oap.setMensajeEx("Servicio en proceso, consulte log para ver el progreso.");

        return oap;
    }

    private TotalesPanelEjecutivo continuar(
            String sup,
            String anio,
            String valorSuc,
            String suc,
            String cSentencia,
            String valorSuc2,
            String varname6,
            String varname5,
            String varname7) {
        if (!valUndefine.equals(sup) || !valUndefine.equals(anio)) {
            valorSuc = (valUndefine.equals(suc)) ? "" : valAnd + cSentencia;
        } else {
            valorSuc = (valUndefine.equals(suc)) ? "" : valWhere + cSentencia;
        }
        valorSuc2 = (valUndefine.equals(suc)) ? "" : valAnd + cSentencia;
        varname6 = varname5.replace("?5", valorSuc);
        varname7 = varname6.replace("?6", valorSuc2);

        Query cnq = getEntityManager()
                .createNativeQuery(varname7, TotalesPanelEjecutivo.class);
        TotalesPanelEjecutivo result = (TotalesPanelEjecutivo) cnq.getSingleResult();

        return result;
    }

    private void continuar10(
            JQueryDataTableParamModel param,
            JQueryDataTableParamModel param2) {
        // condicion para anio
        String anioParam;
        if (!valUndefine.equals(param.anio)) {
            anioParam = " and EXTRACT(year FROM FECHA) = " + param.anio;
        } else {
            anioParam = "";
        }
        varname6 = varname5.replace("?7", anioParam);

        // condicion para mes
        String mesParam;
        if (!valUndefine.equals(param.mes)) {
            mesParam = " and EXTRACT(month FROM FECHA) = " + param.mes;
        } else {
            mesParam = "";
        }
        varname7 = varname6.replace("?8", mesParam);
    }

    private void continuar11(JsonArray row, Referencias c) {
        row.add(
                new JsonPrimitive(
                        c.getReferidoId().getNombre()
                                + " "
                                + ((c.getReferidoId().getApellido() == null)
                                        ? ""
                                        : c.getReferidoId().getApellido())));

        row.add(new JsonPrimitive(c.getReferidoId().getRut()));

        row.add(
                new JsonPrimitive(
                        (c.getReferidoId().getCorreo() != null)
                                ? c.getReferidoId().getCorreo()
                                : ""));

        row.add(
                new JsonPrimitive(
                        new SimpleDateFormat("EEE, d MMM yyyy").format(c.getFecha())));

        row.add(
                new JsonPrimitive((c.getCanalname() != null) ? c.getCanalname() : ""));

        row.add(
                new JsonPrimitive(
                        (c.getReferidoId().getComuna() != null)
                                ? c.getReferidoId().getComuna()
                                : ""));
    }

    private void continuarIF(Referencias c) {
        if ("1".equals(c.getAccionId().getId().toString())) {
            i = "<i class='fa fa-ellipsis-h'> </i> ";
        }
        if ("2".equals(c.getAccionId().getId().toString())) {
            i = "<i class='fa fa-thumbs-up'> </i> ";
        }
        if ("3".equals(c.getAccionId().getId().toString())) {
            i = "<i class='fa fa-clock-o'> </i> ";
        }
        if ("5".equals(c.getAccionId().getId().toString())) {
            i = "<i class='fa fa-headphones'> </i> ";
        }
        if ("6".equals(c.getAccionId().getId().toString())) {
            i = "<i class='fa fa-thumbs-down'> </i> ";
        }
        if ("7".equals(c.getAccionId().getId().toString())) {
            i = "<i class='fa fa-check-square-o'> </i> ";
        }
        if ("8".equals(c.getAccionId().getId().toString())) {
            i = "<i class='fa fa-handshake-o'> </i> ";
        }
        if ("9".equals(c.getAccionId().getId().toString())) {
            i = "<i class='fa fa-gears (alias)'> </i> ";
        }
    }

    private void continuar15(
            List<Referidos> entityList,
            int x5,
            List<Referidos> entityList6,
            int partes,
            List<Referidos> entityList7,
            List<Referidos> entityList8,
            List<Referidos> entityList9,
            List<Referidos> entityList10) {
        int x6 = 0, x6c = 0;
        for (Referidos o : entityList) {
            x6++;
            if (x6 >= x5) {
                x6c++;
                entityList6.add(o);
                if (x6c >= partes) {
                    break;
                }
            }
        }

        int x7 = 0, x7c = 0;
        for (Referidos o : entityList) {
            x7++;
            if (x7 >= x6) {
                x7c++;
                entityList7.add(o);
                if (x7c >= partes) {
                    break;
                }
            }
        }

        continuar16(entityList, x7, entityList8, partes, entityList9, entityList10);
    }

    private void continuar16(
            List<Referidos> entityList,
            int x7,
            List<Referidos> entityList8,
            int partes,
            List<Referidos> entityList9,
            List<Referidos> entityList10) {
        int x8 = 0, x8c = 0;
        for (Referidos o : entityList) {
            x8++;
            if (x8 >= x7) {
                x8c++;
                entityList8.add(o);
                if (x8c >= partes) {
                    break;
                }
            }
        }

        int x9 = 0, x9c = 0;
        for (Referidos o : entityList) {
            x9++;
            if (x9 >= x8) {
                x9c++;
                entityList9.add(o);
                if (x9c >= partes) {
                    break;
                }
            }
        }

        int x10 = 0;
        for (Referidos o : entityList) {
            x10++;
            if (x10 >= x9) {
                entityList10.add(o);
            }
        }
    }

    private void continuarIf3(
            JQueryDataTableParamModel param) {
        // ordena DESC por default
        if (param.iSortColumnIndex != 0) {
            varname4 = varname3.replace("?5", param.sSortDirection);
        } else {
            varname4 = varname3.replace("?5", "DESC");
        }

        // condicion para supervisores
        String supParam;
        if (!valUndefine.equals(param.sup)) {
            supParam = " AND OWNERE IN (" + param.sup + ")";
        } else {
            supParam = "";
        }
        varname5 = varname4.replace("?6", supParam);

        continuar10(param, param);

        // condicion para mes
        // String sucParam;
        if (!valUndefine.equals(param.suc)) {
            sucParam = " and sucursal_id=" + param.suc;
        } else {
            sucParam = "";
        }
    }

    private int continuarFor(
            List<Referidos> entityList,
            List<Referidos> entityList1,
            List<Referidos> entityList2,
            List<Referidos> entityList3,
            int partes) {
        int x1 = 0;
        for (Referidos o : entityList) {
            x1++;
            entityList1.add(o);
            if (x1 >= partes) {
                break;
            }
        }

        int x2 = 0, x2c = 0;
        for (Referidos o : entityList) {
            x2++;
            if (x2 >= x1) {
                x2c++;
                entityList2.add(o);
                if (x2c >= partes) {
                    break;
                }
            }
        }

        int x3 = 0, x3c = 0;
        for (Referidos o : entityList) {
            x3++;
            if (x3 >= x2) {
                x3c++;
                entityList3.add(o);
                if (x3c >= partes) {
                    break;
                }
            }
        }
        return x3;
    }
}
