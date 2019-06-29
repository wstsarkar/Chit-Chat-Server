package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import been.MSG;
import utility.Common;


public class Server extends Thread {
	private ServerSocket server;
	protected List<ServerHandler> clients;
	protected BufferedReader console;
	protected String userInput;

	public Server() {
		try {
			this.server = new ServerSocket(Common.Port);
			System.out.println("New server initialized!");
			this.userInput = null;
			this.clients = Collections.synchronizedList(new ArrayList<ServerHandler>());
			

			new Thread() {
				public void run() {
					BufferedReader console = new BufferedReader(
							new InputStreamReader(System.in));
					try {
						while ((userInput = console.readLine()) != null) {
							if (!userInput.isEmpty()) {

								for (ServerHandler t : clients) {
									t.getObjectOutputStream().writeObject("Hello");
									t.getObjectOutputStream().flush();
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			// client reading with accept() method thread start
			
			this.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				Socket client = server.accept();
				ServerHandler newClient = new ServerHandler(client);
				clients.add(newClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
