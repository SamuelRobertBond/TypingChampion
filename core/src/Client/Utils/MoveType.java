package Client.Utils;

public enum MoveType {

	//Attacks
	JAB,  		// Quick Punch Small energy use
	UPPERCUT,	//Damages opponent and makes them weaker for subsequent attacks
	HOOK, 		//Deals more damage after a block
	CROSS, 		//Double Damage after the opponent performs a jab
	
	//Defense
	BLOCK,		//Blocks a jab
	COUNTER,	//If typed quickly before a punch is thrown the punch is dodged and a counter punch is performed
	
	//States
	Idle,
	Dead,
}
