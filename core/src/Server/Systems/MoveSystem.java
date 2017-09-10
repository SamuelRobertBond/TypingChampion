package Server.Systems;

import java.util.LinkedList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Server;

import Client.Requests.MoveRequest;
import Client.Utils.MoveType;
import Server.Components.EnergyComponent;
import Server.Components.HealthComponent;
import Server.Components.IdComponent;
import Server.Components.StateComponent;
import Server.Enities.ServerPlayer;
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
	
	public MoveSystem(Server server) {
		this.server = server;
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(EnergyComponent.class, HealthComponent.class).get());
	}
	
	@Override
	public void update(float deltaTime) {
		
		LinkedList<MoveRequest> moves = (LinkedList<MoveRequest>) this.moves.clone(); //Cloned because move requests could be received during processing
		
		//Checks if all moves can be performed
		for(Entity entity : entities){
			
			for(int i = 0; i < moves.size(); ++i){
				
				IdComponent ic = im.get(entity);
				MoveRequest r = moves.get(i);
				
				//Check if move can be performed
				if(ic.id == r.id){
					
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
		
		for(MoveRequest r : moves){
			
			//Performs moves
			for(Entity entity : entities){
				IdComponent ic = im.get(entity);
				
				//Perform moves
				if(ic.id == r.id){
					preformMove((ServerPlayer)entity, r);
				}
				
			}
			
		}
		
		
		
		moves.clear();
		
	}

	
	//Performs the specified move
	private void preformMove(ServerPlayer player, MoveRequest r) {
		
		if(r.move == MoveType.BLOCK){
			StateComponent sc = sm.get(player);
			sc.state =  PlayerState.BLOCKING;
			player.setStateTimer(1);
		}else{
			for(Entity entity : entities){
				IdComponent ic = im.get(entity);
				if(ic.id != r.id){
					
				}
			}
		}
		
	}

	private int getDamage(MoveType move){
		
		if(move == MoveType.JAB){
			return MoveInformation.JAB_DAMAGE;
		}else if(move == MoveType.CROSS){
			return MoveInformation.CROSS_DAMAGE;
		}else if(move == MoveType.UPPERCUT){
			return MoveInformation.UPPERCUT_DAMAGE;
		}else if(move == MoveType.HOOK){
			return MoveInformation.HOOK_DAMAGE;
		}else if(move == MoveType.COUNTER){
			return MoveInformation.COUNTER_DAMAGE;
		}
		
		return 0;
		
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
	
	
}
