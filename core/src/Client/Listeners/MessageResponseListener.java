package Client.Listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import Server.Responses.MessageResponse;

public class MessageResponseListener extends Listener {
	
	public MessageResponseListener() {}
	
	@Override
	public void received(Connection connection, Object object) {
		
		if(object instanceof MessageResponse){
			
			MessageResponse r = (MessageResponse)object;
			
			System.out.print(r.message);
		}
		
	}
	
	
}
