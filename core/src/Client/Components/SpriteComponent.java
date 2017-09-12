package Client.Components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

import Client.Utils.SpriteType;

public class SpriteComponent implements Component{

	private HashMap<SpriteType, Sprite> spritemap;
	
	public SpriteComponent() {
		spritemap = new HashMap<SpriteType, Sprite>();
	}
	
	public void addSprite(SpriteType type, Sprite sprite){
		spritemap.put(type, sprite);
	}
	
}
