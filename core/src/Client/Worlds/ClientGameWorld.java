package Client.Worlds;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import Client.Entities.ClientPlayer;
import Client.Requests.KOWordRequest;
import Client.Requests.MoveRequest;
import Client.Requests.WordSubmissionRequest;
import Client.Systems.SpriteRenderSystem;
import Client.Systems.UiRenderSystem;
import Client.Utils.ClientManager;
import Client.Utils.Constants;
import Client.Utils.GameUtils;
import Client.Utils.MenuManager;
import Client.Utils.MoveType;
import Client.Utils.Role;
import Server.Responses.AnimationResponse;
import Server.Responses.KOBeginResponse;
import Server.Responses.WordSubmissionResponse;

public class ClientGameWorld {

	private ClientManager client;
	private StretchViewport view;
	private Engine engine;
	
	private HashMap<String, ClientPlayer> players;
	private LinkedList<Listener> listeners;
	
	private MenuManager menu;
	private TextField field;
	
	private String word;
	private Label wordLabel;
	
	private SpriteRenderSystem spriteSystem;
	private UiRenderSystem uiSystem;
	
	private String name;
	
	private boolean knockedOut;
	
	public ClientGameWorld(StretchViewport view, ClientManager client, String enemyName) {
		
		this.client = client;	
		this.view = view;
		name = client.name;
		
		knockedOut = false;
		
		menu = new MenuManager(view);
		wordLabel = menu.addFloatingText("First", 100, 100);
		
		//Text Field Enter Listener
		field = menu.addFloatingTextField(100, 100, 200, 100);
		field.addListener(new InputListener(){
			
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Input.Keys.ENTER){
					sendWord(field.getText());
				}
				return false;
			}

		});
		
		listeners = new LinkedList<Listener>();
		
		//Word Response Listener
		listeners.push(new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				
				if(object instanceof WordSubmissionResponse){
					
					WordSubmissionResponse r = (WordSubmissionResponse) object;
					
					if(r.success){
						Gdx.app.log("Client Game World", "Receieved new word");
						changeWord(r.newWord);
					}
				}			
			}
			
		});		
		client.getClient().addListener(listeners.peek());
		
		//KO Listener
		listeners.push(new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				
				if(object instanceof KOBeginResponse){
					
					KOBeginResponse r = (KOBeginResponse) object;
					
					if(r.player.compareTo(name) == 0) {
						knockedOut = true;
						Gdx.app.log("Client Game World", "Player " + name + " has been knocked out!");
					}
				}			
			}
			
		});
		client.getClient().addListener(listeners.peek());
		
		//Animation Listener
		listeners.push(new Listener(){
			
			public void received(Connection connection, Object object) {
				
				if(object instanceof AnimationResponse){
					setAnimation((AnimationResponse)object);
				}
				
			};
			
		});
		client.getClient().addListener(listeners.peek());
		
		//Players
		players = new HashMap<String, ClientPlayer>();
		players.put(client.name, new ClientPlayer(client.name, Constants.V_WIDTH/4, Constants.V_HEIGHT/2, 64, 64));
		players.put(enemyName, new ClientPlayer(enemyName, Constants.V_WIDTH * 0.75f, Constants.V_HEIGHT/2, -64, 64));
		
		//Adding Stats UI
		players.get(client.name).addUI(menu.addStatsUI(0, 10, 100, 50, 20));
		
		//Adds Animations to the player
		GameUtils.createBoxerAnimation(players.get(client.name), Constants.PLAYER_SPRITE_SHEET);
		GameUtils.createBoxerAnimation(players.get(enemyName), Constants.ENEMY_SPRITE_SHEET);
		
		engine = new Engine();
		
		engine.addEntity(players.get(client.name));
		engine.addEntity(players.get(enemyName));
		
		spriteSystem = new SpriteRenderSystem(name);
		engine.addSystem(spriteSystem);
		
		uiSystem = new UiRenderSystem(players.get(client.name));
		engine.addSystem(uiSystem);
		
		Gdx.app.log("Client Game World", "Finished Constructing");
	}

	private void changeWord(String word){
		this.word = word;
		this.wordLabel.setText(word);
		field.setText("");
	}
	
	private void setAnimation(AnimationResponse r){
		
		Gdx.app.log("ClientGameWorld setAnimation", "Setting " + r.name + "'s Animation");
		
		ClientPlayer player = players.get(r.name);
		player.animationComponent.move = r.move;
		player.animationComponent.stateTime = 0;
	}
	
	private void sendWord(String text) {
		
		text = text.toUpperCase();
		
		if(!text.equals("") && text.equals(word.toUpperCase())){		
			client.getClient().sendTCP(new WordSubmissionRequest(text));
			
			/*if(!knockedOut) {
				client.getClient().sendTCP(new WordSubmissionRequest(text));
			} else {
				client.getClient().sendTCP(new KOWordRequest(text));
			}*/
			
		}else{
			checkForMove(text.toLowerCase());
		}
	}
	
	private void checkForMove(String move){
		
		if(!knockedOut) {
			if(move.equals("jab")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.JAB));
			}else if(move.equals("block")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.BLOCK));
			}else if(move.equals("cross")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.CROSS));
			}else if(move.equals("counter")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.COUNTER));
			}else if(move.equals("hook")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.HOOK));
			}else if(move.equals("uppercut")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.UPPERCUT));
			}
		}
		
	}
	
	public void render(float delta) {
		menu.render(delta);
		engine.update(delta);
	}
	
	public void resize(int width, int height){
		view.update(width, height);
	}
	
	public void dispose(){
		menu.dispose();
		while(!listeners.isEmpty()){
			client.getClient().removeListener(listeners.poll());
		}
		
	}
}
