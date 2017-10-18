/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkassignment1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

//Receiving message from UDP clients

public class Receiver extends Thread {

    //variable declarations
    byte[] inFromClient;
    byte[] outToClient;
    int clientPort;
    String data;
    boolean key = true;
    Sender sender = new Sender();

    //constructor
    public Receiver() {
    }

    @Override
    public void run() {
        try {
            while (true) {
                //create datagram packet
                inFromClient = new byte[256];
                DatagramPacket fromClient = new DatagramPacket(inFromClient, inFromClient.length);
                //receive data packet from client
                Gateway.ReceiveSocket.receive(fromClient);
                //get data from packet (need more implementation)
                data = (new String(fromClient.getData(), 0, inFromClient.length)).trim();
                //push data to Queue
                Gateway.DataQueue.add(data);
                //feedback to client
                String feedbackMessage = "Gateway received data: " + data;
                System.out.println(data);
                //packaging
                outToClient = feedbackMessage.getBytes();
                //get client's address and port
                InetAddress address = fromClient.getAddress();
                clientPort = fromClient.getPort();
                DatagramPacket toClient = new DatagramPacket(outToClient, outToClient.length, address, clientPort);
                //send to client
                Gateway.ReceiveSocket.send(toClient);
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
