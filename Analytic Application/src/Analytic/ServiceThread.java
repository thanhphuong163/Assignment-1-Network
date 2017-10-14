package Analytic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServiceThread extends Thread{
    private Socket clientSocket;
    private int numberOfClient;
    public ServiceThread(Socket client, int n) {
        this.clientSocket = client;
        this.numberOfClient = n;
    }
    @Override
    public void run() {
        SimpleDateFormat df = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
        try {
            // Receive request from client through socket
            BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            JSONObject json = new JSONObject(new String(is.readLine()));
            String IP = clientSocket.getRemoteSocketAddress().toString();
            String ID = json.getString("ID");
            double Long = json.getDouble("Long");
            double Lat = json.getDouble("Lat");
            Date time = df.parse(json.getString("Time"));
            GPS pos = new GPS(ID,Long,Lat,time);

            // Query data from Database with info request
            Query query = new Query(pos);
            ArrayList<GPS> data = query.getData();

            // Processing
            Computing process = new Computing(data);
            String w = process.processing();

            // Responsing
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            os.write(w);
            os.newLine();
            os.flush();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.numberOfClient--;
    }
}
