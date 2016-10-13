/* Author: Luigi Vincent
* ACM Networking workshop demo client
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DemoClient {
	private final static String ADDRESS = "localhost";
	private final static int PORT = 9001;

	public static void main(String[] args) throws Exception {
		Scanner keyboard = new Scanner(System.in);
		Socket socket = new Socket(ADDRESS, PORT);

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	   
		String serverMessage = null;
		if ((serverMessage = in.readLine()) != null) {
			System.out.println(serverMessage);
			System.out.print("Enter your name: ");
			out.println(keyboard.nextLine());
		}

		new Thread(() -> {
			String input = null;
			while (true) {
				try {
					if ((input = in.readLine()) != null) {
						System.out.println(input);
					}
				} catch (IOException ioe) {
					// no time for this
				}
			}
		}).start();

		while (true) {
			out.println(keyboard.nextLine());
		}
	}
}