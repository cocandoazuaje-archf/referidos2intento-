package cl.cns.integracion.wsreferidosrrvv.util;

import cl.cns.integracion.wsreferidosrrvv.models.*;
import cl.cns.integracion.wsreferidosrrvv.vo.DatosContactoPropertiesMainVO;
import cl.cns.integracion.wsreferidosrrvv.vo.EMCredentialsVO;
import cl.cns.integracion.wsreferidosrrvv.vo.EnvioCorreoOutput;
import cl.cnsv.crypto.encryption.CryptoUtil;
import cl.cnsv.referidosrrvv.models.Referidos;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

public class EMCUtil {

        private static final Logger LOGGER = Logger.getLogger(EMCUtil.class);
        private static final DatosContactoPropertiesMainVO propertiesVO = DatosProperties.loadPropertiesMain();

        private static final EMCredentialsVO credentialsVO = CredentialsProperties.loadPropertiesMain();

        private static final  cryptoUtil = new CryptoUtil("",credentialsVO.getRutaLlaveEmail());
        private static final String EMAILPWD = cryptoUtil.decryptData(
                        credentialsVO.getEMAIL_PASS());

        private static EmailMaticoCall creaCorreo(
                        Referidos referido,
                        Long idpregunta) {
                EmailMaticoCall objRetorno = new EmailMaticoCall();
                EMCCliente cliente = new EMCCliente();
                cliente.setNombre(referido.getNombre());
                String apellido = referido.getApellido().equals("")
                                ? "."
                                : referido.getApellido();
                cliente.setApellidoPaterno(apellido);
                cliente.setEmail(referido.getCorreo());
                objRetorno.setCliente(cliente);

                // Fecha y hora
                String patternFecha = "dd/MM/yyyy";
                String patternHora = "HH:mm";
                SimpleDateFormat formatoFecha = new SimpleDateFormat(patternFecha);
                SimpleDateFormat formatoHora = new SimpleDateFormat(patternHora);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, propertiesVO.getAPIEMSegundosDelay());
                String fecha = formatoFecha.format(calendar.getTime());
                String hora = formatoHora.format(calendar.getTime());
                objRetorno.setFecha(fecha);
                objRetorno.setHora(hora);

                // Email
                EMCEmail email = new EMCEmail();

                // ID Plantilla
                switch (idpregunta.intValue()) {
                        case 1:
                                email.setIdPlantilla(propertiesVO.getAPIEMPlantillaP1());
                                break;
                        case 2:
                                email.setIdPlantilla(propertiesVO.getAPIEMPlantillaP2());
                                break;
                        default:
                                // Code to be executed if idpregunta is not 1 or 2
                                break;
                }

                // Parámetros
                EMCParametro param1 = new EMCParametro();
                param1.setNombre("cli_param1");
                param1.setValor(cliente.getNombre());
                EMCParametro param2 = new EMCParametro();
                param2.setNombre("cli_param2");
                param2.setValor(cliente.getApellidoPaterno());

                EMCParametros parametros = new EMCParametros();
                ArrayList<EMCParametro> parametro = new ArrayList<>();
                parametro.add(param1);
                parametro.add(param2);
                parametros.setParametro(parametro);
                email.setParametros(parametros);

                // Destinatarios
                EMCDestinatario destinatario = new EMCDestinatario();
                destinatario.setEmail(referido.getCorreo());

                EMCDestinatarios destinatarios = new EMCDestinatarios();
                ArrayList<EMCDestinatario> arrDestinatario = new ArrayList<>();
                arrDestinatario.add(destinatario);
                destinatarios.setDestinatario(arrDestinatario);
                email.setDestinatarios(destinatarios);

                objRetorno.setEmail(email);

                // Flag RRVV
                objRetorno.setFlagLineaNegocio(2);

                return objRetorno;
        }

        public static EnvioCorreoOutput enviaCorreo(
                        Referidos referido,
                        Long idpregunta) {
                EmailMaticoCall correo = creaCorreo(referido, idpregunta);

                EnvioCorreoOutput objRetorno = new EnvioCorreoOutput();

                try {
                        String url = propertiesVO.getAPIEMTarget() + propertiesVO.getAPIEMPath();
                        String usr = credentialsVO.getEMAIL_USR();
                        String pwd = EMAILPWD;

                        CredentialsProvider provider = new BasicCredentialsProvider();
                        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                                        usr,
                                        pwd);
                        provider.setCredentials(AuthScope.ANY, credentials);

                        CloseableHttpClient client = HttpClientBuilder
                                        .create()
                                        .setDefaultCredentialsProvider(provider)
                                        .build();
                        HttpPost httpPost = new HttpPost(url);

                        ObjectMapper mapper = new ObjectMapper();
                        String jsonString = mapper.writeValueAsString(correo);
                        StringEntity entity = new StringEntity(jsonString);
                        httpPost.setEntity(entity);
                        httpPost.setHeader("Accept", "application/json");
                        httpPost.setHeader("Content-type", "application/json");

                        // Ejecutar llamada
                        HttpResponse response = client.execute(httpPost);
                        int statusCode = response.getStatusLine().getStatusCode();
                        HttpEntity entityResponse = response.getEntity();

                        objRetorno = new ObjectMapper()
                                        .readValue(
                                                        EntityUtils.toString(entityResponse),
                                                        EnvioCorreoOutput.class);
                        LOGGER.info(
                                        "[EncuestaService][enviarCorreo] RESPUESTA WS: " + objRetorno.toString());

                        objRetorno.setCodigo(0);
                        objRetorno.setMensaje(
                                        "Servicio API_EmailMatico contactado con éxito, Código: " + statusCode);
                        client.close();
                } catch (IOException | ParseException e) {
                        LOGGER.error(e);
                        objRetorno.setCodigo(-1);
                        objRetorno.setMensajeCorreo(
                                        "Error al conectar a servicio de correos: " + e.getMessage());
                }
                return objRetorno;
        }
}
