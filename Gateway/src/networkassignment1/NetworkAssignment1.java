/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkassignment1;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author minhh
 */
public class NetworkAssignment1 {

    /**
     * @param args the command line arguments
     * @throws java.net.SocketException
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, URISyntaxException, MqttException {
        // TODO code application logic here
        //create a gateway socket
        Gateway gateway = new Gateway(1024);
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        URI uri = new URI("http://xvtpdjfm:1VyJas3hrGu9@m10.cloudmqtt.com:15782");
        CloudMQTT SendSocket = new CloudMQTT(uri);
        //run gateway socket
        Gateway.running();
    
    }
    
}
