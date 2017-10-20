package com.technotron.trafficjampredictor;

import com.google.common.net.InetAddresses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;

/**
 * Created by lephu on 10/14/2017.
 */

public class TCPClientTask extends Thread {
    private BufferedWriter os = null;
    private BufferedReader is = null;
    private Double chosenLatitude;
    private Double chosenLongitude;
    private Socket socketOfClient = null;

    TCPClientTask (Double lat, Double lng) {
        this.chosenLatitude = lat;
        this.chosenLongitude = lng;
    }

    @Override
    public void run() {
        try {
            InetAddress serverAddr = InetAddress.getByName(MapsActivity.serverIP);
            socketOfClient = new Socket();
            socketOfClient.connect(new InetSocketAddress(serverAddr,9999),10000);
        } catch (UnknownHostException e) {
            MapsActivity.predictionStr = "Unknown host";
            return;
        } catch (IOException e) {
            MapsActivity.predictionStr = "Cannot connect";
            return;
        }
        try {
            os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
            is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
            os.write(new LocationObject(MapsActivity.android_id,chosenLatitude,chosenLongitude).convertToStrings());
            os.newLine();
            os.flush();
            String responseLine;


            //check for response in 5 seconds
            if ((responseLine = is.readLine()) != null) {
                if (responseLine.equals("")) {
                    MapsActivity.predictionStr = "Did not receive prediction";
                } else {
                    MapsActivity.predictionStr = responseLine;
                }
            } else {
                MapsActivity.predictionStr = "Did not receive prediction";
            }


            os.close();
            is.close();
            socketOfClient.close();

        } catch (UnknownHostException e) {
            MapsActivity.predictionStr = "Unknown host";
            return;
        } catch (IOException e) {
            MapsActivity.predictionStr = "Cannot connect";
            return;
        }
    }
}
