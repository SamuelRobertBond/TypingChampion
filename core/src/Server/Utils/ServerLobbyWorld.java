package Server.Utils;

import com.esotericsoftware.kryonet.Server;
import Server.Listeners.JoinListener;

public class ServerLobbyWorld {
	
	private Server server;
	private JoinListener joinListener;
	
	public ServerLobbyWorld(Server server) {
		
		this.server = server;
		joinListener = new JoinListener();
		
		server.addListener(joinListener);
	}
	
	
	
	
	
	
	
	
	
	
	
}
