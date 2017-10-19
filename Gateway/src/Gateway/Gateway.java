package Gateway;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import Gateway.Receiver;
import Gateway.CloudMQTT;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;
import org.eclipse.paho.client.mqttv3.MqttException;
/**
 *
 * @author minhh
 */
public class Gateway {
    static Queue<String> DataQueue = new LinkedList<String>();
    static DatagramSocket ReceiveSocket;
    static CloudMQTT SendSocket;

    //constructor
    public Gateway(int port) throws SocketException{
        //create gateway socket
        ReceiveSocket = new DatagramSocket(port);
        // print notification
        System.out.println("Gateway is now ready ...");
    }
    
    
    public static void running(){
        Receiver t = new Receiver();
        t.start();
    }
    
    public static void main(String[] args) throws SocketException, IOException, URISyntaxException, MqttException {
        // TODO code application logic here
        //create a gateway socket
        Gateway gateway = new Gateway(1024);
        System.out.println("Gateway ID >> " + InetAddress.getLocalHost().getHostAddress());
        URI uri = new URI("http://xvtpdjfm:1VyJas3hrGu9@m10.cloudmqtt.com:15782");
        CloudMQTT SendSocket = new CloudMQTT(uri);
        //run gateway socket
        Gateway.running();
    }
}
