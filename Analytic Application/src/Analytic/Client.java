package Analytic;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.out;

public class Client {
    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
        String ID = "43125425a3";
        double Lat = 10.7696634543665;
        double Long = 106.6584585345325;
        Date time = df.parse("16-10-2017 09:19:30");

        GPS pos = new GPS(ID,Long,Lat,time);
        Socket serverSocket = new Socket("localhost",9999);
        BufferedWriter os = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
        os.write(pos.toJson().toString());
        os.newLine();
        os.flush();
        os.close();
        BufferedReader is = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        out.println(is.readLine());
    }
}