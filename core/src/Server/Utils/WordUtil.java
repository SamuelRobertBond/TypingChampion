package Server.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public class WordUtil {
	
	private static HashMap<Integer, String> words;
	
	public static void initializeWords(){
		
		Scanner in = null;
			
		FileHandle handle = Gdx.files.internal("Words/words.txt");
		
		in = new Scanner(handle.read());
		
		words = new HashMap<Integer, String>();
		
		if(in != null){
			
			for(int i = 0; in.hasNext(); ++i){
				words.put(i, in.nextLine());
			}
			
			in.close();
		}
		
	}
	
	public static String getWord(){
		
		System.out.println("words size " + words.size());
		
		return words.get(MathUtils.random(words.size() - 1));
	}
}
