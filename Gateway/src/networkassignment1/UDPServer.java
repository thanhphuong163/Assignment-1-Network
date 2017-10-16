/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkassignment1;


import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
/**
 *
 * @author minhh
 */
public class UDPServer {
    public static void main(String []args) throws IOException 
    { 
        // tao ket noi 
        DatagramSocket serverSocket = new DatagramSocket(1024); 
        //thong bao server da san sang ket noi 
        System.out.println("Server is now already"); 
        //tao mang byte de chua du lieu gui len tu client 
        byte inFromClient1[]; 
        inFromClient1 = new byte[256]; 
        byte inFromClient2[]; 
        inFromClient2 = new byte[256]; 
        // lay kich thuoc mang 
        int leng1 = inFromClient1.length; 
        int leng2 = inFromClient2.length; 
        // tao goi de nhan du lieu gui len tu client 
        DatagramPacket fromClient1 = new DatagramPacket(inFromClient1, leng1); 
        DatagramPacket fromClient2 = new DatagramPacket(inFromClient2, leng2); 
        // nhan goi ve server 
        serverSocket.receive(fromClient1); 
        serverSocket.receive(fromClient2); 
        // tao bien data kieu string de lay du lieu trong goi ra 
        String data1,data2; 
        // lay du lieu vao bien data 
        data1 = (new String(fromClient1.getData(),0,inFromClient1.length)).trim(); 
        data2 = (new String(fromClient2.getData(),0,inFromClient2.length)).trim(); 
        // chuyen du lieu tu kieu String -> integer 
        int a,b,tong; 
        a = Integer.parseInt(data1); 
        b = Integer.parseInt(data2); 
        // xu ly yeu cau 
        tong = a + b; 
        //chuyen du lieu tu kieu int -> String va truyen vao bien data 
        data1 = String.valueOf(tong); 
        // dong goi ket qua 
        byte outToClient[]; 
        outToClient = data1.getBytes(); 
        //lay kich thuoc mang 
        leng1 = outToClient.length; 
        //lay dia chi cua may khach, no nam luon trong goi ma da gui len server 
        InetAddress address = fromClient1.getAddress(); 
        // lay so port 
        int port = fromClient1.getPort(); 
        // tao goi de gui ve client 
        DatagramPacket toClient = new DatagramPacket(outToClient, leng1, address, port); 
        //gui goi ve client 
        serverSocket.send(toClient); 
        //dong socket 
        serverSocket.close(); 
    } 

}
