package Server.Utils;

import java.io.IOException;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import Client.Utils.GameUtils;
import Server.World.ServerGameWorld;
import Server.World.ServerLobbyWorld;

@SuppressWarnings("unused")
public class ServerManager {

	private Server server;
	private ServerLobbyWorld lobby;
	
	private HashMap<Integer, ServerGameWorld> games;
	
	public ServerManager(int tcp, int udp) {
		
		server = new Server();
		GameUtils.serializeKryoObjects(server.getKryo());		
		server.start();	
		
		games = new HashMap<Integer, ServerGameWorld>();
		
	}
	
	public Server getServer(){
		return server;
	}

	public boolean bind(int tcp, int udp) {
		
		//Binds server
		try {
			server.bind(tcp, udp);
			lobby = new ServerLobbyWorld(server);
		} catch (IOException e) {
			Gdx.app.log("ServerManager: ", "Failed to bind ports");
			return false;
		}
			
		return true;
	}
	
}
