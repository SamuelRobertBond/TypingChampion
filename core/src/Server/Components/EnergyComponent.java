package Server.Components;

import com.badlogic.ashley.core.Component;

public class EnergyComponent implements Component{

	public final int MAX_ENERGY = 100;
	public final int ENERGY_PER_LETTER = 2;
	public int energy = 50;
	
}
