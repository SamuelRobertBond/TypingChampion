package Server.Utils;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Server;

import Server.Enities.ServerPlayer;
import Server.Listeners.JoinListener;

public class ServerLobbyWorld {
	
	private Server server;
	private HashMap<Integer, ServerPlayer> players;
	
	public ServerLobbyWorld(Server server) {
		
		this.server = server;	
		server.addListener(new JoinListener(players));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
