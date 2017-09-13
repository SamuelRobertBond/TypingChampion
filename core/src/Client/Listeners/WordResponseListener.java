package Client.Listeners;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import Server.Responses.WordSubmissionResponse;

public class WordResponseListener extends Listener{

	private Label label;
	
	public WordResponseListener(Label label) {
		
		this.label = label;
		
	}
	
	@Override
	public void received(Connection connection, Object object) {
		
		if(object instanceof WordSubmissionResponse){
			
			WordSubmissionResponse r = (WordSubmissionResponse)object;
			
		}
		
	}
	
	
	private void receiveWord(String word){
		label.setText(word);
	}
	
}
