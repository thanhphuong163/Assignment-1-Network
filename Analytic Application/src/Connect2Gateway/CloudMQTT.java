/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connect2Gateway;

//import com.mongodb.Mongo;
import com.mongodb.client.MongoCollection;
//import jdk.nashorn.internal.parser.JSONParser;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

//import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.System.out;

/**
 *
 * @author minhh
 */
public class CloudMQTT {
    private final int qos = 1;
    private String topic = "test";
    private MqttClient client;
    private MongoCollection collection;
    public static String[] receiveFromCloud = new String[100];
    public static int count = 0;

    public CloudMQTT(String uri, MongoCollection collection) throws MqttException, URISyntaxException {
        this(new URI(uri),collection);
    }

    public CloudMQTT(URI uri, MongoCollection collection) throws MqttException {
        this.collection = collection;
        String host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
        out.println(host);
        String[] auth = getAuth(uri);
        String username = auth[0];
        String password = auth[1];
        String clientId = "MQTT-Java-Example";
        if (!uri.getPath().isEmpty()) {
            topic = uri.getPath().substring(1);
        }

        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setUserName(username);
        conOpt.setPassword(password.toCharArray());

        client = new MqttClient(host, clientId, new MemoryPersistence());
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {}
            
            @Override
            public void messageArrived(String topic,
                    MqttMessage message)
                            throws Exception {
                out.println(String.format("[%s] %s", topic, new String(message.getPayload())));


                JSONObject json = new JSONObject(message);
                Document doc = new Document()
                        .append("ID", json.getString("ID"))
                        .append("Long", json.getDouble("Long"))
                        .append("Lat", json.getDouble("Lat"))
                        .append("Time", json.getString("Time"));
                collection.insertOne(doc);
                out.println("Added successfully.");
            }
            
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });

        client.connect(conOpt);
        client.subscribe(topic, qos);
        
    }

    private String[] getAuth(URI uri) {
        String a = uri.getAuthority();
        String[] first = a.split("@");
        return first[0].split(":");
    }

//    public void sendMessage(String payload) throws MqttException {
//        MqttMessage message = new MqttMessage(payload.getBytes());
//        message.setQos(qos);
//        client.publish(topic, message); // Blocking publish
//        System.out.println("Sent Message: "+message);
//    }

//    public static void main(String[] args) throws MqttException, URISyntaxException, MalformedURLException {
//        //URL url = new URL("xvtpdjfm:1VyJas3hrGu9@http://m10.cloudmqtt.com:15782");
//        //URI uri = url.toURI();
//        //System.out.println(uri);
//        URI uri = new URI("http://xvtpdjfm:1VyJas3hrGu9@m10.cloudmqtt.com:15782");
//        CloudMQTT s = new CloudMQTT(uri, collection);
//
////        s.sendMessage("Hello");
////        s.sendMessage("Hello 2");
//    }
}