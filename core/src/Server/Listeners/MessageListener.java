package Server.Listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import Client.Requests.MessageRequest;
import Server.Responses.MessageResponse;

public class MessageListener extends Listener {
	
	private Server server;
	
	public MessageListener(Server server) {
		this.server = server;
	}

	@Override
	public void received(Connection connection, Object object) {
		
		System.out.println("This");
		if(object instanceof MessageRequest){
			
			MessageRequest r = (MessageRequest)object;
			server.sendToAllExceptTCP(connection.getID(), new MessageResponse(r.message));
		}
		
	}
	
	
}
