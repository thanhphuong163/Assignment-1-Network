/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkassignment1;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttCallback;

/**
 *
 * @author minhh
 */
public class MQTTCallBackImplement implements MqttCallback{
    static String[] returnMessage = new String[100];
    static int count = 0;
    public MQTTCallBackImplement(){
    
    }
    
    @Override
    public void connectionLost(Throwable cause) {}
            
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        //System.out.println(String.format("[%s] %s", topic, new String(message.getPayload())));
        this.returnMessage[count++] = new String(message.getPayload());
        //System.out.println(returnMessage[count-1]);
        
    }
            
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {}
    
    
}
