package Server.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import Client.Requests.KOWordRequest;
import Client.Requests.MoveRequest;
import Client.Requests.WordSubmissionRequest;
import Server.Components.HealthComponent;
import Server.Components.StateComponent;
import Server.Enities.ServerPlayer;
import Server.Responses.WordSubmissionResponse;
import Server.Utils.PlayerState;
import Server.Utils.WordUtil;

public class KnockoutSystem extends EntitySystem {
	
	private ComponentMapper<StateComponent> sm = ComponentMapper.getFor(StateComponent.class);
	private ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
	
	private Server server;
	private ServerPlayer player;
	
	private HealthComponent hc;
	private StateComponent sc;
	
	private float timeElapsed;
	private Listener koListener;
	
	private int wordsCorrect;
	private boolean knockedOut = false;
	
	private String currentWord;
	
	public KnockoutSystem(Server server, ServerPlayer player) {
		
		this.player = player;
		this.server = server;
		
		sc = sm.get(player);
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
	public void update(float deltaTime) {
		
		if(timeElapsed < 10) { 
			timeElapsed += deltaTime * .001;
		}else{
			knockedOut = true;
		}
		
		Gdx.app.log("Knockout System", "Countdown: " + timeElapsed);
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
