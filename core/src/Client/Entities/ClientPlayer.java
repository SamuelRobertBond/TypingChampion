package Client.Entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Client.Components.AnimationComponent;
import Client.Components.PositionComponent;
import Client.Components.StatsComponent;
import Client.Components.UIComponent;
import Client.Utils.MoveType;
import Server.Responses.StatResponse;

public class ClientPlayer extends Entity{
	
	public AnimationComponent animationComponent;
	public UIComponent uiComponent;
	private PositionComponent positionComponent;
	private StatsComponent statsComponent;

	public ClientPlayer(String name, float x, float y, float width, float height) {
		
		positionComponent = new PositionComponent(x, y, width, height);
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
		
		Gdx.app.log("Client Player", "( " +statsComponent.health + ", " + statsComponent.energy + ")"); 
		
	}
	
	public void setAnimationState(MoveType type){
		statsComponent.state = type;
	}

	public void addUI(UIComponent ui) {
		this.uiComponent = ui;
		add(ui);
	}
	
	public void dispose(){
		//Add Garbage Collection here
	}
	
}
