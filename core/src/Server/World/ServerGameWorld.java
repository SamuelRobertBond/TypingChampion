package Server.World;

import java.util.Stack;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.util.Timer;
import java.util.TimerTask;

import Client.Requests.StartMatchRequest;
import Server.Components.EnergyComponent;
import Server.Components.HealthComponent;
import Server.Components.IdComponent;
import Server.Components.StateComponent;
import Server.Enities.ServerPlayer;
import Server.Responses.GameOverResponse;
import Server.Responses.KOResponse;
import Server.Responses.StatResponse;
import Server.Responses.WordSubmissionResponse;
import Server.Systems.KnockoutSystem;
import Server.Systems.MoveSystem;
import Server.Systems.WordSystem;
import Server.Utils.PlayerState;

public class ServerGameWorld{

	private Server server;
	
	private Engine engine;
	private ComponentMapper<IdComponent> im = ComponentMapper.getFor(IdComponent.class);
	private ComponentMapper<EnergyComponent> em = ComponentMapper.getFor(EnergyComponent.class);
	private ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
	private ComponentMapper<StateComponent> sm = ComponentMapper.getFor(StateComponent.class);
	
	private WordSystem wordSystem;
	private MoveSystem moveSystem;
	private KnockoutSystem koSystem;
	private Timer update;
	private final long TIME_STEP = 100; //Determines how often to fire the timer task in ms
	
	private ServerPlayer[] players;
	private boolean completed;
	private String winnerName;
	
	private int startRequests = 0;
	
	private Stack<Listener> listeners;
	
	
	public ServerGameWorld(int id, ServerPlayer[] matchPlayers, Server server) {
		
		//Gdx.app.log("Server Game World", "Game World Created");
		
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
				checkDisconnects();
				update((float)TIME_STEP/1000f);
				checkKnockouts();
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
		
		Gdx.app.log("ServerGameWorld", "Signalling game start");
		
		for(ServerPlayer player : players){
			server.sendToTCP(player.getID(), new WordSubmissionResponse(true, "First"));
		}
	}
	
	private void checkKnockouts() {
		
		if(koSystem == null){
			
			for(ServerPlayer p : players) {
				
				StateComponent sc = sm.get(p);
				
				if(sc.state == PlayerState.KNOCKED_OUT) {
					
					//Gdx.app.log("Server Game World", p.getName() + " Knocked out");
					
					koSystem = new KnockoutSystem(server, p);
					engine.addSystem(koSystem);
					
					engine.removeSystem(moveSystem);
					engine.removeSystem(wordSystem);
					
					moveSystem.dispose();
					wordSystem.dispose();
					
					break;
				}else{
					winnerName = p.getName();
				}
				
			}
			
			
			
		}else if(koSystem.knockedOut()){
			
			//Gdx.app.log("Server Game World", "Knocked Out, Sending GameOver Request");
			
			for(ServerPlayer player : players){
				server.sendToTCP(player.getID(), new GameOverResponse(winnerName));
			}
			
			completed = true;
			
			dispose();
		
		}else if(koSystem.hasCompleted()){
			
			wordSystem = new WordSystem(server);
			moveSystem = new MoveSystem(server);
			
			HealthComponent hc = hm.get(koSystem.getPlayer());
			StateComponent sc = sm.get(koSystem.getPlayer());
			
			hc.health = hc.maxHealth;
			++hc.knockouts;
			
			sc.state = PlayerState.OPEN;
			
			engine.removeSystem(koSystem);
			koSystem = null;
			
			engine.addSystem(wordSystem);
			engine.addSystem(moveSystem);
			
			
			//End KO
			server.sendToTCP(players[0].getID(), new KOResponse(players[0].getName(), false));
			server.sendToTCP(players[1].getID(), new KOResponse(players[1].getName(), false));
			
			//Update Stats
			server.sendToTCP(players[0].getID(), new KOResponse(players[0].getName(), false));
			server.sendToTCP(players[1].getID(), new KOResponse(players[1].getName(), false));
			
			sendStats(players[0]);
			sendStats(players[1]);
		}
		
	}
	
	private void update(float delta){	
		
		engine.update(delta);
		checkKnockouts();
		
	}
	
	public boolean isCompleted(){
		return completed;
	}
	
	
	private void sendStats(Entity player){
		
		HealthComponent hc = hm.get(player);
		EnergyComponent ec = em.get(player);
		IdComponent ic = im.get(player);
		
		server.sendToTCP(ic.id, new StatResponse(hc.health, ec.energy));
		
	}
	
	
	private void checkDisconnects() {
		
		ServerPlayer prev = players[1];
		
		for(ServerPlayer player : players){
			
			IdComponent ic = im.get(player);
			boolean connected = false;
			
			for(Connection c : server.getConnections()){
				int id = c.getID();
				if(id == ic.id){
					connected = true;
				}
			}
			
			if(!connected){
				Gdx.app.log("Server Game World", ic.name + " disconnected");
				ic = im.get(prev);
				server.sendToTCP(prev.getID(), new GameOverResponse(ic.name));
				completed = true;
				dispose();
				break;
			}
			
			prev = player;
		}
		
	}
	
	
	public void dispose(){
		
		update.cancel();
		
		while(!listeners.isEmpty()){
			server.removeListener(listeners.pop());
		}
		
		if(koSystem != null){
			koSystem.dispose();
		}
		
		for(ServerPlayer player : players){
			player.reset();
		}
		
		engine.removeAllEntities();
	}

}
