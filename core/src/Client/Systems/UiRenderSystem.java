package Client.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Client.Components.StatsComponent;
import Client.Components.UIComponent;
import Client.Entities.ClientPlayer;

public class UiRenderSystem extends EntitySystem{

	private ComponentMapper<StatsComponent> sm = ComponentMapper.getFor(StatsComponent.class);
	private ComponentMapper<UIComponent> um = ComponentMapper.getFor(UIComponent.class);
	
	private UIComponent uc;
	private StatsComponent sc;
	
	private SpriteBatch batch;
	
	public UiRenderSystem(ClientPlayer player) {
		batch = new SpriteBatch();
		uc = um.get(player);
		sc = sm.get(player);
	}
	
	@Override
	public void update(float deltaTime) {
		
		uc.healthSprite.setSize(uc.MAX_BAR_WIDTH * sc.health/sc.MAX_HEALTH, uc.healthSprite.getHeight());
		uc.energySprite.setSize(uc.MAX_BAR_WIDTH * sc.energy/sc.MAX_ENERGY, uc.energySprite.getHeight());
		
		batch.begin();
		
		uc.energyFrameSprite.draw(batch);
		uc.energySprite.draw(batch);
		
		uc.healthFrameSprite.draw(batch);
		uc.healthSprite.draw(batch);
		
		batch.end();
		
	}
	
}
