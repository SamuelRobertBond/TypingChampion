package Client.Utils;

import com.esotericsoftware.kryo.Kryo;

import Client.Requests.JoinRequest;
import Client.Requests.MessageRequest;
import Client.Requests.MoveRequest;
import Client.Requests.WordSubmissionRequest;
import Server.Responses.JoinResponse;
import Server.Responses.MessageResponse;
import Server.Responses.MoveResponse;
import Server.Responses.WordSubmissionResponse;

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
		
	}
	
}
