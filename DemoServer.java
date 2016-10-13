/* Author: Luigi Vincent
* ACM Networking workshop demo server
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class DemoServer {
	private final static int PORT = 9001;
	private static Set<PrintWriter> clients = Collections.newSetFromMap(new ConcurrentHashMap<PrintWriter, Boolean>());

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(PORT);
		System.out.println("Server online.\nServer IP: " + InetAddress.getLocalHost());

		while (true) {
			new Thread(new ClientHandler(server.accept())).start();
		}
	}

	private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String name;

        ClientHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println("Successfully connected to server!");
				name = in.readLine();
				System.out.println(name + " connected. IP: " + socket.getInetAddress().getHostAddress());
				alertConnect(name + " connected.");
				clients.add(out);

				String input = null;
				while (true) {
					if ((input = in.readLine()) != null) {
						messageAll(input);
					}
				}
			} catch (IOException ioe) {
				clients.remove(out);
				String message = name + " disconnected.";
				System.out.println(message);
				messageAll(message);
			}
		}

		private void messageAll(String message) {
	    	for (PrintWriter client : clients) {
	    		if (client != out) {
	    			client.println(name + ": " + message);
	    		}
	    	}
    	}
    }

    private static void alertConnect(String message) {
    	for (PrintWriter client : clients) {
    		client.println(message);	
    	}
    }
}