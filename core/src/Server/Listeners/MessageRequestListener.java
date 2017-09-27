package Server.Listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import Client.Requests.MessageRequest;
import Server.Responses.MessageResponse;

public class MessageRequestListener extends Listener {
	
	private Server server;
	
	public MessageRequestListener(Server server) {
		this.server = server;
	}
	
	@Override
	public void received(Connection connection, Object object) {
		
		if(object instanceof MessageRequest){
			MessageRequest r = (MessageRequest)object;
			Gdx.app.log("Server - Message", r.name + ": " + r.message);
			server.sendToAllExceptTCP(connection.getID(), new MessageResponse(r.name, r.message));
		}
		
	}
	
	
}
