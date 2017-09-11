package Server.Listeners;

import com.esotericsoftware.kryonet.Connection;
import Client.Requests.JoinRequest;
import com.esotericsoftware.kryonet.Listener;

public class JoinListener extends Listener {

	@Override
	public void received(Connection connection, Object object) {
		
		if(object instanceof JoinRequest){
			//sys.addAction();
		}
		
	}
	
}
