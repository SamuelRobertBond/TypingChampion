package Client.Screens;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.tdg.gdx.TypingGame;

import Client.Utils.Constants;
import Client.Worlds.GameWorld;

public class GameScreen {

	private TypingGame game;
	private StretchViewport view;
	
	private Client client;
	private Server server;
	
	private GameWorld world;
	
	public GameScreen(TypingGame game, Client client, Server server) {
		
		this.game = game;
		this.server = server;
		this.client = client;
		
		view = new StretchViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		
		world = new GameWorld(view);
	}
	
}
