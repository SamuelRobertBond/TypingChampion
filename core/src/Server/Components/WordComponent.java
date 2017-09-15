package Server.Components;

import com.badlogic.ashley.core.Component;

public class WordComponent implements Component{

	public String word;
	
	public WordComponent(String word){
		this.word = word;
	}
	
}
