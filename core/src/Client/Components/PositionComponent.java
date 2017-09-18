package Client.Components;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component{

	public float x;
	public float y;
	public final float scale = 1;
	public float width;
	public float height;
	
	public PositionComponent(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
}
