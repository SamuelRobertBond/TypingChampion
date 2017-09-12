package Server.Responses;

public class MessageResponse {
	
	public String message;
	public String name;
	
	public MessageResponse() {}
	
	public MessageResponse(String name, String message) {
		this.name = name;
		this.message = message;
	}

}
