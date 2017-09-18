package Server.Responses;

import Client.Utils.MoveType;

public class AnimationResponse {

	public String name;
	public MoveType move;
	
	public AnimationResponse(String name, MoveType move) {
		this.name = name;
		this.move = move;
	}
	
	public AnimationResponse() {
		// TODO Auto-generated constructor stub
	}
	
	
}
