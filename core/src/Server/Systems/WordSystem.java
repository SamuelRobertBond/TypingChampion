package Server.Systems;

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

import Client.Requests.WordSubmissionRequest;
import Server.Components.EnergyComponent;
import Server.Components.HealthComponent;
import Server.Components.IdComponent;
import Server.Components.WordComponent;
import Server.Responses.StatResponse;
import Server.Responses.WordSubmissionResponse;
import Server.Utils.WordUtil;

public class WordSystem extends EntitySystem{
	
	private Server server;
	
	private ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
	private ComponentMapper<IdComponent> im = ComponentMapper.getFor(IdComponent.class);
	private ComponentMapper<WordComponent> wm = ComponentMapper.getFor(WordComponent.class);
	private ComponentMapper<EnergyComponent> em = ComponentMapper.getFor(EnergyComponent.class);
	
	private ImmutableArray<Entity> entities;
	private Listener wordListener;
	
	public WordSystem(Server server) {
		
		this.server = server;
		
		wordListener = new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				
				if(object instanceof WordSubmissionRequest){
					WordSubmissionRequest r = (WordSubmissionRequest)object;
					processWord(connection.getID(), r);
				}
				
			}
			
		};
		server.addListener(wordListener);
	}
	
	private void processWord(int id, WordSubmissionRequest r){
		
		Gdx.app.log("WordSystem", "Word Recieved");
		
		for(Entity entity : entities){
			
			IdComponent ic = im.get(entity);
			WordComponent wc = wm.get(entity);
			
			if(ic.id == id){
				
				if(wc.word.toLowerCase().equals(r.word.toLowerCase())){
					
					EnergyComponent ec = em.get(entity);
					ec.energy += ec.ENERGY_PER_LETTER * wc.word.length();
					
					if(ec.energy > 100){
						ec.energy = 100;
					}
					
					wc.word = WordUtil.getWord();
					server.sendToTCP(id, new WordSubmissionResponse(true, wc.word));
					sendStats(entity);
					
				}else{
					server.sendToTCP(id, new WordSubmissionResponse(false, null));
				}

			}
			
		}
		
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(WordComponent.class).get());
	}
	
	public void dispose() {
		server.removeListener(wordListener);
	}
	
	private void sendStats(Entity player){
		
		HealthComponent hc = hm.get(player);
		EnergyComponent ec = em.get(player);
		IdComponent ic = im.get(player);
		
		Gdx.app.log("MoveSystem", "Sending Stats");
		
		server.sendToTCP(ic.id, new StatResponse(hc.health, ec.energy));
		
	}
	
}
