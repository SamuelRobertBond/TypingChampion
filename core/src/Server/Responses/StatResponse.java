package Server.Responses;

import Server.Utils.PlayerState;

public class StatResponse {

	public int health;
	public int energy;
	public PlayerState yourState;
	public PlayerState enemyState;
	
	public StatResponse(int health, int energy, PlayerState yourState, PlayerState enemyState) {
		
		this.health = health;
		this.energy = energy;
		
		this.yourState = yourState;
		this.enemyState = enemyState;
		
	}
	
	public StatResponse(){
		
	}
	
	
}
