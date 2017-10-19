package Analytic;

import Connect2Gateway.CloudMQTT;
import Database.Database;
import com.mongodb.client.MongoCollection;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.net.*;

import static java.lang.System.out;

public class Server {
    private static final int port = 9999;
    private static Database database;
    private static QueueRequests queueRequests;
    private static CloudMQTT cloud;
    private static ServiceThread process;

    public static int numberOfThread = 0;
    public static void main(String[] args) throws InterruptedException, URISyntaxException, MqttException, UnknownHostException {
        out.println("Server IP: "+InetAddress.getLocalHost().getHostAddress());

        // Connect to Database
        database = new Database();
        MongoCollection collection = database.getCollection();
        out.println("Connecting to Database...");

        // Create QueueRequests
        queueRequests = new QueueRequests();

        // Connect to Cloud
        URI uri = new URI("http://xvtpdjfm:1VyJas3hrGu9@m10.cloudmqtt.com:15782");
        cloud = new CloudMQTT(uri,collection);
        out.println("Connecting to Cloud...");

        // Listen to clients
        try {
            ServerSocket listener = new ServerSocket(port);
            out.println("Server is listening...");
            while (true) {
                Socket client = listener.accept();
                out.println("Receive request from " + client.getRemoteSocketAddress().toString() + ".");
                queueRequests.push(client);
                out.println("Push request "+ client.getRemoteSocketAddress().toString() +" into Queue.");
                if (numberOfThread < 10) {
                    numberOfThread++;
                    out.println("Number of Threads: " + numberOfThread);
                    process = new ServiceThread(queueRequests.pop(), collection);
                    process.run();
                    out.println("Processing request of " + client.getRemoteSocketAddress().toString() + ".");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
