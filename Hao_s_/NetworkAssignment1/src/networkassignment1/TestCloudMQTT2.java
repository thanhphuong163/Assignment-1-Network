/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkassignment1;

import com.sun.jndi.toolkit.url.Uri;
import java.net.MalformedURLException;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author minhh
 */
public class TestCloudMQTT2 {
    private final int qos = 1;
    private String topic = "test";
    private MqttClient client;
    static MQTTCallBackImplement callBack = new MQTTCallBackImplement();

    public TestCloudMQTT2(String uri) throws MqttException, URISyntaxException {
        this(new URI(uri));
    }

    public TestCloudMQTT2(URI uri) throws MqttException {
        String host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
        System.out.println(host);
        String[] auth = getAuth(uri);
        String username = auth[0];
        String password = auth[1];
        String message;
        String clientId = "MQTT-Java-Example-ahihi";
        if (!uri.getPath().isEmpty()) {
            topic = uri.getPath().substring(1);
        }

        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setUserName(username);
        conOpt.setPassword(password.toCharArray());

        client = new MqttClient(host, clientId, new MemoryPersistence());
        client.setCallback(callBack);
        //System.out.println(callBack.getMessage()[0]);
        
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
        System.out.println(MQTTCallBackImplement.returnMessage[2]);
    }

    public static void main(String[] args) throws MqttException, URISyntaxException, MalformedURLException {
        //URL url = new URL("xvtpdjfm:1VyJas3hrGu9@http://m10.cloudmqtt.com:15782");
        //URI uri = url.toURI();
        //System.out.println(uri);
        URI uri = new URI("http://xvtpdjfm:1VyJas3hrGu9@m10.cloudmqtt.com:15782");
        TestCloudMQTT2 s = new TestCloudMQTT2(uri);
        s.sendMessage("Hello");
//        s.sendMessage("Hello 2");
    }
}
