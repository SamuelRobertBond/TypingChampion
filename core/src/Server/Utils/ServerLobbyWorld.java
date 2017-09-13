package Server.Utils;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Server;

import Server.Enities.ServerPlayer;
import Server.Listeners.JoinRequestListener;
import Server.Listeners.MessageRequestListener;

public class ServerLobbyWorld {
	
	private Server server;
	private HashMap<Integer, ServerPlayer> players;
	
	public ServerLobbyWorld(Server server) {
		
		this.server = server;
		players = new HashMap<Integer, ServerPlayer>();
		
	}
	
	public void addListeners() {
		server.addListener(new MessageRequestListener(server));
		server.addListener(new JoinRequestListener(players, server));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
