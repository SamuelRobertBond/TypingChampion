package Client.Screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.tdg.gdx.TypingGame;

import Client.Requests.JoinRequest;
import Client.Utils.ClientManager;
import Client.Utils.Constants;
import Client.Utils.MenuManager;
import Server.Utils.ServerManager;

public class MenuScreen implements Screen{

	private TypingGame game;
	
	private TextField field;
	private StretchViewport view;
	private MenuManager menu;
	private Label errorMessageLabel;
	
	@SuppressWarnings("unused")
	private ServerManager server;
	
	private final Sound BEEP;
	private final float BEEP_VOLUME = .3f;
	
	public MenuScreen(TypingGame game) {
		
		this.game = game;
		view = new StretchViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		
		BEEP = Gdx.audio.newSound(Constants.BEEP);
		
		menu = new MenuManager(view);
		
		menu.setCellSize(100, 40);
		
		errorMessageLabel = menu.addFloatingText("", 0, 0);
		
		//Menu Setup
		Label title = menu.addLabel("Typing Champ");
		menu.setActorCellSize(160, 40, title);
		menu.row();
		
		field = menu.addTextField();
		field.setMessageText("Name:");
		menu.setActorCellSize(320, 40, field);
		menu.row();
		
		//Join Button
		menu.addTextButton("Join").addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				BEEP.play(BEEP_VOLUME);
				
				if(!field.getText().equals("")){
					setClient(field.getText());
				}
				
			}
			
			
			
		});
		menu.row();
		
		
		//Host Button
		menu.addTextButton("Host").addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				BEEP.play(BEEP_VOLUME);
				
				if(!field.getText().equals("")){
					setServer(field.getText());
				}
				
			}
		});
		menu.row();
		
		menu.addTextButton("How to Play").addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				BEEP.play(BEEP_VOLUME);
				Gdx.net.openURI("https://trashdoggames.itch.io/typing-champ");
				
			}
		});
		menu.row();
		
		menu.addTextButton("Exit").addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				BEEP.play(BEEP_VOLUME);
				Gdx.app.exit();
			}
		});
	}
	
	private void setServer(String name){
		
		ServerManager server = new ServerManager(54555, 54777);
		ClientManager client = new ClientManager(name);
		
		if(server.bind(54555, 54777) && client.connectLan(name, 54555, 54777)){
			client.getClient().sendTCP(new JoinRequest(name));
			game.setScreen(new LobbyScreen(game, server, client));
		}else{
			setErrorText();
		}
	}
	
	private void setErrorText() {
		
		errorMessageLabel = menu.addFloatingText("Failed to bind ports", 0, 0);
		errorMessageLabel.setPosition(Constants.V_WIDTH/2 - errorMessageLabel.getWidth()/2, 40);
		
	}
	
	private void setClient(String name){
		game.setScreen(new ConnectionScreen(game, name));
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
		BEEP.dispose();
		menu.dispose();
	}

	
}
