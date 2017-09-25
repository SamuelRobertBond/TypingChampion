package Server.Components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component{

	public int health = 100;
	public int maxHealth = 100;
	public int knockouts = 0;
	
}
