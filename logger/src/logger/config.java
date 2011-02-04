/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package logger;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.commons.configuration.*;

/**
 *
 * @author RUS
 */
public class config {
    private PropertiesConfiguration factory = null;
    Logger logger = Logger.getLogger(this.getClass().getName());

    private config() {
        PropertyConfigurator.configure("log4j.xml");
        logger.debug("Create Config Factory");
        try {
            factory = new PropertiesConfiguration("sunteams.properties");
            factory.setAutoSave(true);
        } catch (ConfigurationException ex) {
            java.util.logging.Logger.getLogger(config.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static config getInstance() {
        PropertyConfigurator.configure("log4j.xml");
        Logger loggers = Logger.getLogger("config.Getinstance");
        return mainHolder.INSTANCE;
    }

    public PropertiesConfiguration getFactory() {
        return factory;
    }

    private static class mainHolder {

        private static final config INSTANCE = new config();
    }

}
