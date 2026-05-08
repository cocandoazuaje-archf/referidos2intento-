package cl.cns.integracion.wsreferidosrrvv.util;

import cl.cns.integracion.wsreferidosrrvv.vo.EMCredentialsVO;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class CredentialsProperties {

    private static ResourceProperty rpFunc = null;
    private static ResourceProperty rpKeys = null;
    private static final Logger LOGGER = Logger.getLogger(DatosProperties.class);

    private CredentialsProperties() {
        // constructor vacio
    }

    public static EMCredentialsVO loadPropertiesMain() {
        EMCredentialsVO propertiesVO = null;
        propertiesVO = new EMCredentialsVO();
        propertiesVO.setCodigoError(0);

        try {
            rpFunc = new ResourceProperty("func_ws_referidosrrvv");
            rpKeys = new ResourceProperty("key_ws_referidosrrvv");

            propertiesVO.setRutaLlaveEmail(
                    rpFunc.getString("cryptocns.rutallaveEmail"));
            propertiesVO.setRutaLlaveSMS(rpFunc.getString("cryptocns.rutallaveSMS"));

            propertiesVO.setEMAIL_USR(rpKeys.getString("EMAIL_USR"));
            propertiesVO.setEMAIL_PASS(rpKeys.getString("EMAIL_PASS"));
            propertiesVO.setSMS_USR(rpKeys.getString("SMS_USR"));
            propertiesVO.setSMS_PASS(rpKeys.getString("SMS_PASS"));
        } catch (IOException ex) {
            java.util.logging.Logger
                    .getLogger(DatosProperties.class.getName())
                    .log(Level.SEVERE, null, ex);
            LOGGER.error("error al leer el archivo properties : ", ex);
            propertiesVO.setCodigoErromensaje(ex.getMessage());
            propertiesVO.setCodigoError(Constantes.ERROR);
        }

        return propertiesVO;
    }
}
