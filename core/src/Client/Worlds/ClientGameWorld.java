package Client.Worlds;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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
import Client.Requests.MoveRequest;
import Client.Requests.WordSubmissionRequest;
import Client.Utils.ClientManager;
import Client.Utils.MenuManager;
import Client.Utils.MoveType;
import Server.Responses.WordSubmissionResponse;

public class ClientGameWorld {

	private ClientManager client;
	
	private LinkedList<Listener> listeners;
	
	private MenuManager menu;
	private TextField field;
	
	private String word;
	private Label wordLabel;
	
	public ClientGameWorld(StretchViewport view, ClientManager client, HashMap<String, ClientPlayer> players) {
		
		this.client = client;		
		
		Gdx.app.log("Client World", "Client World Created");
		
		//Menu Stuff
		
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
		}else{
			checkForMove(text.toLowerCase());
		}
	}
	
	private void checkForMove(String move){
		
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
	
	public void render(float delta) {
		menu.render(delta);
	}
	
	public void dispose(){
		menu.dispose();
		while(!listeners.isEmpty()){
			client.getClient().removeListener(listeners.poll());
		}
		
	}
}
