/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkassignment1;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.DatagramSocket;
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
    private static Queue<String> DataQueue = new LinkedList<String>();
    private static DatagramSocket ReceiveSocket;
    private static CloudMQTT SendSocket;

    //constructor
    public Gateway(int port) throws SocketException{
        //create gateway socket
        ReceiveSocket = new DatagramSocket(port);
        //print notification
        System.out.println("Gateway is now ready ...");
    }
    //Receiving message from UDP clients
    public static class Receiver extends Thread{
        //variable declarations
        byte inFromClient[];
        byte outToClient[];
        int clientPort;
        String data;
        boolean key = true;
        Sender sender = new Sender();
        
        //constructor
        public Receiver(){}
        
        @Override
        public void run(){
            
            try {
                while (true) {
                //create datagram packet
                inFromClient = new byte[256];
                DatagramPacket fromClient = new DatagramPacket(inFromClient,inFromClient.length);
                
                //receive data packet from client
                ReceiveSocket.receive(fromClient);

                //get data from packet (need more implementation)
                data = (new String(fromClient.getData(),0,inFromClient.length)).trim();

                //push data to Queue
                DataQueue.add(data);

                //feedback to client
                String feedbackMessage = "Gateway received data: "+data;
                System.out.println(data);
                
                //packaging
                outToClient = feedbackMessage.getBytes();

                //get client's address and port
                InetAddress address = fromClient.getAddress();
                clientPort = fromClient.getPort();
                DatagramPacket toClient = new DatagramPacket(outToClient,outToClient.length,address,clientPort);

                //send to client
                ReceiveSocket.send(toClient);
                
                //send data thread control
                if (!sender.isAlive()) {
                 sender = new Sender();
                 sender.start();
                }
            }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    
    //Send data class
    public static class Sender extends Thread {
        byte outToCloud[];
        String dataOut;
        public Sender() {
        }
        @Override
        
        public void run(){
            try {
                    while(!DataQueue.isEmpty()){
                       dataOut = DataQueue.poll();
                       SendSocket.sendMessage(dataOut);
                    }
            } catch (MqttException e) {
                System.err.println(e);
            }  
        }
    }
    
    public static void running(){
        Receiver t = new Receiver();
        t.start();
    }
    
    public static void main(String[] args) throws SocketException, IOException, URISyntaxException, MqttException {
        //create a gateway socket
        Gateway gateway = new Gateway(1024);
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        URI uri = new URI("http://xvtpdjfm:1VyJas3hrGu9@m10.cloudmqtt.com:15782");
        SendSocket = new CloudMQTT(uri);
        //run gateway socket
        Gateway.running();
    }
}
