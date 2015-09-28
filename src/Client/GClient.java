package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GClient implements Runnable {

    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private int ClientNums;

    public GClient(Socket socket, int ClientNums) {
        this.socket = socket;
        this.ClientNums = ClientNums;
    }

	public void run() {
		try {
			pw = new PrintWriter(socket.getOutputStream());
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			br = new BufferedReader(isr);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		
//		pw.println("heihei i'm here Client" + " " + ClientNums);
//		pw.flush();

//		try {
//			String line = br.readLine();
//			System.out.println(line);
//		} catch (IOException ioe) {
//			System.out.println(ioe.getMessage());
//		}
	}
}
