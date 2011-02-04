/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package logger;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author RUS
 */
public class utils {
    public static String getFileDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    public static String getDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }

    public static String getTime1(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }
    public static String getTime2(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

    public static  boolean get10Min(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("mm");
        return dateFormat.format(date).lastIndexOf("0") == 1;
    }

}
