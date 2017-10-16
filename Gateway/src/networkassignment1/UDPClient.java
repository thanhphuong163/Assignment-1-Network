/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package networkassignment1;

import java.io.DataInputStream; 
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException;
import java.net.UnknownHostException; 

/**
 *
 * @author minhh
 */
public class UDPClient {
    public static int count = 0;
    public void test () throws SocketException{
        //tao moi socket client 
        DatagramSocket ClientSocket = new DatagramSocket();
        System.out.println("Connected to server"); 

        //nhap yeu cau tu nguoi dung 
        DataInputStream inFromUser = new DataInputStream(System.in); 
        int a,b,tong; 
        try 
        { 
//            System.out.println("Nhap vao so a :"); 
//            a = Integer.parseInt(inFromUser.readLine()); 
//            System.out.println("Nhap vao so b :"); 
//            b = Integer.parseInt(inFromUser.readLine()); 
            //Khai bao mang byte de chua du lieu gui di server 
            byte outToServer1[]; 
            //chuyen kieu du lieu : int -> String 
            String s1 = String.valueOf(count++); 
            //chuyen kieu du lieu : String -> byte va dua vao mang byte da khai bao o tren 
            outToServer1 = s1.getBytes(); 
            System.out.println(s1);
            //lay kich thuoc cua mang 
            int leng1 = outToServer1.length; 
            //dia chi may chu 
            InetAddress address = InetAddress.getByName("Localhost"); 
            // so port 
            int port = 1024; 
            // tao goi de gui di 
            DatagramPacket toServer1 = new DatagramPacket(outToServer1, leng1, address, port); 
            // gui goi len server 
            ClientSocket.send(toServer1); 
            //tao goi de nhan du lieu ve 
            byte inFromServer[]; 
            inFromServer = new byte[256]; 
             

            //kich thuoc mang nhan du lieu ve 
            leng1 = inFromServer.length; 
             
            // tao goi nhan du lieu ve 
            DatagramPacket fromServer = new DatagramPacket(inFromServer, leng1); 
             
            // nhan goi tra ve tu server 
            ClientSocket.receive(fromServer); 
             
            //khai bao bien de chuyen tu kieu byte sang kieu String 
            String data; 
            // dua du lieu tu mang byte vao bien data, lay tu vi tri so 0. 
            data = (new String(fromServer.getData(),0,fromServer.getLength())).trim(); 
            //in ket qua ra man hinh 
            System.out.println("Ket Qua :"+data); 
            ClientSocket.close(); 
             
        }catch (UnknownHostException e) 
        { 
            System.out.println("Server Not Found"); 
            System.exit(1); 
        }catch (IOException e) 
        { 
            System.out.println("Cannot connect to server"); 
            System.exit(1); 
        } 

         
    }
    public static void main(String []args) throws IOException 
    { 
        int i = 0;
        UDPClient client;
        while(i++<100){
            client = new UDPClient();
            client.test();
            
        }
    } 

}
