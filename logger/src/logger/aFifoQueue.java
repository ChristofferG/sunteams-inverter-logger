/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package logger;
import java.util.LinkedList;
/**
 *
 * @author RUS
 */
public class aFifoQueue {

    private LinkedList <Integer>data;

    private aFifoQueue() {
        data = new LinkedList();
    }

    public void addByte(byte value) {
        int byteValue = new Integer(value).intValue();
        if (byteValue < 0) {
            byteValue = value & 0x80;
            byteValue += value & 0x7F;
        }
        data.add(byteValue);
    }
    public boolean isEmpty() {
        return data.isEmpty();
    }
    public int getValue() {
        if (data.isEmpty()) {
            return -1;
        }
        else {
            return data.removeFirst();
        }
    }
public void Clear() {
    while (data.size() > 0) {
        data.remove();
    }
}
    public static aFifoQueue getInstance() {
        return aFifoQueueHolder.INSTANCE;
    }

    private static class aFifoQueueHolder {
        private static final aFifoQueue INSTANCE = new aFifoQueue();
    }
 }
