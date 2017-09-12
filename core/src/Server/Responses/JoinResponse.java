package Server.Responses;

public class JoinResponse {
	
	public String name;
	public boolean success;
	
	public JoinResponse() {}
	
	public JoinResponse(String name, boolean success) {
		this.name = name;
		this.success = success;
	}
}
