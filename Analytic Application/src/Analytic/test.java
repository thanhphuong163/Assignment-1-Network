package Analytic;

import Connect2Gateway.CloudMQTT;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.System.out;

public class test {
    private static final String host = "localhost";
    private static final int port = 27017;
    public static int numberOfThread = 0;
    public static void main(String[] args) throws InterruptedException, URISyntaxException, MqttException {
        // Connect to Database
        MongoClient mongo = new MongoClient(host,port);
        MongoDatabase database = mongo.getDatabase("dataBase");
        MongoCollection collection = database.getCollection("collection");
        QueueRequests queueRequests = new QueueRequests();
        out.println("Connected to Database successfully");
        URI uri = new URI("http://xvtpdjfm:1VyJas3hrGu9@m10.cloudmqtt.com:15782");
        CloudMQTT s = new CloudMQTT(uri,collection);
        out.println("Connected to Cloud successfully.");

        try {
            ServerSocket listener = new ServerSocket(9999);
            out.println("Server is listening...");
            while (true) {
                Socket client = listener.accept();
                out.println("Receive request from " + client.getRemoteSocketAddress().toString() + ".");
                queueRequests.push(client);
                out.println("Push request "+ client.getRemoteSocketAddress().toString() +" into Queue.");
                if (numberOfThread < 10) {
                    numberOfThread++;
                    out.println("Number of threads: " + numberOfThread);
                    ServiceThread process = new ServiceThread(queueRequests.pop(), numberOfThread, collection);
                    process.start();
                    out.println("Processing request of " + client.getRemoteSocketAddress().toString() + ".");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
