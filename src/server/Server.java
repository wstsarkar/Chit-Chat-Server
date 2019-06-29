package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import been.MSG;
import controller.MsgController;
import utility.Common;

/**
 * 
 * @author Williyam
 * 
 */
public class Server extends Thread {
	private ServerSocket server;
	protected List<ServerHandler> clients;

	private MsgController msgController;

	public Server() {
		try {
			msgController = new MsgController();
			this.server = new ServerSocket(Common.Port);
			System.out.println("New server initialized!");
			this.clients = Collections.synchronizedList(new ArrayList<ServerHandler>());

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
				readMessage(newClient);
				System.out.println("Client Nubmer :" + clients.size() + " ,Connected :"
						+ client.getInetAddress().getHostAddress());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void readMessage(ServerHandler client) {
		new Thread() {
			public void run() {
				try {
					while (client.isConnected()) {
						MSG msg = (MSG) client.getObjectInputStream().readObject();
						if (msg.getIs_hello_msg() == 1) {
							client.setUserId(msg.getSender_user_id());
							clients.add(client);
							writeAllUnsendMessage(client);
						} else {
							ServerHandler targetClient = null;
							boolean isSend = false;
							for (int i = 0; i < clients.size(); i++) {
								targetClient = clients.get(i);
								if (targetClient.getUserId() == msg.getReceiver_user_id()) {
									writeMessage(targetClient, msg);
									isSend = true;
									break;
								}
							}
							if (!isSend) {
								msg.setIs_send(0);
								msgController.createMsg(msg);
							}
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				if (clients.contains(client)) {
					clients.remove(client);
				}
			}
		}.start();
	}

	public void writeMessage(ServerHandler client, MSG msg) {
		try {
			if (client.isConnected()) {
				client.getObjectOutputStream().writeObject(msg);
				client.getObjectOutputStream().flush();
				msg.setIs_send(1);
				msgController.createMsg(msg);
			}
		} catch (IOException e) {
			msg.setIs_send(0);
			msgController.createMsg(msg);
			e.printStackTrace();
		}
	}

	public void writeAllUnsendMessage(ServerHandler client) {
		List<MSG> msgs = msgController.getAllUnsendMSG(client.getUserId());
		for (int i = 0; i <msgs.size(); i++) {
			MSG msg  = msgs.get(i);
			try {
				if (client.isConnected()) {
					client.getObjectOutputStream().writeObject(msg);
					client.getObjectOutputStream().flush();
					msg.setIs_send(1);
					msgController.createMsg(msg);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
