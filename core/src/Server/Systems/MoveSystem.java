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

public class MoveSystem extends EntitySystem{

	private Server server;
	
	private ImmutableArray<Entity> entities;
	
	private ComponentMapper<IdComponent> im = ComponentMapper.getFor(IdComponent.class);
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
		
		//Checks if all moves can be preformed
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
		
		//Preforms moves
		for(Entity entity : entities){
			
			for(MoveRequest r : moves){
				
				IdComponent ic = im.get(entity);
				
				//Perform moves
				if(ic.id != r.id){
					HealthComponent hc = hm.get(entity);
					
				}
				
			}
			
		}
		
	}

	private float getEnergyCost(MoveType move) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
