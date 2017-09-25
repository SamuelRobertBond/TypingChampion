package Client.Utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;

import Client.Entities.ClientPlayer;
import Client.Requests.JoinRequest;
import Client.Requests.KOWordRequest;
import Client.Requests.MessageRequest;
import Client.Requests.MoveRequest;
import Client.Requests.ReadyRequest;
import Client.Requests.StartMatchRequest;
import Client.Requests.WordSubmissionRequest;
import Server.Responses.AnimationResponse;
import Server.Responses.JoinResponse;
import Server.Responses.KOResponse;
import Server.Responses.MessageResponse;
import Server.Responses.MoveResponse;
import Server.Responses.StartResponse;
import Server.Responses.StatResponse;
import Server.Responses.WordSubmissionResponse;
import Server.Utils.PlayerState;

public class GameUtils {

	public static void serializeKryoObjects(Kryo kryo){
		
		kryo.register(MoveRequest.class);
		kryo.register(MoveResponse.class);
		
		kryo.register(MessageRequest.class);
		kryo.register(MessageResponse.class);
		
		kryo.register(JoinRequest.class);
		kryo.register(JoinResponse.class);
		
		kryo.register(WordSubmissionRequest.class);
		kryo.register(WordSubmissionResponse.class);
		
		kryo.register(ReadyRequest.class);
		
		kryo.register(StartResponse.class);
		
		kryo.register(StartMatchRequest.class);
		kryo.register(StatResponse.class);
		
		kryo.register(KOResponse.class);
		kryo.register(KOWordRequest.class);
		
		kryo.register(AnimationResponse.class);
		
		kryo.register(MoveType.class);
		kryo.register(PlayerState.class);
		
	}
	
	public static final float frameDuration = 1/5f;
	
	public static void createBoxerAnimation(ClientPlayer player, FileHandle fileHandle){
		
		Texture tex = new Texture(fileHandle);
		
		TextureRegion[][] frames = TextureRegion.split(tex, 32, 32);
		
		TextureRegion idle[] = {frames[0][0], frames[0][1], frames[0][2], frames[0][3]};
		TextureRegion block[] = {frames[0][4], frames[0][5], frames[0][6], frames[0][7]};
		
		TextureRegion jab[] = {frames[1][4], frames[1][5], frames[1][6], frames[1][7]};
		TextureRegion cross[] = {frames[1][0], frames[1][1], frames[1][2], frames[1][3]};
		
		TextureRegion uppercut[] = {frames[2][0], frames[2][1], frames[2][2], frames[2][3]};
		TextureRegion ko[] = {frames[2][4]};
		
		player.addAnimation(MoveType.BLOCK, new Animation<TextureRegion>(frameDuration, block));
		player.addAnimation(MoveType.UPPERCUT, new Animation<TextureRegion>(frameDuration, uppercut));
		player.addAnimation(MoveType.HOOK, new Animation<TextureRegion>(frameDuration, uppercut));
		player.addAnimation(MoveType.JAB, new Animation<TextureRegion>(frameDuration, jab));
		player.addAnimation(MoveType.COUNTER, new Animation<TextureRegion>(frameDuration, jab));
		player.addAnimation(MoveType.CROSS, new Animation<TextureRegion>(frameDuration, cross));
		player.addAnimation(MoveType.Idle, new Animation<TextureRegion>(frameDuration, idle));
		player.addAnimation(MoveType.Dead, new Animation<TextureRegion>(frameDuration, ko));
		
	}
	
}
