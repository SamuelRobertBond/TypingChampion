package Server.Utils;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;

import Client.Listeners.MessageResponseListener;
import Client.Utils.GameUtils;
import Server.Listeners.JoinRequestListener;
import Server.Listeners.MessageRequestListener;

public class ServerManager {

	private Server server;
	private ServerLobbyWorld world;
	
	public ServerManager(int tcp, int udp) {
		
		server = new Server();
		GameUtils.serializeKryoObjects(server.getKryo());		
		server.start();	
		
	}
	
	public Server getServer(){
		return server;
	}

	public boolean bind(int tcp, int udp) {
		
		//Binds server
		try {
			server.bind(tcp, udp);
		} catch (IOException e) {
			Gdx.app.log("ServerManager: ", "Failed to bind ports");
			return false;
		}
				
		ServerLobbyWorld world = new ServerLobbyWorld(server);
		world.addListeners();
		
		return true;
	}
	
	
}
