/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author cox
 */
public class Prospection {

    private static String salida = "";
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(
            Prospection.class);

    public static String consultar(String rut, int timeout) {
        String url2 = Resources.URL_PROSPECTION + "?param=" + rut.trim();
        String url3 = "http://localhost:8080/ws-pagos-multiples-rrvv/webresources/transaccion?usuario=exakzio01";

        try {
            URL url = new URL(url2);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("x-api-key", Resources.KEY_PROSPECTION);
            if (timeout > 0) {
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException(
                        "Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((conn.getInputStream())));

            String output, sal = null;
            while ((output = br.readLine()) != null) {
                sal = output;
            }

            JsonObject obj = new JsonParser().parse(sal).getAsJsonObject();
            salida = obj.get("indicador").toString();

            if ((salida != null)
                    && !(salida.contains("El rut consultado no tiene un indicador asociado"))) {
                salida = salida.replace("\"", "");
            } else {
                salida = "";
            }

            if (conn != null) {
                conn.disconnect();
            }
        } catch (RuntimeException | IOException ex) {
            LOGGER.error(ex);
        }

        return salida;
    }
}
