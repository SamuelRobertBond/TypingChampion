package Server.Listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import Client.Requests.MoveRequest;
import Client.Utils.MoveType;
import Server.Enities.ServerPlayer;
import Server.Systems.MoveSystem;

public class MoveListener extends Listener{
	
	private MoveSystem sys;
	
	public MoveListener(MoveSystem sys) {
		this.sys = sys;
	}
	
	@Override
	public void received(Connection connection, Object object) {
		
		if(object instanceof MoveRequest){
			//sys.addAction();
		}
		
	}
	
}
