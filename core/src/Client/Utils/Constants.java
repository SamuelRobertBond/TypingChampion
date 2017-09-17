package Client.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Constants {

	//Virtual Resolution Ratio
	public static float V_WIDTH = 640;
	public static float V_HEIGHT = 360;
	
	//Font File
	public static final FileHandle FONT_FILE = Gdx.files.internal("Fonts/Tahoma.ttf");
	
	//Textures
	public static final FileHandle PLAYER_SPRITE_SHEET = Gdx.files.internal("Spirtes/player.png");
	public static final FileHandle ENEMY_SPRITE_SHEET = Gdx.files.internal("Spirtes/enemy.png");
	
}
