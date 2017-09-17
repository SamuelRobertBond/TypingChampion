package Server.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;

import Server.Components.EnergyComponent;
import Server.Components.HealthComponent;
import Server.Enities.ServerPlayer;

public class KnockoutSystem extends EntitySystem {
	
	private Server server;
	private ImmutableArray<Entity> entities;
	private ServerPlayer player;
	
	public KnockoutSystem(Server server, ServerPlayer player) {
		Gdx.app.log("KO", "We have a knockout\n");
		this.server = server;
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(EnergyComponent.class, HealthComponent.class).get());
	}
	
	@Override
	public void update(float deltaTime) {
		
	}
	
	
}
