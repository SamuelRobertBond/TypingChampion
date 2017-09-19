package Client.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class UIComponent implements Component{

	public float MAX_BAR_WIDTH;
	
	public Sprite healthSprite;
	public Sprite healthFrameSprite;
	
	public Sprite energySprite;
	public Sprite energyFrameSprite;
	
	public UIComponent(Sprite healthSprite, Sprite energySprite, Sprite healthFrameSprite, Sprite energyFrameSprite) {

		MAX_BAR_WIDTH = healthSprite.getWidth();
		this.healthSprite = healthSprite;
		this.healthFrameSprite = healthFrameSprite;
		this.energySprite = energySprite;
		this.energyFrameSprite = energyFrameSprite;
	}

	
}
