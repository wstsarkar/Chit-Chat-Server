package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler {
	protected Socket client;
	private ObjectOutputStream oos;

	public ObjectOutputStream getObjectOutputStream() {
		return oos;
	}

	public ServerHandler(Socket client) {
		this.client = client;
		try {
			this.oos = new ObjectOutputStream(this.client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
