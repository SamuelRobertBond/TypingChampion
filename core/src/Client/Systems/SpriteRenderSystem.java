package Client.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import Client.Components.AnimationComponent;
import Client.Components.PositionComponent;
import Client.Components.StatsComponent;
import Client.Utils.MoveType;

public class SpriteRenderSystem extends EntitySystem{
	
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
	private ImmutableArray<Entity> entities;
	
	private StretchViewport view;
	private SpriteBatch batch;
	
	public SpriteRenderSystem(String name, StretchViewport view) {
		batch = new SpriteBatch();
		this.view = view;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(AnimationComponent.class, PositionComponent.class, StatsComponent.class).get());
	}
	
	@Override
	public void update(float deltaTime) {
		
		batch.setProjectionMatrix(view.getCamera().combined);
		batch.begin();
		
		
		for(Entity entity : entities){
			
			PositionComponent pc = pm.get(entity);
			AnimationComponent ac = am.get(entity);
			
			TextureRegion frame = ac.getAnimation(ac.move).getKeyFrame(ac.stateTime);
			
			ac.stateTime += deltaTime;
			
			if(ac.stateTime > .75f && ac.move != MoveType.Dead){
				ac.stateTime = 0;
				ac.move = MoveType.Idle;
			}
			
			batch.draw(frame, pc.x, pc.y, pc.width, pc.height);
		}
		
		batch.end();
		
	}
	
	
	
}
