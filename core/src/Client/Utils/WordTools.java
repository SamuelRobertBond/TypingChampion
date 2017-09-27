package Client.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class WordTools {

	
	/**
	 * Used to format a words file in the asset
	 * NOT TO BE USED WHILE RUNNING GAME
	 */
	public static void formatWordFile(){
		
		try {
			
			Scanner in = new Scanner(new File("C:/Users/Sam Bond/Desktop/Typing Champ/android/assets/Words/words.txt"));
			
			Queue<String> queue = new LinkedList<String>();
			
			while(in.hasNext()){
				
				String word = in.nextLine();
				
				System.out.println(word);
				
				if(!(word.length() < 3 || word.contains(":") || word.contains("'") || word.contains(".") || word.contains("'") || word.contains("-") || word.length() > 12)){
					queue.offer(word);
				}
				
			}
			
			File file = new File("words_revised.txt");
			BufferedWriter writer = null;
			
			try {
				writer = new BufferedWriter(new FileWriter(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while(queue.size() > 0){
				
				String word = queue.poll();
				writer.write(word);
				writer.newLine();
				
			}
			
			in.close();
			
			System.out.println("done");
			
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
