package Server.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import Client.Requests.ReadyRequest;
import Server.Enities.ServerPlayer;
import Server.Listeners.JoinRequestListener;
import Server.Listeners.MessageRequestListener;
import Server.Responses.StartResponse;

public class ServerLobbyWorld {
	
	private Server server;
	private HashMap<Integer, ServerPlayer> players;
	
	private LinkedList<ServerGameWorld> games;
	
	private Stack<Listener> listeners;
	
	public ServerLobbyWorld(Server server) {
		
		this.server = server;
		players = new HashMap<Integer, ServerPlayer>();
		
		games = new LinkedList<ServerGameWorld>();
		
		listeners = new Stack<Listener>();
		
		//Message Listener
		listeners.push(new MessageRequestListener(server));
		server.addListener(listeners.peek());
		
		//Join Listener
		listeners.push(new JoinRequestListener(players, server));
		server.addListener(listeners.peek());
		
		//Ready Listener
		listeners.push(new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				
				if(object instanceof ReadyRequest){
								
					players.get(connection.getID()).setReady(!players.get(connection.getID()).isReady());
					
					if(players.get(connection.getID()).isReady()){
						startGame(connection);
					}
					
				}
				
			}
			
		});
		server.addListener(listeners.peek());
		
	}
	
	private void startGame(Connection connection){
		
		ServerPlayer player = players.get(connection.getID());
		
		Gdx.app.log("Server Lobby World", player.getName() + " is ready");
		
		//Check for games to start
		for(int key : players.keySet()){
			
			ServerPlayer other = players.get(key);
			
			if(other.isReady() && !other.equals(player)){
				
				ServerPlayer[] matchPlayers = {player, other};
				
				player.setReady(false);
				other.setReady(false);
				
				games.add(new ServerGameWorld(games.size(), matchPlayers, server));
				
				server.sendToTCP(connection.getID(), new StartResponse(other.getName()));
				server.sendToTCP(key, new StartResponse(player.getName()));
				
			}
			
		}
		
		//Check for games to remove
		for(int i = 0; i < games.size(); ++i){
			
			if(games.get(i).isCompleted()){
				games.remove(i);
				--i;
			}
			
		}
		
	}
	
	public void dispose() {
		while(!listeners.isEmpty()){
			server.removeListener(listeners.pop());
		}
	}
	
}
