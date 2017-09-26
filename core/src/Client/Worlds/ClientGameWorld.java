package Client.Worlds;

import java.util.HashMap;
import java.util.LinkedList;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import Client.Entities.ClientPlayer;
import Client.Requests.MoveRequest;
import Client.Requests.WordSubmissionRequest;
import Client.Systems.SpriteRenderSystem;
import Client.Systems.UiRenderSystem;
import Client.Utils.ClientManager;
import Client.Utils.Constants;
import Client.Utils.GameUtils;
import Client.Utils.MenuManager;
import Client.Utils.MoveType;
import Server.Enities.ServerPlayer;
import Server.Responses.AnimationResponse;
import Server.Responses.GameOverResponse;
import Server.Responses.KOResponse;
import Server.Responses.KOUpdateResponse;
import Server.Responses.StatResponse;
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
	private Label koTimeLabel;
	
	private SpriteRenderSystem spriteSystem;
	private UiRenderSystem uiSystem;
	
	private boolean completed;
	
	private String name;
	
	private final float UI_ELEMENT_WIDTH = 200;
	private final float UI_ELEMENT_HEIGHT = 30;
	private final float UI_ELEMENT_GAP = 10;
	
	private boolean knockedOut;
	
	public ClientGameWorld(StretchViewport view, ClientManager client, String enemyName) {
		
		this.client = client;	
		this.view = view;
		name = client.name;
		
		completed = false;
		knockedOut = false;
		
		menu = new MenuManager(view);
		
		//Text Field Enter Listener
		field = menu.addFloatingTextField(Constants.V_WIDTH/2 - 100, 20, 200, 40);
		field.addListener(new InputListener(){
			
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Input.Keys.ENTER){
					sendWord(field.getText());
				}
				return false;
			}

		});
		
		wordLabel = menu.addFloatingText("First", (field.getX() + field.getWidth()/2), (field.getHeight() + field.getY()) + 5);
		wordLabel.setPosition((field.getX() + field.getWidth()/2) - wordLabel.getWidth()/2 , wordLabel.getY());
		
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
				
				if(object instanceof KOResponse){
					KOResponse r = (KOResponse) object;
					beginKO(r);
				}			
			}
			
		});
		client.getClient().addListener(listeners.peek());
		
		//KO Update Listener
		listeners.push(new Listener(){
			@Override
			public void received(Connection connection, Object object) {
				
				if(object instanceof KOUpdateResponse){
					updateKOTime((KOUpdateResponse)object);
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
		
		//Stat Listener
		listeners.push(new Listener(){
			@Override
			public void received(Connection connection, Object object) {
				if(object instanceof StatResponse){
					setStats((StatResponse)object);
				}
			}

		});
		client.getClient().addListener(listeners.peek());
		
		//Game Over Listener
		listeners.push(new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				if(object instanceof GameOverResponse){
					gameover((GameOverResponse)object);
				}
			}
			
		});
		client.getClient().addListener(listeners.peek());

		
		//Players
		players = new HashMap<String, ClientPlayer>();
		players.put(client.name, new ClientPlayer(client.name, Constants.V_WIDTH/4, Constants.V_HEIGHT/2, 64, 64));
		players.put(enemyName, new ClientPlayer(enemyName, Constants.V_WIDTH * 0.75f, Constants.V_HEIGHT/2, -64, 64));
		
		//Adding Stats UI
		players.get(client.name).addUI(menu.addStatsUI(Constants.V_WIDTH/2 - UI_ELEMENT_WIDTH - UI_ELEMENT_GAP, Constants.V_HEIGHT - UI_ELEMENT_HEIGHT - 5, UI_ELEMENT_WIDTH, UI_ELEMENT_HEIGHT, 20));
		
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


	private void beginKO(KOResponse r) {
		
		knockedOut = r.enable;
		
		if(r.enable){
			players.get(r.name).setDead(true);
			koTimeLabel = menu.addFloatingText("0", Constants.V_WIDTH/2, Constants.V_HEIGHT/2);
		}else{
			players.get(r.name).setDead(false);
			menu.removeActor(koTimeLabel);
			koTimeLabel = null;
		}
		
	}
	
	private void updateKOTime(KOUpdateResponse r) {
		if(koTimeLabel != null){
			koTimeLabel.setText(r.time + "");
		}
	}
	
	private void changeWord(String word){
		
		menu.removeActor(wordLabel);
		
		this.word = word;
		wordLabel = menu.addFloatingText(word, (field.getX() + field.getWidth()/2), (field.getHeight() + field.getY()) + 5);
		wordLabel.setPosition((field.getX() + field.getWidth()/2) - wordLabel.getWidth()/2 , wordLabel.getY());
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
			
		}else{
			checkForMove(text.toLowerCase());
		}
	}
	
	private void setStats(StatResponse r) {
		players.get(client.name).updateStats(r);
	}
	
	private void checkForMove(String move){
		
		if(!knockedOut) {
			if(move.equals("jab")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.JAB));
				field.setText("");
			}else if(move.equals("block")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.BLOCK));
				field.setText("");
			}else if(move.equals("cross")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.CROSS));
				field.setText("");
			}else if(move.equals("counter")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.COUNTER));
				field.setText("");
			}else if(move.equals("hook")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.HOOK));
				field.setText("");
			}else if(move.equals("uppercut")){
				client.getClient().sendTCP(new MoveRequest(client.name, MoveType.UPPERCUT));
				field.setText("");
			}
		}
		
	}
	
	public void render(float delta) {
		engine.update(delta);
		menu.render(delta);
	}
	
	public void resize(int width, int height){
		view.update(width, height);
	}
	
	private void gameover(GameOverResponse r) {
		
		Gdx.app.log("Client Game World", "Recieved Game Over Response");
		
		if(field != null){
			menu.removeActor(field);
		}
		
		if(wordLabel != null){
			menu.removeActor(wordLabel);
		}
		
		if(koTimeLabel != null){
			menu.removeActor(koTimeLabel);
		}
		
		Label label = menu.addFloatingText(r.name + " Wins!", 0, 0);
		label.setPosition(Constants.V_WIDTH/2 - label.getWidth()/2, Constants.V_HEIGHT/2 - label.getHeight()/2);
		
		TextButton button = menu.addFloatingButton("To Menu", 0 , 0);
		button.setSize(Constants.V_WIDTH/4, 20);
		button.setPosition(Constants.V_WIDTH/2 - button.getWidth()/2, label.getY() - button.getHeight()/2 - 10);
		
		button.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setCompleted();
			}

		});
		
	}
	
	public boolean getCompleted(){
		return completed;
	}
	
	private void setCompleted(){
		completed = true;
	}
	
	public void dispose(){
		menu.dispose();
		while(!listeners.isEmpty()){
			client.getClient().removeListener(listeners.poll());
		}
		
	}
	
}
