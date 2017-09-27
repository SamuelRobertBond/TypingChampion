package Server.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import Client.Requests.WordSubmissionRequest;
import Server.Components.HealthComponent;
import Server.Components.IdComponent;
import Server.Enities.ServerPlayer;
import Server.Responses.KOResponse;
import Server.Responses.KOUpdateResponse;
import Server.Responses.WordSubmissionResponse;
import Server.Utils.WordUtil;

public class KnockoutSystem extends EntitySystem {
	
	private ComponentMapper<IdComponent> im = ComponentMapper.getFor(IdComponent.class);
	private ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
	
	private ImmutableArray<Entity> players;
	
	private Server server;
	private ServerPlayer player;
	
	private HealthComponent hc;
	
	private float timeElapsed;
	private Listener koListener;
	
	private int wordsCorrect;
	private boolean knockedOut = false;
	
	private String currentWord;
	
	public KnockoutSystem(Server server, ServerPlayer player) {
		
		this.player = player;
		this.server = server;
		
		hc = hm.get(player);
		
		timeElapsed = 0;
		wordsCorrect = 0;
		
		currentWord = WordUtil.getWord(hc.knockouts + 3);
		server.sendToTCP(player.getID(), new WordSubmissionResponse(true, currentWord));
		
		koListener = new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				
				if(object instanceof WordSubmissionRequest){
					WordSubmissionRequest r = (WordSubmissionRequest)object;
					processWord(r, connection);
				}
				
			}

			
			
		};
		server.addListener(koListener);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		players = engine.getEntities();
		
		for(Entity entity : players){
			IdComponent ic = im.get(entity);
			server.sendToTCP(ic.id, new KOResponse(player.getName(), true));
		}
	}
	
	@Override
	public void update(float deltaTime) {
		
		if(timeElapsed < 10) {
			
			timeElapsed += deltaTime;
			int koTime = (int)Math.ceil(timeElapsed);
			
			if(koTime > 10){
				koTime = 10;
			}
			
			for(Entity p : players){
				IdComponent ic = im.get(p);
				server.sendToTCP(ic.id, new KOUpdateResponse(koTime));
			}
			
		}else{
			timeElapsed = 10;
			knockedOut = true;
		}
		
	}
	
	public void dispose(){
		server.removeListener(koListener);
	}
	
	private void processWord(WordSubmissionRequest r, Connection connection) {
		
		if(currentWord.toLowerCase().equals(r.word.toLowerCase())){
			++wordsCorrect;
			currentWord = WordUtil.getWord(hc.knockouts + 3);
			server.sendToTCP(connection.getID(), new WordSubmissionResponse(true, currentWord));
		}else{
			server.sendToTCP(connection.getID(), new WordSubmissionResponse(false, null));
		}
		
	}
	
	public boolean knockedOut(){
		return knockedOut;
	}

	public boolean hasCompleted() {
		return wordsCorrect >= 10;
	}

	public Entity getPlayer() {
		return player;
	}
	
	
}
