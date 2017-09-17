package Client.Entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Client.Components.AnimationComponent;
import Client.Utils.MoveType;

public class ClientPlayer extends Entity{
	
	
	private AnimationComponent animationComponent;
	
	public ClientPlayer() {
		animationComponent = new AnimationComponent();
	}
	
	public void addAnimation(MoveType key, Animation<TextureRegion> animation){
		animationComponent.addAnimation(key, animation);
	}
	
}
