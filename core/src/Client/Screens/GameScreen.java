package Client.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.tdg.gdx.TypingGame;

import Client.Requests.StartMatchRequest;
import Client.Utils.ClientManager;
import Client.Utils.Constants;
import Client.Worlds.ClientGameWorld;
import Server.Utils.ServerManager;

public class GameScreen implements Screen{

	private TypingGame game;
	private StretchViewport view;
	
	private ClientManager client;
	
	@SuppressWarnings("unused")
	private ServerManager server;
	
	private ClientGameWorld world;
	
	

	public GameScreen(TypingGame game, ClientManager client, ServerManager server, String enemyName) {
		
		this.game = game;
		this.server = server;
		this.client = client;
		
		view = new StretchViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		world = new ClientGameWorld(view, client, enemyName);
		
		client.getClient().sendTCP(new StartMatchRequest());
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(world.getCompleted()){
			world.dispose();
			game.setScreen(new LobbyScreen(game, client));
		}
		
		world.render(delta);
		
	}

	@Override
	public void resize(int width, int height) {
		world.resize(width, height);
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
		
	}
	
}
