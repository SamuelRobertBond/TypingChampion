package Client.Utils;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;

public class ClientManager {
	
	private int id;

	public ClientManager(String name, int tcp, int udp) {
		
		Client client = new Client();
		GameUtils.serializeKryoObjects(client.getKryo());
		client.start();
		
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
	
}
