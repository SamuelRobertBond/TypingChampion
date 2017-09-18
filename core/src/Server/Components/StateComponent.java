package Server.Components;

import com.badlogic.ashley.core.Component;

import Client.Utils.MoveType;
import Server.Utils.PlayerState;

public class StateComponent implements Component{
	
	public PlayerState state;
	public MoveType move;
	
	public StateComponent(PlayerState state) {
		this.state = state;
		move = MoveType.Idle;
	}

}
