package Server.Responses;

public class WordSubmissionResponse {

	public boolean success;
	public String newWord;
	
	public WordSubmissionResponse(boolean success, String newWord) {
		this.success = success;
		this.newWord = newWord;
	}

	public WordSubmissionResponse() {
		// TODO Auto-generated constructor stub
	}
}
