package Server.Utils;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;

import Client.Utils.GameUtils;

public class ServerManager {

	private Server server;
	
	public ServerManager(int tcp, int udp) {
		
		server = new Server();
		GameUtils.serializeKryoObjects(server.getKryo());
		
		server.start();
		
		//Binds server
		try {
			server.bind(tcp, udp);
		} catch (IOException e) {
			Gdx.app.log("ServerManager: ", "Failed to bind ports");
			e.printStackTrace();
		}
		
		
		
	}
	
	public Server getServer(){
		return server;
	}
	
}
