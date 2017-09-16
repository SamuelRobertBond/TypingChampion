package Server.Components;

import com.badlogic.ashley.core.Component;

import Server.Utils.PlayerState;

public class StateComponent implements Component{
	
	public PlayerState state;	
	
	public StateComponent(PlayerState state) {
		this.state = state;
	}

}
