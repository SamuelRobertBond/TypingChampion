package Client.Components;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component{

	public float x;
	public float y;
	public final float scale = 1;
	public int xScale;
	public int yScale;
	
	public PositionComponent(float x, float y) {
		this.x = x;
		this.y = y;
		this.xScale = 1;
		this.yScale = 1;
	}
	
}
