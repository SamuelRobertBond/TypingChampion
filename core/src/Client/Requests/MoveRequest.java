package Client.Requests;

import Client.Utils.MoveType;

public class MoveRequest {

	public String name;
	public MoveType move;

	public MoveRequest(String name, MoveType move) {
		this.name = name;
		this.move = move;
	}
	
	public MoveRequest() {
		// TODO Auto-generated constructor stub
	}
	
}
