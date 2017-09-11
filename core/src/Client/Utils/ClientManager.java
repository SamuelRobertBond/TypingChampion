package Client.Utils;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;

import Client.Listeners.MessageResponseListener;

public class ClientManager {
	
	private Client client;
	private int id;

	public ClientManager(String name, int tcp, int udp) {
		
		client = new Client();
		GameUtils.serializeKryoObjects(client.getKryo());
		client.start();
		
		addListeners(client);
		
		InetAddress address = client.discoverHost(udp, 300);
		
		//Client Connecting to server
		try {
			client.connect(400, address, tcp);
		} catch (IOException e) {
			Gdx.app.log("ClientManager", "Failed to connect to a client");
			e.printStackTrace();
		}
		
		id = client.getID();
	}
	
	public Client getClient(){
		return client;
	}
	
	private void addListeners(Client client) {
		client.addListener(new MessageResponseListener());
	}
	
}
