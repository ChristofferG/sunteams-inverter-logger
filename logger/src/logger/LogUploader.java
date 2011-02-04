/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logger;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

/**
 *
 * @author RUS
 */
public class LogUploader {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void Upload(Date date, double eToday, int now) {
        boolean CanUpload = true;
        String tCurl = settings.getCurl();
        String tAPIKey = settings.getAPIKey();
        String tSystemId = settings.getSystemId();
        String tURL = settings.getURL();
        String x = String.format("%s -d \"d=%s\" -d \"t=%s\" -d \"v1=%d\" -d \"v2=%d\" -H \"X-Pvoutput-SystemId: %s\" -H \"X-Pvoutput-Apikey: %s\" %s", tCurl, utils.getDate(date), utils.getTime1(date), (int) (eToday * 1000), now, tSystemId, tAPIKey, tURL);
        if (tCurl.contentEquals("BAD") || tAPIKey.contentEquals("BAD") || tSystemId.contentEquals("BAD") || tURL.contentEquals("BAD")) {
            logger.info("Cannot upload, one or more values for upload missing, check sunteams.properties file");
            logger.debug("Run command " + x);

        } else {
            logger.debug("Run command " + x);
            try {
                Process p = Runtime.getRuntime().exec(x);
                p.waitFor();
                logger.debug("Exit Value " + p.exitValue());

            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }
}
