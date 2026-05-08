/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AppStarter implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppStarter.class);

    /**
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String file = "/usr/archivos/referidosrrvv/data/mae/log4j.properties";
        if (file != null) {
            PropertyConfigurator.configure(file);
        }
        LOGGER.info(
                "AppStarter:contextInitialized:: AppStarter contextInitialized");
    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("AppStarter:contextInitialized:: AppStarter contextDestroyed");
    }
}
