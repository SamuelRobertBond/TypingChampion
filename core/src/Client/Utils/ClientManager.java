package Client.Utils;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;

import Client.Listeners.MessageResponseListener;

public class ClientManager {
	
	private Client client;
	public String name;

	public ClientManager(String name, int tcp, int udp) {
		
		client = new Client();
		GameUtils.serializeKryoObjects(client.getKryo());
		client.start();
		
		InetAddress address = client.discoverHost(udp, 300);
		
		//Client Connecting to server
		try {
			client.connect(400, address, tcp, udp);
		} catch (IOException e) {
			Gdx.app.log("ClientManager", "Failed to connect to a client");
			e.printStackTrace();
		}
		
		this.name = name;
	}
	
	public Client getClient(){
		return client;
	}
	
}
