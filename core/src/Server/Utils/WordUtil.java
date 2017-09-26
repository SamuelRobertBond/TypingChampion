package Server.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public class WordUtil {
	
	private static final int WORD_MIN_LENGTH = 3;
	private static final int WORD_MAX_LENGTH = 7;
	
	private static HashMap<Integer, LinkedList<String>> words;
	
	public static void initializeWords(){
		
		Scanner in = null;
			
		FileHandle handle = Gdx.files.internal("Words/words.txt");
		
		in = new Scanner(handle.read());
		
		words = new HashMap<Integer, LinkedList<String>>();
		
		setUpMap();
		
		if(in != null){
			
			while(in.hasNext()){
				
				String word = in.nextLine();
				
				if(words.containsKey(word.length())){
					words.get(word.length()).add(word);
				}
				
			}
			
			in.close();
		}
		
	}
	
	private static void setUpMap() {
		
		for(int i = WORD_MIN_LENGTH; i <= WORD_MAX_LENGTH; ++i){
			words.put(i, new LinkedList<String>());
		}
		
	}

	public static String getWord(){
		LinkedList<String> l = words.get(MathUtils.random(WORD_MIN_LENGTH, WORD_MAX_LENGTH));
		return l.get(MathUtils.random(l.size() - 1));
	}
	
	/**
	 * Returns a word of the specified length, if there are no words of that length it returns null
	 * @param length
	 * @return
	 */
	public static String getWord(int length){
		
		if(words.containsKey(length)){
			LinkedList<String> l = words.get(length);
			
			if(length >= l.size()){
				length = l.size() - 1;
			}
			
			return l.get(MathUtils.random(l.size() - 1));
		}else{
			return null;
		}
		
	}
	
}
