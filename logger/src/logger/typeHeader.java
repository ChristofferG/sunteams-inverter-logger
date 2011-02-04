/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logger;

/**
 *
 * @author RUS
 */
public class typeHeader {

    public static final int UNKNOWN = 0;
    public static final int VALUES = 1;
    public static final int[] VALUESh = {0xAA, 0xAA, 0xFF, 0x01, 0x01, 0x00, 0x01, 0x82};
    public static final int SERIAL = 2;
    public static final int[] SERIALh = {0xAA, 0xAA, 0xFF, 0x00, 0x7F, 0x00, 0x00, 0x80};
    public static final int SERIAL_CONFIRMED = 3;
    public static final int[] SERIAL_CONFIRMEDh = {0xAA, 0xAA, 0xFF, 0x01, 0x7F, 0x00, 0x00, 0x81};
}
