package Server.Utils;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Server;

import Server.Enities.ServerPlayer;
import Server.Listeners.JoinListener;
import Server.Listeners.MessageListener;

public class ServerLobbyWorld {
	
	private Server server;
	private HashMap<Integer, ServerPlayer> players;
	
	public ServerLobbyWorld(Server server) {
		
		this.server = server;
		players = new HashMap<Integer, ServerPlayer>();
		
	}
	
	public void addListeners() {
		server.addListener(new MessageListener(server));
		server.addListener(new JoinListener(players, server));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
