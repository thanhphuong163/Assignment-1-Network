package Analytic;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.System.out;

public class Database {
    public static void main(String args[]) throws UnknownHostException {
        String serverIP = InetAddress.getLocalHost().getHostAddress();
        out.println(serverIP);
    }
}
