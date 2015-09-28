package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import javax.swing.JLabel;

import Client.GClient;

public class GameServer {
	
	private ServerSocket ss;
	
	private int port;
	
	public GameServer() {
		port = 5000;
		int ClientNums = 2;
		Socket socket = null;

		try {
			ss = new ServerSocket(port);					
		} catch(IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		}		
		

        while (true) {
            try {
                socket = ss.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
//            Thread g = new Thread(new GClient(socket, ClientNums));
//            g.start();
            System.out.println(ClientNums);
            ClientNums--;
        }
	}

	public static void main(String [] args) {
		GameServer gs = new GameServer();
	}

//end of GameServer class
}
