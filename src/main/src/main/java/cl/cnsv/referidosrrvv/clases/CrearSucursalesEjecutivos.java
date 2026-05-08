/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.EjecutivosJpaController;
import cl.cnsv.referidosrrvv.controller.SucursalesJpaController;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Sucursales;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author uvv
 */
public class CrearSucursalesEjecutivos {

    private static final Logger LOGGER = Logger.getLogger(
            CrearSucursalesEjecutivos.class.getName());

    public CrearSucursalesEjecutivos() {
        // constructor vacio
    }

    public List<ErrorCrearEjecutivosOut> cargar(
            List<EntidadDeCargaJs> rjsList,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {
        SucursalesJpaController sc = new SucursalesJpaController(utx, null, em);
        EjecutivosJpaController ec = new EjecutivosJpaController(utx, null, em);
        List<ErrorCrearEjecutivosOut> ecreList = new ArrayList<>();
        Sucursales s = new Sucursales();
        Ejecutivos e = null;

        for (EntidadDeCargaJs rjs : rjsList) {
            try {
                s = sc.findSucursales(new BigDecimal(rjs.getSUCURSAL()));
            } catch (Exception ex) {
                LOGGER.info(ex);
            }

            if (rjs.getEJECUTIVO() != null && !rjs.getEJECUTIVO().isEmpty()) {
                continuar(e, rjs);
                try {
                    if (s == null) {
                        ErrorCrearEjecutivosOut ecre = new ErrorCrearEjecutivosOut();
                        ecre.setCODEJECUTIVO(rjs.getCODEJECUTIVO());
                        ecre.setEJECUTIVO(rjs.getEJECUTIVO());
                        ecre.setIdSucursales(s);
                        ecre.setCORREO(rjs.getCORREO());
                        ecre.setTELEFONO(rjs.getTELEFONO());
                        ecre.setERROR(
                                "ERROR -> La sucursal " + rjs.getSUCURSAL() + " no existe.");
                        ecreList.add(ecre);
                        continue;
                    }

                    // BUSCA CODIGO EJECUTIVO
                    e = ec.findByCodEjecutiva(rjs.getCODEJECUTIVO());
                    e.setNombre(rjs.getEJECUTIVO().trim().toLowerCase());
                    e.setSucursalId(s);
                    e.setCorreo(rjs.getCORREO());
                    e.setTelefono(rjs.getTELEFONO());
                    ec.edit(e);
                } catch (Exception ex) {
                    e = new Ejecutivos();
                    LOGGER.info(ex);
                    e.setCodigo(rjs.getCODEJECUTIVO().trim().toLowerCase());
                    e.setNombre(rjs.getEJECUTIVO().trim());
                    e.setSucursalId(s);
                    e.setCorreo(rjs.getCORREO());
                    e.setTelefono(rjs.getTELEFONO());
                    e.setVersion(BigInteger.ONE);
                    e.setReferenciasCollection(new ArrayList<Referencias>());
                    ec.create(e);
                }
            }
        }
        return ecreList;
    }

    private void continuar(Ejecutivos e, EntidadDeCargaJs rjs) {
        e = new Ejecutivos();
        if (rjs.getCODEJECUTIVO() != null && !rjs.getCODEJECUTIVO().isEmpty()) {
            rjs.setCODEJECUTIVO(rjs.getCODEJECUTIVO().toLowerCase());
            e.setCodigo(rjs.getCODEJECUTIVO());
        } else {
            UUID idOne = UUID.randomUUID();
            e.setCodigo(idOne.toString());
        }
    }

    public static class ErrorCrearEjecutivosOut {

        String CODEJECUTIVO;
        String EJECUTIVO;
        Sucursales idSucursales;
        String CORREO;
        String TELEFONO;
        String ERROR;

        /**
         * @return the CODEJECUTIVO
         */
        public String getCODEJECUTIVO() {
            return CODEJECUTIVO;
        }

        /**
         * @param CODEJECUTIVO the CODEJECUTIVO to set
         */
        public void setCODEJECUTIVO(String CODEJECUTIVO) {
            this.CODEJECUTIVO = CODEJECUTIVO;
        }

        /**
         * @return the EJECUTIVO
         */
        public String getEJECUTIVO() {
            return EJECUTIVO;
        }

        /**
         * @param EJECUTIVO the EJECUTIVO to set
         */
        public void setEJECUTIVO(String EJECUTIVO) {
            this.EJECUTIVO = EJECUTIVO;
        }

        /**
         * @return the idSucursales
         */
        public Sucursales getIdSucursales() {
            return idSucursales;
        }

        /**
         * @param idSucursales the idSucursales to set
         */
        public void setIdSucursales(Sucursales idSucursales) {
            this.idSucursales = idSucursales;
        }

        /**
         * @return the CORREO
         */
        public String getCORREO() {
            return CORREO;
        }

        /**
         * @param CORREO the CORREO to set
         */
        public void setCORREO(String CORREO) {
            this.CORREO = CORREO;
        }

        /**
         * @return the TELEFONO
         */
        public String getTELEFONO() {
            return TELEFONO;
        }

        /**
         * @param TELEFONO the TELEFONO to set
         */
        public void setTELEFONO(String TELEFONO) {
            this.TELEFONO = TELEFONO;
        }

        /**
         * @return the ERROR
         */
        public String getERROR() {
            return ERROR;
        }

        /**
         * @param ERROR the ERROR to set
         */
        public void setERROR(String ERROR) {
            this.ERROR = ERROR;
        }
    }
}
