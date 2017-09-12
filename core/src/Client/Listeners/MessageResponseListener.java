package Client.Listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import Server.Responses.MessageResponse;

public class MessageResponseListener extends Listener {
	
	private TextArea area;
	
	public MessageResponseListener(TextArea area) {
		this.area = area;
	}
	
	@Override
	public void received(Connection connection, Object object) {
		
		if(object instanceof MessageResponse){
			MessageResponse r = (MessageResponse)object;
			Gdx.app.log("MessageResponseListener: Message Received: ", r.name + ": " + r.message);
			area.appendText(r.name + ": " + r.message + "\n");
		}
		
	}
	
	
}
