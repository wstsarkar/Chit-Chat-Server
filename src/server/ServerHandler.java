package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 
 * @author Williyam
 * 
 */
public class ServerHandler {
	protected Socket client;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int userId = 0;

	public ObjectOutputStream getObjectOutputStream() {
		return oos;
	}

	public ObjectInputStream getObjectInputStream() {
		return ois;
	}
	
	public boolean isConnected(){
		return this.client.isConnected();
	}
	
	public boolean isClosed(){
		return this.client.isClosed();
	}	
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public ServerHandler(Socket client) {
		this.client = client;
		try {
			this.oos = new ObjectOutputStream(this.client.getOutputStream());
			this.ois = new ObjectInputStream(this.client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
