package Client.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Client.Components.AnimationComponent;
import Client.Components.PositionComponent;
import Client.Components.StatsComponent;

public class SpriteRenderSystem extends EntitySystem{
	
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<StatsComponent> sm = ComponentMapper.getFor(StatsComponent.class);
	private ImmutableArray<Entity> entities;
	
	private SpriteBatch batch;
	private String name;
	
	public SpriteRenderSystem(String name) {
		batch = new SpriteBatch();
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(AnimationComponent.class, PositionComponent.class, StatsComponent.class).get());
	}
	
	@Override
	public void update(float deltaTime) {
		
		batch.begin();
		
		for(Entity entity : entities){
			
			PositionComponent pc = pm.get(entity);
			AnimationComponent ac = am.get(entity);
			StatsComponent sc = sm.get(entity);
			
			TextureRegion frame = ac.getAnimation(sc.state).getKeyFrame(ac.stateTime);
			
			ac.stateTime += deltaTime;
			
			if(ac.stateTime > 200){
				ac.stateTime = 0;
			}
			
			batch.draw(frame, pc.x, pc.y, pc.scale * 32, pc.scale * 32);
		}
		
		batch.end();
		
	}
	
	
	
}
