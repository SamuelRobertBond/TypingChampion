package Server.World;

import java.util.Stack;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;

import java.util.Timer;
import java.util.TimerTask;

import Client.Requests.StartMatchRequest;
import Server.Components.IdComponent;
import Server.Components.StateComponent;
import Server.Enities.ServerPlayer;
import Server.Responses.WordSubmissionResponse;
import Server.Systems.MoveSystem;
import Server.Systems.WordSystem;

public class ServerGameWorld extends EntitySystem{

	private Server server;
	
	private Engine engine;
	
	private WordSystem wordSystem;
	private MoveSystem moveSystem;
	private Timer update;
	private final long TIME_STEP = 100; //Determines how often to fire the timer task in ms
	
	private ServerPlayer[] players;
	private boolean completed;
	
	private int startRequests = 0;
	
	private Stack<Listener> listeners;
	
	
	public ServerGameWorld(int id, ServerPlayer[] matchPlayers, Server server) {
		
		Gdx.app.log("Server Game World", "Game World Created");
		
		engine = new Engine();
		this.server = server;
		
		listeners = new Stack<Listener>();
		
		//Ready Listeners
		listeners.push( new Listener(){
		
			public void received(Connection connection, Object object) {
				if(object instanceof StartMatchRequest){
					startGame();
				}
			}
		
		});
		server.addListener(listeners.peek());
		
		//Entities
		players = matchPlayers;
		for(ServerPlayer player : matchPlayers){
			engine.addEntity(player);
		}
	
		completed = false;
		
		//Systems
		wordSystem = new WordSystem(server);
		moveSystem = new MoveSystem(server);
		
		engine.addSystem(wordSystem);
		engine.addSystem(moveSystem);
		
		//Update Settings
		update = new Timer();
		update.scheduleAtFixedRate(new TimerTask(){
			
			@Override
			public void run() {
				//Gdx.app.log("Server Game World", "Updating Server");
				update();
			}
			
		}, TIME_STEP, TIME_STEP);
		
	}
	
	private void startGame(){
		
		++startRequests;
		if(startRequests == players.length){
			signalGameStart();
		}
		
	}
	
	private void signalGameStart(){
		
		Gdx.app.log("ServerGameWorld", "signalling game start");
		
		for(ServerPlayer player : players){
			server.sendToTCP(player.getID(), new WordSubmissionResponse(true, "First"));
		}
	}
	
	private void update(){
		for(ServerPlayer p : players) {
			
			ComponentMapper<StateComponent> sm = ComponentMapper.getFor(StateComponent.class);
			StateComponent sc = sm.get(p);
			
			if(sc.state == PlayerState.KNOCKED_OUT);
		}
		engine.update(TIME_STEP);
	}
	
	public boolean isCompleted(){
		return completed;
	}
	
	public void dispose(){
		
		update.cancel();
		
		while(!listeners.isEmpty()){
			server.removeListener(listeners.pop());
		}
		wordSystem.dispose();
		moveSystem.dispose();
	}

}
