package Server.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import Client.Requests.KOWordRequest;
import Client.Requests.MoveRequest;
import Server.Enities.ServerPlayer;

public class KnockoutSystem extends EntitySystem {
	
	private Server server;
	private ServerPlayer player;
	private float timeElapsed;
	private Listener koListener;
	
	public KnockoutSystem(Server server, ServerPlayer player) {
		
		this.timeElapsed = 0;
		this.server = server;
		
		koListener = new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				
				if(object instanceof KOWordRequest){
					KOWordRequest r = (KOWordRequest)object;
				}
				
			}
			
		};
		server.addListener(koListener);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
	}
	
	@Override
	public void update(float deltaTime) {
		
		if(timeElapsed < 10) { 
			timeElapsed += deltaTime;
		}
		Gdx.app.log("Knockout System", "Countdown: " + timeElapsed);
	}
	
	
}
