package Client.Listeners;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import Client.Entities.ClientPlayer;
import Server.Responses.JoinResponse;

public class JoinResponseListener extends Listener{
	
	HashMap<String, ClientPlayer> players;
	
	public JoinResponseListener(HashMap<String, ClientPlayer> players) {
		this.players = players;
	}
	
	@Override
	public void received(Connection connection, Object object) {
		if(object instanceof JoinResponse){
			JoinResponse r = (JoinResponse)object;
			
			if(r.success){
				players.put(r.name, new ClientPlayer());
			}else{
				Gdx.app.log("Join Response Listener: ", "Player Limit Reached: Failed to add player");
			}
			
		}
	}
	
}
