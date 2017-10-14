/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkassignment1;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author minhh
 */
public class CloudMQTT {
    private final int qos = 1;
    private String topic = "test";
    private MqttClient client;
    public static String[] receiveFromCloud = new String[100];
    public static int count = 0;

    public CloudMQTT(String uri) throws MqttException, URISyntaxException {
        this(new URI(uri));
    }

    public CloudMQTT(URI uri) throws MqttException {
        String host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
        System.out.println(host);
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
                System.out.println(String.format("[%s] %s", topic, new String(message.getPayload())));
                //receiveFromCloud[count++] = new String(message.getPayload());
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

    public void sendMessage(String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        client.publish(topic, message); // Blocking publish
        System.out.println("Sent Message: "+message);
    }

    public static void main(String[] args) throws MqttException, URISyntaxException, MalformedURLException {
        //URL url = new URL("xvtpdjfm:1VyJas3hrGu9@http://m10.cloudmqtt.com:15782");
        //URI uri = url.toURI();
        //System.out.println(uri);
        //URI uri = new URI("http://xvtpdjfm:1VyJas3hrGu9@m10.cloudmqtt.com:15782");
        //CloudMQTT s = new CloudMQTT(uri);
       
//        s.sendMessage("Hello");
//        s.sendMessage("Hello 2");
    }
}