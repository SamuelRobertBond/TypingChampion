package Server.Utils;

import com.esotericsoftware.kryonet.Server;

import Server.Enities.ServerPlayer;

public class ServerGameWorld {

	private boolean completed;
	
	public ServerGameWorld(int size, ServerPlayer[] matchPlayers, Server server) {
		
		completed = false;
		
	}
	
	public boolean isCompleted(){
		return completed;
	}

}
