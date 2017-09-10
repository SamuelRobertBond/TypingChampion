package Server.Components;

import com.badlogic.ashley.core.Component;

public class IdComponent implements Component{
	
	public String name;
	public int id;
	
	public IdComponent(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	
	
	
}
