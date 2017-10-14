package Analytic;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.out;


public class test {
    public static void main(String[] args) throws InterruptedException {
        MongoClient mongo = new MongoClient("localhost",27017);
        MongoDatabase database = mongo.getDatabase("dataGPS");

        QueueRequests queueRequests = new QueueRequests();
        int numberOfThread = 0;
        try {
            ServerSocket listener = new ServerSocket();
            out.println("Server is listening...");
            while (true) {
                Socket client = listener.accept();
                out.println("Receive request from " + client.getRemoteSocketAddress().toString() + ".");
                queueRequests.push(client);
                out.println("Push request "+ client.getRemoteSocketAddress().toString() +" into Queue.");
                if (numberOfThread < 10) {
                    numberOfThread++;
                    ServiceThread process = new ServiceThread(queueRequests.pop(), numberOfThread);
                    process.start();
                    out.println("Processing request of " + client.getRemoteSocketAddress().toString() + ".");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
