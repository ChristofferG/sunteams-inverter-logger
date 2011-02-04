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
public class aMessageQueue {

    private LinkedList <Integer>data;

    private aMessageQueue() {
        data = new LinkedList();
    }

    public void addMessage(int value) {
        data.add(value);
    }
    public boolean isEmpty() {
        return data.isEmpty();
    }
    public int getValue() {
        if (data.isEmpty()) {
            return typeMessage.EMPTY;
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
    public static aMessageQueue getInstance() {
        return aMessageQueueHolder.INSTANCE;
    }

    private static class aMessageQueueHolder {
        private static final aMessageQueue INSTANCE = new aMessageQueue();
    }
 }
