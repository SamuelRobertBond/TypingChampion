package Client.Requests;

import Client.Utils.MoveType;

public class MoveRequest {

	public int id;
	public MoveType move;

	public MoveRequest(int id, MoveType move) {
		this.id = id;
		this.move = move;
	}
	
}
