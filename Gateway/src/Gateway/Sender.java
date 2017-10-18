/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateway;

import org.eclipse.paho.client.mqttv3.MqttException;

//Send data class

public class Sender extends Thread {

    byte[] outToCloud;
    String dataOut;

    public Sender() {
    }

    @Override
    public void run() {
        try {
            while (!Gateway.DataQueue.isEmpty()) {
                dataOut = Gateway.DataQueue.poll();
                Gateway.SendSocket.sendMessage(dataOut);
            }
        } catch (MqttException e) {
            System.err.println(e);
        }
    }
    
}
