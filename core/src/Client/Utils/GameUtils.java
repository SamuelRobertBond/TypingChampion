package Client.Utils;

import com.esotericsoftware.kryo.Kryo;

import Client.Requests.MoveRequest;
import Client.Requests.UpdateRequest;
import Server.Responses.MoveResponse;
import Server.Responses.UpdateResponse;

public class GameUtils {

	public static void serializeKryoObjects(Kryo kryo){
		
		kryo.register(MoveRequest.class);
		kryo.register(MoveResponse.class);
		
		kryo.register(UpdateRequest.class);
		kryo.register(UpdateResponse.class);
		
	}
	
}
