package com.technotron.trafficjampredictor;

import com.google.common.net.InetAddresses;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by lephu on 10/14/2017.
 */

public class UDPClientTask extends Thread {
    private DatagramSocket socket;
    private int timer;

    UDPClientTask(int timer) {
        this.timer = timer;
    }

    UDPClientTask() {
        this.timer = 5;
    }


    @Override
    public void run() {
        try {
            socket = new DatagramSocket(1024);
            while (MapsActivity.isSendingData) {
                String message = new LocationObject(MapsActivity.android_id, MapsActivity.myLatitude,
                        MapsActivity.myLongitude).convertToStrings();

                InetAddress ia = InetAddress.getByName(MapsActivity.serverIP);

                DatagramPacket dpSend = new DatagramPacket(message.getBytes(), message.getBytes().length, ia, 1024);
                socket.send(dpSend);

                //stop button not pressed
                if (MapsActivity.isSendingData) {
                    this.sleep(timer * 1000);
                }
                else {
                    break;
                }
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
