/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logger;

import java.util.LinkedList;
import org.apache.log4j.Logger;
import java.util.Date;

/**
 *
 * @author RUS
 */
public class messageHandler extends Thread {

    aFifoQueue data = aFifoQueue.getInstance();
    aMessageQueue msg = aMessageQueue.getInstance();
    Logger logger = Logger.getLogger(this.getClass().getName());
    /*
     * Used to calculate the value modes
     * Easier to use global than pass them around
     */
    int instTotal = 0;
    int instNumber = 0;
    int instPeak = 0;
    boolean instWrite = false;

    public void run() {
        LinkedList<Integer> ValuesRecieved = null;
        int headerCount = 0;
        int[] header = new int[8];
        int size = 0;
        int[] sum = new int[2];
        int sumCount = 0;
        int dataCounter = -1;


        logger.info("Incoming Message Thread Started");
        int val = -1;
        do {
            if (!data.isEmpty()) {
                val = data.getValue();
                //System.out.printf("%02X ", val);
                if (headerCount == 0) {
                    if (val == 0xAA) {
                        header[headerCount] = val;
                        headerCount++;
                    } else {
                        System.out.printf(" ?? 0x%02x ", val);

                    }
                    dataCounter = -1;
                } else if (headerCount == 1) {
                    //second should be AA
                    if (val == 0xAA) {
                        header[headerCount] = val;
                        headerCount++;
                    } else {
                        headerCount = 0;
                    }
                    dataCounter = -1;
                } else if (headerCount == 2) {
                    //second should be FF
                    if (val == 0xFF) {
                        header[headerCount] = val;
                        headerCount++;
                    } else {
                        headerCount = 0;
                    }
                    dataCounter = -1;
                } else if (headerCount < 8) {
                    header[headerCount] = val;
                    headerCount++;
                    dataCounter = -1;
                } else if (headerCount == 8) {
                    size = val;
                    headerCount++;
                    logger.debug("Data Size = " + size);
                    dataCounter = 0;
                    ValuesRecieved = new LinkedList();
                } else if (headerCount > 8) {
                    // in here then must have good header and size
                    if (dataCounter >= 0 && dataCounter < size) {
                        //System.out.printf("0x%02x ", val);
                        dataCounter++;
                        sumCount = 0;
                        ValuesRecieved.add(val);
                    } else {
                        // must be start of checksum
                        sum[sumCount] = val;
                        sumCount++;
                        if (sumCount > 1) {
                            headerCount = 0;
                            ProcessData(header, sum, size, ValuesRecieved);

                        }
                    }
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }


        } while (true);
    }

    private void ProcessData(int[] Header, int[] Sum, int Size, LinkedList<Integer> Data) {
        Date date = new Date();

        switch (CheckHeader(Header)) {
            case typeHeader.UNKNOWN:
                String temp = "";
                logger.error("Unknown Header Type :");
                for (int x = 0; x < 8; x++) {
                    temp += String.format("0x%02X ", Header[x]);
                }
                logger.error(String.format("Size : 0x%02X  Data %s", Size, temp));
                msg.addMessage(typeMessage.UNKNOWN);

                break;
            case typeHeader.SERIAL:
                logger.debug("Recieved Serial ");
                msg.addMessage(typeMessage.GOT_SERIAL);
                break;
            case typeHeader.SERIAL_CONFIRMED:
                logger.debug("Serial Confirmed");
                msg.addMessage(typeMessage.SERIAL_CONFIRM);
                break;
            case typeHeader.VALUES:
                double Temp = ((Data.get(0) * 256) + Data.get(1)) / 10.0;
                double Vpv = ((Data.get(2) * 256) + Data.get(3)) / 10.0;
                int ENow = ((Data.get(10) * 256) + Data.get(11));
                double EToday = ((Data.get(12) * 256) + Data.get(13)) / 10.0;
                double ETotal = ((Data.get(16) * 256) + Data.get(17)) / 10.0;

                /*
                 * send a message to the queue if there was output from inverter
                 */
                if (ENow == 0) {
                    msg.addMessage(typeMessage.DATA_ZERO);
                } else {
                    msg.addMessage(typeMessage.DATA_OK);

                }

                /*
                 * update counters
                 */
                instTotal += ENow;
                instNumber++;
                if (ENow > instPeak) {
                    instPeak = ENow;
                }
                logger.debug("Total:" + instTotal + " Samples:" + instNumber + " Peak:" + instPeak);

                LogWriter lw = new LogWriter();

                /*
                 * Only do an upload if at a 10 minute interval and instWrite is false
                 */
                if (utils.get10Min(date) && instWrite == false) {
                    logger.info("Upload Values");
                    LogUploader lu = new LogUploader();
                    lu.Upload(date, EToday, ENow);
                    /*
                     * set the flag so not to do this twice in a row
                     * if the timer happens to trigger trice in the
                     * same minute
                     */
                    instWrite = true;

                    lw.WriteDataToLog(date, ENow, ETotal, EToday, Temp);
                    lw.WriteLine(date);

                    /*
                     * reset the counters
                     */
                    instTotal = 0;
                    instNumber = 0;
                    instPeak = 0;

                } else {
                    instWrite = false;
                    lw.WriteDataToLog(date, ENow, ETotal, EToday, Temp);

                }

                break;
            default:

        }

    }

    private int CheckHeader(int[] Header) {
        boolean match = false;
        if (MatchHeader(Header, typeHeader.VALUESh)) {
            return typeHeader.VALUES;
        }
        if (MatchHeader(Header, typeHeader.SERIALh)) {
            return typeHeader.SERIAL;
        }
        if (MatchHeader(Header, typeHeader.SERIAL_CONFIRMEDh)) {
            return typeHeader.SERIAL_CONFIRMED;
        }

        return typeHeader.UNKNOWN;
    }

    boolean MatchHeader(int[] Header, int[] Match) {
        for (int x = 0; x < 8; x++) {
            if (Header[x] != Match[x]) {
                return false;
            }
        }
        return true;
    }
}
