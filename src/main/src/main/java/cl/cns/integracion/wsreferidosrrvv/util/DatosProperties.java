package cl.cns.integracion.wsreferidosrrvv.util;

import cl.cns.integracion.wsreferidosrrvv.vo.DatosContactoPropertiesMainVO;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class DatosProperties {

        private static ResourceProperty rp = null;
        private static final Logger LOGGER = Logger.getLogger(DatosProperties.class);

        private DatosProperties() {
                // constructor vacio
        }

        public static DatosContactoPropertiesMainVO loadPropertiesMain() {
                DatosContactoPropertiesMainVO propertiesVO = null;
                propertiesVO = new DatosContactoPropertiesMainVO();
                propertiesVO.setCodigoError(0);

                try {
                        rp = new ResourceProperty("conf_ws_referidosrrvv");

                        propertiesVO.setLog4jFileName(
                                        rp.getString("log4j.name.file") + ".properties");
                        propertiesVO.setLog4jPath(
                                        System.getProperty("ARCHIVOS_BASE") + rp.getString("log4j.path"));
                        propertiesVO.setAPIEMTarget(rp.getString("apiem.target"));
                        propertiesVO.setAPIEMPath(rp.getString("apiem.path"));
                        propertiesVO.setAPIEMPreguntasEnvio(rp.getString("apiem.preguntasenvio"));
                        propertiesVO.setAPIEMRespuestaEnvio(
                                        rp.getString("apiem.respuestaenvio").equals("1"));
                        propertiesVO.setAPIEMPlantillaP1(rp.getString("apiem.plantilla.p1"));
                        propertiesVO.setAPIEMPlantillaP2(rp.getString("apiem.plantilla.p2"));
                        propertiesVO.setAPIEMSegundosDelay(
                                        Integer.parseInt(rp.getString("apiem.segundosdelay")));
                        propertiesVO.setSMSTarget(rp.getString("sms.target"));
                        propertiesVO.setSMSPath(rp.getString("sms.path"));
                        propertiesVO.setSMSPlantillaP1(rp.getString("sms.plantilla.p1"));
                        propertiesVO.setSMSPlantillaP2(rp.getString("sms.plantilla.p2"));
                        propertiesVO.setSMSLineaNegocio(rp.getString("sms.lineanegocio"));
                        propertiesVO.setSMSMaxNombre(
                                        Integer.parseInt(rp.getString("sms.maxnombre")));
                } catch (IOException ex) {
                        java.util.logging.Logger
                                        .getLogger(DatosProperties.class.getName())
                                        .log(Level.SEVERE, null, ex);
                        LOGGER.error("error al leer el properties : ", ex);
                        propertiesVO.setCodigoErromensaje(ex.getMessage());
                        propertiesVO.setCodigoError(Constantes.ERROR);
                }

                return propertiesVO;
        }
}
