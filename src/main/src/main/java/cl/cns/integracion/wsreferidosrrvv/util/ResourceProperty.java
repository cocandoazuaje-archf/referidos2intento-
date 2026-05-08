package cl.cns.integracion.wsreferidosrrvv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class ResourceProperty {

    Properties pb;
    private static final Logger LOGGER = Logger.getLogger(ResourceProperty.class);

    public ResourceProperty(String resource)
            throws FileNotFoundException, IOException {
        // File archivo = new File(System.getProperty("ARCHIVOS_BASE") +
        // "ws-referidos-rrvv/data/mae/" + resource + ".properties");

        URL url = new URL(
                System.getProperty("ARCHIVOS_BASE")
                        + "ws-referidos-rrvv/data/mae/"
                        + resource
                        + ".properties");
        File archivo = FileUtils.toFile(url);

        try (FileInputStream fis = new FileInputStream(archivo)) {
            pb = new Properties();
            pb.load(fis);
        } catch (FileNotFoundException e) {
            LOGGER.error(
                    "Error, El Archivo de configuraciones solicitado no existe ",
                    e);
            throw e;
        } catch (IOException e) {
            LOGGER.error("Error, Generico Detalle: ", e);
            throw e;
        }
    }

    public String getString(String name) {
        return pb.getProperty(name);
    }
}
