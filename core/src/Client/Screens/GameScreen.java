package Client.Screens;

import java.util.HashMap;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.tdg.gdx.TypingGame;

import Client.Entities.ClientPlayer;
import Client.Utils.ClientManager;
import Client.Utils.Constants;
import Client.Utils.MenuManager;
import Client.Worlds.GameWorld;
import Server.Utils.ServerManager;

public class GameScreen implements Screen{

	private TypingGame game;
	private StretchViewport view;
	private MenuManager menu;
	
	private ClientManager client;
	private ServerManager server;
	
	private GameWorld world;
	

	public GameScreen(TypingGame game, HashMap<String, ClientPlayer> players, ClientManager client, ServerManager server) {
		
		this.game = game;
		this.server = server;
		this.client = client;
		
		view = new StretchViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		
		world = new GameWorld(view, client, null);
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		world.render(delta);
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
