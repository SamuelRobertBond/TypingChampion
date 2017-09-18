package Client.Worlds;

import java.util.HashMap;
import java.util.LinkedList;

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
import Client.Utils.ClientManager;
import Client.Utils.Constants;
import Client.Utils.GameUtils;
import Client.Utils.MenuManager;
import Client.Utils.MoveType;
import Client.Utils.Role;
import Server.Responses.KOBeginResponse;
import Server.Responses.WordSubmissionResponse;

public class ClientGameWorld {

	private ClientManager client;
	private Engine engine;
	
	private LinkedList<Listener> listeners;
	
	private MenuManager menu;
	private TextField field;
	
	private String word;
	private Label wordLabel;
	
	private HashMap<Role, ClientPlayer> players;
	private SpriteRenderSystem spriteSystem;
	
	private String name;
	
	private boolean knockedOut;
	
	public ClientGameWorld(StretchViewport view, ClientManager client) {
		
		this.client = client;		
		name = client.name;
		
		knockedOut = false;
		
		Gdx.app.log("Client World", "Client World Created");
		
		menu = new MenuManager(view);
		wordLabel = menu.addLabel("");
		
		menu.row();
		
		//Text Field Enter Listener
		field = menu.addTextField();
		menu.setActorCellSize(400, 34, field);
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
		
		//Players
		players = new HashMap<Role, ClientPlayer>();
		players.put(Role.Player, new ClientPlayer());
		players.put(Role.Enemy, new ClientPlayer());
		
		//Adds Animations to the player
		GameUtils.createBoxerAnimation(players.get(Role.Player), Constants.PLAYER_SPRITE_SHEET);
		GameUtils.createBoxerAnimation(players.get(Role.Enemy), Constants.PLAYER_SPRITE_SHEET);
		
		engine = new Engine();
		
		engine.addEntity(players.get(Role.Player));
		engine.addEntity(players.get(Role.Enemy));
		
		spriteSystem = new SpriteRenderSystem(name);
		engine.addSystem(spriteSystem);
	}

	private void changeWord(String word){
		this.word = word;
		this.wordLabel.setText(word);
		field.setText("");
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
	
	public void dispose(){
		menu.dispose();
		while(!listeners.isEmpty()){
			client.getClient().removeListener(listeners.poll());
		}
		
	}
}
