package Client.Screens;

import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.tdg.gdx.TypingGame;

import Client.Listeners.MessageResponseListener;
import Client.Requests.MessageRequest;
import Client.Requests.ReadyRequest;
import Client.Utils.ClientManager;
import Client.Utils.Constants;
import Client.Utils.MenuManager;
import Server.Responses.StartResponse;
import Server.Utils.ServerManager;
import Server.Utils.WordUtil;

public class LobbyScreen implements Screen{

	private TypingGame game;
	
	private ServerManager server;
	private ClientManager client;

	private StretchViewport view;
	
	private Stack<Listener> listeners;
	
	private MenuManager menu;
	private TextField field;
	private TextArea area;
	private InputMultiplexer in;

	private StartResponse start;
	
	
	public LobbyScreen(TypingGame game, ServerManager server, ClientManager client) {
		
		this.game = game;
		
		this.listeners = new Stack<Listener>();
		
		this.server = server;
		this.client = client;
		
		WordUtil.initializeWords();
		setLobby();
		
	}
	
	public LobbyScreen(TypingGame game, ClientManager client) {
		
		this.game = game;
		this.client = client;
		
		this.listeners = new Stack<Listener>();
		
		setLobby();
		
	}
	
	
	private void setLobby(){
		
		in = new InputMultiplexer();
		
		view = new StretchViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		
		//Menu 
		menu = new MenuManager(view, in);
		
		menu.setCellSize(320, 34);
		menu.addLabel("Lobby");
		menu.row();
		
		area = menu.addTextArea();
		area.setTouchable(Touchable.disabled);
		menu.setActorCellSize(320, 200, area);
		menu.row();
		
		field = menu.addTextField();
		menu.setActorCellSize(320, 40, field);
		field.setMessageText("Message:");
		
		menu.row();
		
		TextButton send = menu.addTextButton("Send");
		send.addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sendMessage();
			}
			
		});
		
		TextButton ready = menu.addTextButton("Ready");
		
		ready.addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {	
				setReady();
			}
			
		});
		
		menu.setActorCellSize(160, 40, send);
		menu.setActorCellSize(160, 40, ready);
		
		field.addCaptureListener(new InputListener(){
			
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				
				if(keycode == Input.Keys.ENTER){
					sendMessage();
				}
				
				return false;
			}
			
		});
		
		menu.getStage().setKeyboardFocus(field);
				
		//Start Listener
		listeners.push(new Listener(){

			@Override
			public void received(Connection connection, Object object) {
				if(object instanceof StartResponse){
					start = (StartResponse)object;
				}
			}
			
		});
		client.getClient().addListener(listeners.peek());
		
		//Message Listener
		listeners.push(new MessageResponseListener(area));
		client.getClient().addListener(listeners.peek());
		
		
		Gdx.input.setInputProcessor(in);
		
	}
	
	private void setReady(){
		client.getClient().sendTCP(new ReadyRequest());
	}
	
	private void sendMessage() {
		
		if(!field.getText().equals("")){
			//Gdx.app.log("LobbyScreen - Sending Message", client.name + ": " + field.getText());
			
			area.appendText(client.name + ": " + field.getText() + "\n");
			client.getClient().sendTCP(new MessageRequest(client.name, field.getText()));
			field.setText("");
		}
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		menu.render(delta);
		
		if(start != null){
			this.dispose();
			game.setScreen(new GameScreen(game, client, server));
		}
		
	}

	@Override
	public void resize(int width, int height) {
		view.update(width, height);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		
		while(!listeners.isEmpty()){
			client.getClient().removeListener(listeners.pop());
		}
		
	}

}
