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
	
	//Sounds
	public static final FileHandle PUNCH = Gdx.files.internal("Sounds/punch.wav");
	public static final FileHandle BELL = Gdx.files.internal("Sounds/bell.ogg");
	public static final FileHandle BEEP = Gdx.files.internal("Sounds/beep.wav");
	public static final FileHandle MUSIC = Gdx.files.internal("Sounds/music.mp3");
	public static final FileHandle WORD_CORRECT = Gdx.files.internal("Sounds/correct.ogg");
	
}
