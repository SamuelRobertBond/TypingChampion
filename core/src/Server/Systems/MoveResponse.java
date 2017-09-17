package Server.Systems;

import Client.Utils.MoveType;
import Client.Utils.Role;

public class MoveResponse {

	public Role role;
	public MoveType move;
	
	public MoveResponse(MoveType move, Role role) {
		this.role = role;
		this.move = move;
	}
	
	public MoveResponse(){
		
	}
	
}
