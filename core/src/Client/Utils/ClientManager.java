package Client.Utils;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;

import Client.Listeners.MessageResponseListener;

public class ClientManager {
	
	private Client client;
	public String name;

	public ClientManager(String name) {
		
		client = new Client();
		GameUtils.serializeKryoObjects(client.getKryo());
		client.start();
		
		this.name = name;
		
	}
	
	public boolean connectLan(String name, int tcp, int udp){
		
		InetAddress address = client.discoverHost(udp, 300);
		
		try {
			client.connect(5000, address, tcp, udp);
		} catch (IOException e) {
			Gdx.app.log("ClientManager", "Failed to connect to a client");
			return false;
		}
		
		this.name = name;
		
		return true;
		
	}
	
	public boolean connectNet(String name, String address, int tcp, int udp){
		
		try {
			client.connect(5000, address, tcp, udp);
		} catch (IOException e) {
			Gdx.app.log("ClientManager", "Failed to connect to a client");
			e.printStackTrace();
			return false;
		}
		
		this.name = name;
		return true;
		
	}

	public Client getClient(){
		return client;
	}
	
}
