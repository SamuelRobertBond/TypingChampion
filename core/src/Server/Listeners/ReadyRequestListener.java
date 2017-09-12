package Server.Listeners;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import Client.Requests.ReadyRequest;
import Server.Enities.ServerPlayer;
import Server.Responses.StartResponse;

public class ReadyRequestListener extends Listener{

	private HashMap<Integer, ServerPlayer> players;
	private Server server;
	
	public ReadyRequestListener(HashMap<Integer, ServerPlayer> players, Server server) {
		this.players = players;
		this.server = server;
	}
	
	@Override
	public void received(Connection connection, Object object) {
		
		if(object instanceof ReadyRequest){
			
			players.get(connection.getID()).setReady(!players.get(connection.getID()).isReady());
			
			boolean startGame = true;
			
			for(int key : players.keySet()){
				players.get(key);
				if(!players.get(key).isReady()){
					startGame = false;
				}
			}
			
			if(startGame){
				server.sendToAllTCP(new StartResponse());
			}
			
		}
		
	}
	
}
