package Analytic;

import Database.Query;
import com.mongodb.client.MongoCollection;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.System.out;

public class ServiceThread extends Thread{
    private Socket clientSocket;
    private MongoCollection collection;
    public ServiceThread(Socket client, MongoCollection collection) {
        this.clientSocket = client;
        this.collection = collection;
    }
    @Override
    public void run() {
        SimpleDateFormat df = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
        try {
            // Receive request from client through socket
            BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            JSONObject json = new JSONObject(new String(is.readLine()));
            String ID = json.getString("ID");
            double Long = json.getDouble("Long");
            double Lat = json.getDouble("Lat");
            Date time = df.parse(json.getString("Time"));
            GPS pos = new GPS(ID,Long,Lat,time);

            // Query data from Database with info request
            Query query = new Query(pos, collection);
            ArrayList<GPS> data = query.getData();

            // Processing
            String w;
            if (data.size() <= 0) {
                w = "There is no data in this area.";
            }
            else {
                Computing process = new Computing(data);
                w = process.processing();
            }
            out.println("Computed data: " + w);
//            String w = "Hello";
//            out.println("ID: " + pos.getID());
//            out.println("Long: " + pos.getLongitude());
//            out.println("Lat: " + pos.getLatitude());
//            out.println("Time: " + pos.getTime().toString());
            // Response
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
        Server.numberOfThread--;
        out.println("Completed processing for request " + clientSocket.getRemoteSocketAddress());
    }

}
