package Client.Worlds;

import java.util.HashMap;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import Client.Entities.ClientPlayer;
import Client.Requests.WordSubmissionRequest;
import Client.Utils.ClientManager;
import Client.Utils.MenuManager;

public class GameWorld {

	private ClientManager client;
	private HashMap<String, ClientPlayer> players;
	
	private MenuManager menu;
	private TextField field;
	private Label word;
	
	public GameWorld(StretchViewport view, ClientManager client, HashMap<String, ClientPlayer> players) {
		
		this.client = client;
		this.players = players;
		
		
		//Start Game Listener
		client.getClient().addListener(new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				
				
				
			}
			
		});

		
		//Menu Stuff
		
		menu = new MenuManager(view);
		word = menu.addLabel("");
		
		//Text Field Enter Listener
		field = menu.addTextField();
		field.addListener(new InputListener(){
			
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Input.Keys.ENTER){
					sendWord(field.getText());
				}
				return false;
			}

		});
		
		
	}
	
	private void sendWord(String text) {
		if(!field.getText().equals("")){
			client.getClient().sendTCP(new WordSubmissionRequest());
		}
	}
	

	public void render(float delta) {
		menu.render(delta);
	}
	
}
