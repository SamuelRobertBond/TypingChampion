package Server.Listeners;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import Client.Requests.JoinRequest;
import Server.Enities.ServerPlayer;

import com.esotericsoftware.kryonet.Listener;

public class JoinListener extends Listener {

	
	
	public JoinListener(HashMap<Integer, ServerPlayer> players) {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void received(Connection connection, Object object) {
		
		if(object instanceof JoinRequest){
			
		}
		
	}
	
}
