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
public class LogWriter {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void WriteDataToLog(Date date, int Now, double eTotal, double eToday, double Temp) {
        String x = String.format("%s %04d %07.1f %04.1f %03.1f\n", utils.getTime2(date), Now, eTotal, eToday, Temp);
        logger.info("Write Values " + x);
        try {
            FileWriter write = new FileWriter(settings.getDailyFile()+"\\" + utils.getFileDate(date) + ".data", true);
            PrintWriter print_line = new PrintWriter(write);
            print_line.printf("%s %04d %07.1f %04.1f %03.1f\n", utils.getTime2(date), Now, eTotal, eToday, Temp);
            print_line.close();

        } catch (Exception ex) {
            logger.error(ex);
        }
    }
    public void WriteLine(Date date) {
        try {
            FileWriter write = new FileWriter(settings.getDailyFile()+"\\" + utils.getFileDate(date) + ".data", true);
            PrintWriter print_line = new PrintWriter(write);
            print_line.printf("=========================================================================\n");
            print_line.close();

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

}
