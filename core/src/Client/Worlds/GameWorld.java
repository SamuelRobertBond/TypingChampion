package Client.Worlds;

import com.badlogic.gdx.utils.viewport.StretchViewport;

import Client.Entities.ClientPlayer;
import Client.Utils.Constants;

public class GameWorld {

	public GameWorld(StretchViewport view) {
		
		view = new StretchViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		
		ClientPlayer player = new ClientPlayer();
		
	}
	
}
