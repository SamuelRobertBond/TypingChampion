package Client.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.tdg.gdx.TypingGame;

import Client.Requests.JoinRequest;
import Client.Requests.MessageRequest;
import Client.Utils.ClientManager;
import Client.Utils.MenuManager;
import Server.Utils.ServerManager;

public class LobbyScreen implements Screen{

	private TypingGame game;
	
	private ServerManager server;
	private ClientManager client;
	
	private TextField field;
	
	private MenuManager menu;
	private StretchViewport view;
	
	public LobbyScreen(TypingGame game, ServerManager server, ClientManager client) {
		
		this.game = game;
		
		this.server = server;
		this.client = client;
		
		setLobby();
	}
	
	public LobbyScreen(TypingGame game, ClientManager client) {
		this.game = game;
		
		setLobby();
	}
	
	private void setLobby(){
		
		view = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		menu = new MenuManager(view);
		
		menu.setCellSize(320, 80);
		menu.addLabel("Lobby");
		menu.row();
		
		field = menu.addTextField();
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
		
		menu.setActorCellSize(160, 80, send);
		menu.setActorCellSize(160, 80, ready);
		
	}
	
	private void sendMessage() {
		
		if(!field.getText().equals("")){
			client.getClient().sendTCP(new MessageRequest(field.getText()));
		}
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		menu.render(delta);
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
		// TODO Auto-generated method stub
		
	}

}
