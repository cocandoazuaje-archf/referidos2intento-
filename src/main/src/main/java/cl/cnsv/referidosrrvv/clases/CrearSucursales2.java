/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.SucursalesJpaController1;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Sucursales;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author uvv
 */
public class CrearSucursales2 {

    private static final Logger LOGGER = Logger.getLogger(
            CrearSucursales.class.getName());

    public CrearSucursales2() {
        // constructor vacio
    }

    public List<ErrorCrearSucursalesOut> cargar(
            List<EntidadDeCargaJs> rjsList,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {
        SucursalesJpaController1 sc = new SucursalesJpaController1(utx, null, em);
        List<ErrorCrearSucursalesOut> ecrsList = new ArrayList<>();
        Sucursales s = new Sucursales();

        for (EntidadDeCargaJs rjs : rjsList) {
            try {
                s = sc.findSucursales(rjs.getCODSUCURSAL());
            } catch (Exception ex) {
                LOGGER.info(ex);
            }

            try {
                if (s == null && rjs.getCODSUCURSAL() != null) {
                    ErrorCrearSucursalesOut ecrs = new ErrorCrearSucursalesOut();
                    ecrs.setCODSUCURSAL(rjs.getCODSUCURSAL());
                    ecrs.setSUCURSAL(rjs.getSUCURSAL());
                    ecrs.setDIRECCION(rjs.getDIRECCION());
                    ecrs.setCOMUNA(rjs.getCOMUNA());
                    ecrs.setREGION(rjs.getREGION());
                    ecrs.setERROR(
                            "No se puede editar la sucursal "
                                    + rjs.getCODSUCURSAL()
                                    + " , la sucursal no existe.");
                    ecrsList.add(ecrs);
                    continue;
                }
                s = sc.findSucursales(rjs.getCODSUCURSAL());
                s.setNombre(rjs.getSUCURSAL());
                s.setDireccion(rjs.getDIRECCION());
                s.setComuna(rjs.getCOMUNA());
                s.setRegion(rjs.getREGION());
                sc.edit(s);
            } catch (Exception e) {
                LOGGER.info(e);
                s = new Sucursales();
                s.setId(new BigDecimal("85"));
                s.setVersion(BigInteger.ONE);
                s.setNombre(rjs.getSUCURSAL());
                s.setDireccion(rjs.getDIRECCION());
                s.setComuna(rjs.getCOMUNA());
                s.setRegion(rjs.getREGION());
                sc.create(s);
            }
        }
        return ecrsList;
    }

    public static class ErrorCrearSucursalesOut {

        private BigDecimal CODSUCURSAL;
        private String SUCURSAL;
        private String NOMBRE;
        private String DIRECCION;
        private String COMUNA;
        private String REGION;
        private String ERROR;

        /**
         * @return the CODSUCURSAL
         */
        public BigDecimal getCODSUCURSAL() {
            return CODSUCURSAL;
        }

        /**
         * @param CODSUCURSAL the CODSUCURSAL to set
         */
        public void setCODSUCURSAL(BigDecimal CODSUCURSAL) {
            this.CODSUCURSAL = CODSUCURSAL;
        }

        /**
         * @return the SUCURSAL
         */
        public String getSUCURSAL() {
            return SUCURSAL;
        }

        /**
         * @param SUCURSAL the SUCURSAL to set
         */
        public void setSUCURSAL(String SUCURSAL) {
            this.SUCURSAL = SUCURSAL;
        }

        /**
         * @return the NOMBRE
         */
        public String getNOMBRE() {
            return NOMBRE;
        }

        /**
         * @param NOMBRE the NOMBRE to set
         */
        public void setNOMBRE(String NOMBRE) {
            this.NOMBRE = NOMBRE;
        }

        /**
         * @return the DIRECCION
         */
        public String getDIRECCION() {
            return DIRECCION;
        }

        /**
         * @param DIRECCION the DIRECCION to set
         */
        public void setDIRECCION(String DIRECCION) {
            this.DIRECCION = DIRECCION;
        }

        /**
         * @return the COMUNA
         */
        public String getCOMUNA() {
            return COMUNA;
        }

        /**
         * @param COMUNA the COMUNA to set
         */
        public void setCOMUNA(String COMUNA) {
            this.COMUNA = COMUNA;
        }

        /**
         * @return the REGION
         */
        public String getREGION() {
            return REGION;
        }

        /**
         * @param REGION the REGION to set
         */
        public void setREGION(String REGION) {
            this.REGION = REGION;
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
