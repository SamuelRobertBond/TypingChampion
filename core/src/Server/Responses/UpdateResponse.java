package Server.Responses;

public class UpdateResponse {

	public int id;
	String name;
	
	int health;
	
	/**
	 * Response used to update the health and energy of each player
	 * @param id
	 */
	public UpdateResponse(int id) {
		
	}
	
	//Used during serialization
	public UpdateResponse(){}
	
}
