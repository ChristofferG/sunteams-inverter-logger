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

    public static final int AVERAGE = 0;
    public static final int PEAK = 1;
    public static final int INSTANT = 2;
    static Logger logger = Logger.getLogger("settings");

    public static String getPort() {
        String port = config.getInstance().getFactory().getString("PORT", "BAD");
        return port;
    }

    public static String getDailyFile() {
        String port = config.getInstance().getFactory().getString("DAILY", "c:\\temp");
        return port;
    }

    public static String getCurl() {
        String port = config.getInstance().getFactory().getString("cURL", "BAD");
        return port;
    }

    public static String getURL() {
        String port = config.getInstance().getFactory().getString("URL", "BAD");
        return port;
    }

    public static String getAPIKey() {
        String port = config.getInstance().getFactory().getString("APIKey", "BAD");
        return port;
    }

    public static String getSystemId() {
        String port = config.getInstance().getFactory().getString("SystemId", "BAD");
        return port;
    }

    public static int getSleep() {
        int time = config.getInstance().getFactory().getInt("SLEEP", 60000);
        return time;
    }
/**
 * Returns how the Instantaneous Power is calculated
 * instant - value at the poll interval
 * average - average value between the poll interval
 * peak - peak value obtained between poll intervals
 * @return
 */
    public static int getMode() {
        String mode = config.getInstance().getFactory().getString("mode", "instant");
        if (mode.contentEquals("instant")) {
            logger.debug("Mode is Instant");
            return INSTANT;
        }
        if (mode.contentEquals("average")) {
            logger.debug("Mode is Average");
            return AVERAGE;
        }
            logger.debug("Mode is Peak");
        return PEAK;
    }
}
