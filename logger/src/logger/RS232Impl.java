/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logger;

import jcomm.*;

/**
 *
 * @author RUS
 */
public class RS232Impl extends CommRS232 {

    aFifoQueue data = aFifoQueue.getInstance();

    public void onRead(byte[] dataIN) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dataIN.length; i++) {
            data.addByte(dataIN[i]);
        }
    }
}
