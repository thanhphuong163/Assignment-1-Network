package Analytic;

import java.net.Socket;
import java.util.LinkedList;

public class QueueRequests {
    private LinkedList<Socket> queueRequests;

    public QueueRequests() {
        this.queueRequests = new LinkedList<>();
    }

    public void push(Socket item) {
        if (!this.isFull()){
            this.queueRequests.addLast(item);
        }
    }

    public Socket pop() {
        return this.queueRequests.removeFirst();
    }

    public boolean isFull() {
        if (this.queueRequests.size() > 100) {
            return true;
        }
        else {
            return false;
        }
    }

    public  boolean isEmpty() {
        return this.queueRequests.isEmpty();
    }
}