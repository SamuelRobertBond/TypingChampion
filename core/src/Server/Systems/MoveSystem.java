package Server.Systems;

import java.util.LinkedList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import Client.Requests.MoveRequest;
import Client.Utils.MoveType;
import Server.Components.EnergyComponent;
import Server.Components.HealthComponent;
import Server.Components.IdComponent;
import Server.Components.StateComponent;
import Server.Enities.ServerPlayer;
import Server.Responses.AnimationResponse;
import Server.Responses.KOResponse;
import Server.Responses.StatResponse;
import Server.Utils.MoveInformation;
import Server.Utils.PlayerState;

public class MoveSystem extends EntitySystem{

	private Server server;
	
	private ImmutableArray<Entity> entities;
	
	private ComponentMapper<IdComponent> im = ComponentMapper.getFor(IdComponent.class);
	private ComponentMapper<StateComponent> sm = ComponentMapper.getFor(StateComponent.class);
	private ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
	private ComponentMapper<EnergyComponent> em = ComponentMapper.getFor(EnergyComponent.class);
	
	private LinkedList<MoveRequest> moves;
	
	private Listener moveListener;
	
	public MoveSystem(Server server) {
		
		this.server = server;
		
		moves = new LinkedList<MoveRequest>();
		
		//Network Listeners
		moveListener = new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				
				if(object instanceof MoveRequest){
					MoveRequest r = (MoveRequest)object;
					addMove(r);
				}
				
			}
			
		};
		server.addListener(moveListener);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(EnergyComponent.class, HealthComponent.class).get());
	}
	
	private void addMove(MoveRequest r){
		moves.add(r);
	}
	
	@Override
	public void update(float deltaTime) {
		
		LinkedList<MoveRequest> moves = new LinkedList<MoveRequest>();
		while(!this.moves.isEmpty()){
			moves.add(this.moves.pop());
		}
		
		//Checks if all moves can be performed
		for(Entity entity : entities){
			
			for(int i = 0; i < moves.size(); ++i){
				
				IdComponent ic = im.get(entity);
				MoveRequest r = moves.get(i);
				
				//Check if move can be performed
				if(ic.name.equals(r.name)){
					
					//Energy handling
					EnergyComponent ec = em.get(entity);
					if(ec.energy >= getEnergyCost(r.move)){
						ec.energy -= getEnergyCost(r.move);
					}else{
						moves.remove(i);
						--i;
					}
					
				}
			}
			
		}
		
		for(int i = 0; i < entities.size(); ++i){
		
			Entity entity = entities.get(i);
			
			for(MoveRequest r : moves){
				IdComponent ic = im.get(entity);
				
				//Perform moves
				if(ic.name.equals(r.name)){
					preformMove((ServerPlayer)entity, r);
				}
				
			}
			
		}
		
		moves.clear();
		
	}

	private void preformMove(ServerPlayer player, MoveRequest r) {
		
		//Checks for blocking
		if(r.move == MoveType.BLOCK){
			
			StateComponent sc = sm.get(player);
			
			for(Entity entity : entities){
				if(!entity.equals(player)){
					IdComponent ic = im.get(player);
					sendAnimation(player, ic.id, r);
				}
			}
			
			sc.state =  PlayerState.BLOCKING;
			player.setStateTimer();
			
		}else{
			
			//Check for moves
			for(int i = 0; i < entities.size(); ++i){
				
				Entity entity = entities.get(i);
				
				IdComponent ic = im.get(entity);
				
				//If the player is not the player performing the move
				if(!ic.name.equals(r.name)){
					
					EnergyComponent ec = em.get(entity);
					StateComponent sc = sm.get(entity);
					
					//Gets the adjusted damage
					int damage = getDamage((ServerPlayer)entity, r.move);
					
					//Jab Energy Mitigation
					if(r.move == MoveType.JAB && ec.energy > ec.MAX_ENERGY * .5f){
						
						Gdx.app.log("Move System", ic.name + ": Energy -> ( " + ec.energy + ", " + (ec.energy - damage) + ")");
						
						ec.energy -= damage;
						if(ec.energy < 0 ){
							ec.energy = 0;
						}
						
						sendStats(player);
						sendStats(entity);
						
					}else{
						
						//Health Deduction
						HealthComponent hc = hm.get(entity);
						Gdx.app.log("Move System", ic.name + ": Health -> ( " + hc.health + ", " + (hc.health - damage) + ")");
						hc.health -= damage;
						
						if(hc.health < 0){
							hc.health = 0;
							sc.state =  PlayerState.KNOCKED_OUT;
						}
						
						
						//Attacker update
						sendStats(player);
						sendAnimation(player, ic.id, r);
						
						//Defender update
						sc = sm.get(player);
						sendStats(entity);
					}
				}
			}
		}
		
	}
	
	private void sendAnimation(Entity player, int enemyID, MoveRequest r) {
		//Send to the attacker
		IdComponent ic = im.get(player);
		server.sendToTCP(ic.id, new AnimationResponse(ic.name, r.move));
		server.sendToTCP(enemyID, new AnimationResponse(ic.name, r.move));
	}

	private void sendStats(Entity player){
		
		HealthComponent hc = hm.get(player);
		EnergyComponent ec = em.get(player);
		IdComponent ic = im.get(player);
		
		Gdx.app.log("MoveSystem", "Sending Stats");
		
		server.sendToTCP(ic.id, new StatResponse(hc.health, ec.energy));
		
	}
	
	private int getDamage(ServerPlayer opponent, MoveType move){
		
		StateComponent stateComponent = sm.get(opponent);
		
		int damage = 0;
		PlayerState state = stateComponent.state;
		stateComponent.move = move;
		
		if(state == PlayerState.BLOCKING){
			return 0;
		}
		else if(move == MoveType.JAB){
			
			damage = MoveInformation.JAB_DAMAGE;
			
		}else if(move == MoveType.CROSS){
			
			damage = MoveInformation.CROSS_DAMAGE;
			if(PlayerState.JABBING == state){
				damage *= MoveInformation.CROSS_MOD;
			}
			
		}else if(move == MoveType.UPPERCUT){
			
			damage = MoveInformation.UPPERCUT_DAMAGE;
			stateComponent.state = PlayerState.WEAKEND;
			opponent.setStateTimer();
			
		}else if(move == MoveType.HOOK){
			
			damage = MoveInformation.HOOK_DAMAGE;
			if(stateComponent.state == PlayerState.BLOCKING){
				damage *= MoveInformation.HOOK_MOD;
			}
			
		}else if(move == MoveType.COUNTER){
			
			if(stateComponent.state != PlayerState.BLOCKING && stateComponent.state != PlayerState.OPEN){
				damage = MoveInformation.COUNTER_DAMAGE;
			}
			
		}
		
		if(stateComponent.state == PlayerState.WEAKEND){
			damage *= MoveInformation.WEAKEND_MOD;
		}
		
		return damage;
		
	}
	
	private int getEnergyCost(MoveType move) {
		
		if(move == MoveType.JAB){
			return MoveInformation.JAB_COST;
		}else if(move == MoveType.CROSS){
			return MoveInformation.CROSS_COST;
		}else if(move == MoveType.UPPERCUT){
			return MoveInformation.UPPERCUT_COST;
		}else if(move == MoveType.HOOK){
			return MoveInformation.HOOK_COST;
		}else if(move == MoveType.BLOCK){
			return MoveInformation.BLOCK_COST;
		}else if(move == MoveType.COUNTER){
			return MoveInformation.COUNTER_COST;
		}
		
		return MoveInformation.MAX_ENERGY + 1; 
	}
	
	public void dispose(){
		server.removeListener(moveListener);
	}
	
}
