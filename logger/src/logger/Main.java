/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logger;

import jcomm.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author RUS
 */
public class Main {

    private static int DELAY = 1000;
    private static int[] init1 = {0x43, 0x5A, 0x00, 0x00, 0x7F, 0x00, 0x00, 0x03, 0x00, 0x01, 0x1F};
    private static int[] init2 = {0x43, 0x5A, 0x00, 0x00, 0x7F, 0x00, 0x00, 0x00, 0x00, 0x01, 0x1C};
    private static int[] sendSerial = {0x43, 0x5A, 0x00, 0x00, 0x7F, 0x01, 0x00, 0x01, 0x0B, 0x41, 0x32, 0x46, 0x53, 0x41, 0x32, 0x31, 0x30, 0x32, 0x31, 0x01, 0x03, 0x6D};
    private static int[] getValues = {0x43, 0x5A, 0x00, 0x00, 0x01, 0x01, 0x01, 0x02, 0x00, 0x00, 0xA2};
    private static int[] getmodel = {0x43, 0x5A, 0x00, 0x00, 0x01, 0x01, 0x01, 0x03, 0x00, 0x00, 0xA3};
    private static int[] getParms = {0x43, 0x5A, 0x00, 0x00, 0x01, 0x01, 0x01, 0x04, 0x00, 0x00, 0xA4};
    private static RS232Impl rs232;
    private static Logger logger = Logger.getLogger("Main");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.xml");

        logger.debug("Startup");
        String Port = settings.getPort();
        if (Port.contains("BAD")) {
            logger.error("Com Port not defined");
            System.out.println("\nCom Port not defined");
            System.exit(2);
        }
        int tWait = settings.getSleep();
        // counts how many passes with no reply
        int NoDataCount = 0;
        // message queue from message handler;
        aMessageQueue msg = aMessageQueue.getInstance();

        aFifoQueue DataIn = aFifoQueue.getInstance();
        // queue should be empty anyway
        DataIn.Clear();

        // TODO code application logic here
        rs232 = new RS232Impl();
        messageHandler in = new messageHandler();
        in.start();

        DCB dcb = new DCB();
        Timeouts ts = new Timeouts();
        EvtMask evt = new EvtMask();
        dcb.BaudRate = 9600;
        dcb.ByteSize = 8;
        dcb.Parity = 0;
        dcb.StopBits = 0;


        //dcb.DCBlength = 28;
        dcb.fBinary = 1;
        dcb.fOutxCtsFlow = 0;

        ts.ReadIntervalTimeout = 0;
        ts.ReadTotalTimeoutConstant = 10;
        ts.ReadTotalTimeoutMultiplier = 0;
        ts.WriteTotalTimeoutMultiplier = 10;
        ts.WriteTotalTimeoutConstant = 10;

        evt.EvtMask = EvtMask.EV_RXCHAR;
        try {
            int result = rs232.initComm();
            if (result == CommRS232.RS232_OK) {
                rs232.start();
                rs232.setDCB(dcb);
                rs232.setTimeouts(ts);
                rs232.setEvtMask(evt);
                for (;;) {
                    if (rs232.openComm(Port) == CommRS232.RS232_OK) {

                        NoDataCount = 0;
                        Initialize();
                        Thread.sleep(2000);
                        if (msg.isEmpty()) {
                            logger.info("Looks to be asleep , wait for Inverter " + (tWait / 1000) + " Seconds");
                            Thread.sleep(tWait);
                            System.exit(1);
                        } else if (msg.getValue() == typeMessage.GOT_SERIAL) {
                            confirmSerial();
                            if (msg.getValue() == typeMessage.SERIAL_CONFIRM) {
                                getModel();
                                while (NoDataCount < 5) {
                                    getValues();
                                    Thread.sleep(1000 * 30);
                                    if (!msg.isEmpty()) {
                                        if (msg.getValue() == typeMessage.DATA_ZERO) {
                                            NoDataCount++;
                                        } else {
                                            NoDataCount = 0;
                                        }
                                    } else {
                                        NoDataCount++;
                                    }
                                }
                                logger.info("No Data Recieved");
                                Thread.sleep(60000);
                            }

                        } else {
                            logger.info("Looks to be asleep , wait for Inverter " + (tWait / 1000) + " Seconds");
                            Thread.sleep(tWait);
                            System.exit(1);
                        }
                    }
                    rs232.closeComm();
                }
            } else {
                logger.error("Error on start rs232");
            }
        } catch (Exception ex) {
            logger.error(ex);




        }
        System.exit(1);




    }

    private static void Initialize() {
        logger.info("Init Comms to Inverter");
        try {
            for (int x = 0; x
                    < 5; x++) {
                rs232.write(ToBytes(init1));
                Thread.sleep(DELAY);
            }
            rs232.write(ToBytes(init2));
            Thread.sleep(DELAY * 5);
        } catch (Exception ex) {
            logger.error(ex);
        }

    }

    private static void confirmSerial() {
        logger.info("Confirm Serial");
        try {
            rs232.write(ToBytes(sendSerial));
            Thread.sleep(DELAY * 5);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    private static void getModel() {
        logger.info("Get Model");
        try {
            for (int x = 0; x
                    < 16; x++) {
                rs232.write(ToBytes(init2));
                Thread.sleep(DELAY);
            }
            rs232.write(ToBytes(getmodel));
            Thread.sleep(DELAY * 3);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    private static void getValues() {
        logger.info("Get Values");
        try {
            rs232.write(ToBytes(getValues));
            logger.debug("Sleep " + (DELAY * 5));
            Thread.sleep(DELAY * 5);
        } catch (Exception ex) {
            logger.error(ex);
        }

    }

    /**
     * Convert the array of integers to an array of bytes
     *
     * @param data
     * @return
     */
    private static byte[] ToBytes(int data[]) {
        byte[] out = new byte[data.length];
        for (int x = 0; x
                < data.length; x++) {
            out[x] = (byte) data[x];
            //System.out.printf("%02x ", out[x]);
        } //System.out.println("");
        return out;
    }
}
