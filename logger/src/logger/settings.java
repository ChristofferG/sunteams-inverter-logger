package logger;

import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.commons.configuration.*;

/**
 *
 * @author plowater
 */
public class settings {

    public static String getPort() {
        String port = config.getInstance().getFactory().getString("PORT","BAD");
        return port;
    }
    public static String getDailyFile() {
        String port = config.getInstance().getFactory().getString("DAILY","c:\\temp");
        return port;
    }
    public static String getCurl() {
        String port = config.getInstance().getFactory().getString("cURL","BAD");
        return port;
    }
    public static String getURL() {
        String port = config.getInstance().getFactory().getString("URL","BAD");
        return port;
    }
    public static String getAPIKey() {
        String port = config.getInstance().getFactory().getString("APIKey","BAD");
        return port;
    }
    public static String getSystemId() {
        String port = config.getInstance().getFactory().getString("SystemId","BAD");
        return port;
    }
    public static int getSleep() {
        int time = config.getInstance().getFactory().getInt("SLEEP",60000);
        return time;
    }
}
