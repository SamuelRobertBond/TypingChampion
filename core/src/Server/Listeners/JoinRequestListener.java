package Server.Listeners;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import Client.Requests.JoinRequest;
import Client.Requests.MessageRequest;
import Server.Enities.ServerPlayer;
import Server.Responses.JoinResponse;
import Server.Responses.MessageResponse;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class JoinRequestListener extends Listener {
	
	private Server server;
	private HashMap<Integer, ServerPlayer> players;
	
	public JoinRequestListener(HashMap<Integer, ServerPlayer> players, Server server) {
		this.players = players;
		this.server = server;
	}

	@Override
	public void received(Connection connection, Object object) {
		
		if(object instanceof JoinRequest){
			
			JoinRequest r = (JoinRequest)object;
			
			Gdx.app.log("Join Request Listener: ", r.name + " has joined the game");
			
			if(players.size() < 2){
				
				if(addPlayer(connection, r.name)){
					
					for(int key : players.keySet()){
						server.sendToTCP(connection.getID(), new JoinResponse(players.get(key).getName(), true));
					}
					
					server.sendToAllTCP(new JoinResponse(r.name, true));
					server.sendToAllTCP(new MessageResponse(r.name.toUpperCase(), " HAS JOINED THE GAME"));
					
				}
				
			}else{
				server.sendToTCP(connection.getID(), new JoinResponse(r.name, false));
			}
		}
		
	}
	
	private boolean addPlayer(Connection connection, String name) {
		
		if(!players.containsKey(connection.getID())){
			players.put(connection.getID(), new ServerPlayer(name, connection));
			return true;
		}
		
		return false;
	}
	
}
