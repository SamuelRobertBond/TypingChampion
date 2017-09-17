package Client.Components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Client.Utils.MoveType;
import Client.Utils.SpriteType;

public class AnimationComponent implements Component{

	public float stateTime;
	private HashMap<MoveType, Animation<TextureRegion>> animations;
	
	public AnimationComponent() {
		this.animations = new HashMap<MoveType, Animation<TextureRegion>>();
		stateTime = 0;
	}
	
	public void addAnimation(MoveType key, Animation<TextureRegion> animation){
		animations.put(key, animation);
	}
	
	public Animation<TextureRegion> getAnimation(MoveType type){
		return animations.get(type);
	}
	
}
