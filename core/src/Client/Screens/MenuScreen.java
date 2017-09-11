package Client.Screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.tdg.gdx.TypingGame;

import Client.Utils.ClientManager;
import Client.Utils.Constants;
import Client.Utils.MenuManager;
import Server.Utils.ServerManager;

public class MenuScreen implements Screen{

	private TypingGame game;
	
	private TextField field;
	private StretchViewport view;
	private MenuManager menu;
	
	private ServerManager server;
	private ClientManager client;
	
	public MenuScreen(TypingGame game) {
		this.game = game;
		view = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		menu = new MenuManager(view);
		
		menu.setCellSize(80, 40);
		
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
				
				if(!field.getText().equals("")){
					setClient(field.getMessageText());
					startLobby();
				}
				
			}
		});
		menu.row();
		
		//Host Button
		menu.addTextButton("Host").addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				if(!field.getText().equals("")){
					setServer();
					setClient(field.getText());
					startLobby();
				}
				
			}
		});
		menu.row();
		
		menu.addTextButton("Exit").addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
	}
	
	private void setServer(){
		server = new ServerManager(54555, 54777);
	}
	
	private void setClient(String name){
		client = new ClientManager(name, 54555, 54777);
	}
	
	private void startLobby(){
		this.dispose();
		if(server != null){
			game.setScreen(new LobbyScreen(game, server, client));
		}else{
			game.setScreen(new LobbyScreen(game, client));
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
		menu.dispose();
	}

	
}
