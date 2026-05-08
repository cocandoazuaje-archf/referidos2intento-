package cl.cns.integracion.wsreferidosrrvv.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.cns.integracion.wsreferidosrrvv.models.SMSCall;
import cl.cns.integracion.wsreferidosrrvv.models.SMSParametros;
import cl.cns.integracion.wsreferidosrrvv.vo.DatosContactoPropertiesMainVO;
import cl.cns.integracion.wsreferidosrrvv.vo.EMCredentialsVO;
import cl.cns.integracion.wsreferidosrrvv.vo.SMSOutput;
import cl.cnsv.crypto.encryption.CryptoUtil;

public class SMSUtil {

        private static final Logger LOGGER = Logger.getLogger(EMCUtil.class);
        private static final DatosContactoPropertiesMainVO propertiesVO = DatosProperties.loadPropertiesMain();
        private static final EMCredentialsVO credentialsVO = CredentialsProperties.loadPropertiesMain();

        private static final CryptoUtil cryptoUtil = new CryptoUtil(
                        "",
                        credentialsVO.getRutaLlaveSMS());
        private static final String SMSPWD = cryptoUtil.decryptData(
                        credentialsVO.getSMS_PASS());

        public static SMSOutput enviaSMS(
                        String movil,
                        int idPregunta,
                        SMSParametros parametros) {
                LOGGER.info("[SMSUtil][enviaSMS] INI");

                SMSOutput objRetorno = new SMSOutput();

                SMSCall sms = new SMSCall();
                sms.setCelular(movil);
                sms.setLineaNegocio(propertiesVO.getSMSLineaNegocio());
                sms.setParametros(parametros);

                switch (idPregunta) {
                        case 1:
                                sms.setIdPlantilla(propertiesVO.getSMSPlantillaP1());
                                break;
                        case 2:
                                sms.setIdPlantilla(propertiesVO.getSMSPlantillaP2());
                                break;
                        default:
                                objRetorno.setCodigo(-1);
                                objRetorno.setMensaje("ID pregunta no válido");
                                return objRetorno;
                }

                LOGGER.info(
                                "[SMSUtil][enviaSMS] Llamando a servicio con parámetros: "
                                                + sms.toString());

                try {
                        CredentialsProvider provider = new BasicCredentialsProvider();
                        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                                        credentialsVO.getSMS_USR(),
                                        SMSPWD);
                        provider.setCredentials(AuthScope.ANY, credentials);

                        CloseableHttpClient client = HttpClientBuilder
                                        .create()
                                        .setDefaultCredentialsProvider(provider)
                                        .build();
                        HttpPost httpPost = new HttpPost(
                                        propertiesVO.getSMSTarget() + propertiesVO.getSMSPath());

                        ObjectMapper mapper = new ObjectMapper();
                        String jsonString = mapper.writeValueAsString(sms);
                        StringEntity entity = new StringEntity(jsonString);
                        httpPost.setEntity(entity);
                        httpPost.setHeader("Content-type", "application/json");

                        // Ejecutar llamada
                        HttpResponse response = client.execute(httpPost);
                        int statusCode = response.getStatusLine().getStatusCode();
                        HttpEntity entityResponse = response.getEntity();

                        objRetorno = new ObjectMapper()
                                        .readValue(EntityUtils.toString(entityResponse), SMSOutput.class);
                        LOGGER.info("[SMSUtil][enviaSMS] RESPUESTA WS: " + objRetorno.toString());

                        objRetorno.setCodigo(0);
                        objRetorno.setMensaje(
                                        "Servicio wsEmailMatico contactado con éxito, Código: " + statusCode);
                        client.close();

                        LOGGER.info("[SMSUtil][enviaSMS] result: " + statusCode);

                        return objRetorno;
                } catch (IOException | ParseException e) {
                        LOGGER.error("[SMSUtil][enviarSMS] ERROR: " + e.getMessage(), e);
                        objRetorno.setCodigo(-1);
                        objRetorno.setMensaje(
                                        "Error al conectar a servicio de SMS: " + e.getMessage());
                }
                return objRetorno;
        }
}
