/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author cox
 */
public class ProspeccionGetConfig {

    private static final Logger LOGGER = Logger.getLogger(
            ProspeccionGetConfig.class);
    private String url;
    private String x_api_key;
    private String archivo2 = getClass()
            .getClassLoader()
            .getResource("ProspeccionConfig.xml")
            .getFile();

    public ProspeccionGetConfig() {
        try {
            URL url = new URL("file://" + archivo2);
            File fXmlFile = FileUtils.toFile(url);
            // File fXmlFile = new File(archivo2);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            dbFactory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true);
            dbFactory.setFeature(
                    "http://xml.org/sax/features/external-general-entities",
                    false);
            dbFactory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities",
                    false);

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("config");

            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                this.url = eElement.getElementsByTagName("url").item(0).getTextContent();
                this.x_api_key = eElement.getElementsByTagName("x_api_key").item(0).getTextContent();
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.error(ex);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getX_api_key() {
        return x_api_key;
    }
}
