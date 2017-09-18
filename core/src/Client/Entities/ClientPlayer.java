package Client.Entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Client.Components.AnimationComponent;
import Client.Components.PositionComponent;
import Client.Components.StatsComponent;
import Client.Utils.MoveType;
import Server.Responses.StatResponse;

public class ClientPlayer extends Entity{
	
	private AnimationComponent animationComponent;
	private PositionComponent positionComponent;
	private StatsComponent statsComponent;
	
	public ClientPlayer(String name, float x, float y) {
		
		positionComponent = new PositionComponent(x, y);
		animationComponent = new AnimationComponent();
		statsComponent = new StatsComponent(name);
		
		add(animationComponent);
		add(positionComponent);
		add(statsComponent);
	}
	
	public void addAnimation(MoveType key, Animation<TextureRegion> animation){
		animationComponent.addAnimation(key, animation);
	}
	
	public void updateStats(StatResponse r){
		
		statsComponent.health = r.health;
		statsComponent.energy = r.energy;
		
	}
	
	public void setAnimationState(MoveType type){
		statsComponent.state = type;
	}
	
}
