package Client.Components;

import com.badlogic.ashley.core.Component;

import Client.Utils.MoveType;

public class StatsComponent implements Component{

	public float health = 100;
	public float energy = 50;
	public final float MAX_HEALTH = 100;
	public final float MAX_ENERGY = 100;
	public MoveType state;
	public String name;
	
	public StatsComponent(String name) {
		this.name = name;
		this.state = MoveType.Idle;
	}
	
}
