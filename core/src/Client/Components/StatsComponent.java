package Client.Components;

import com.badlogic.ashley.core.Component;

import Client.Utils.MoveType;

public class StatsComponent implements Component{

	public int health = 100;
	public int energy = 100;
	public final int MAX_HEALTH = 100;
	public final int MAX_ENERGY = 100;
	public MoveType state;
	public String name;
	
	public StatsComponent(String name) {
		this.name = name;
		this.state = MoveType.Idle;
	}
	
}
