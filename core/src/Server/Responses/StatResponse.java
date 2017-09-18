package Server.Responses;

import Client.Utils.MoveType;
import Client.Utils.Role;

public class StatResponse {

	public Role role;
	public int health;
	public int energy;
	public MoveType move;
	
	public StatResponse(Role role, int health, int energy, MoveType move) {
		
		this.role = role;
		this.health = health;
		this.energy = energy;
		this.move = move;
		
	}
	
	public StatResponse(){
		
	}
	
	
}
