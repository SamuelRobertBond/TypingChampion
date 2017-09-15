package Server.Enities;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;

import Server.Components.EnergyComponent;
import Server.Components.HealthComponent;
import Server.Components.IdComponent;
import Server.Components.StateComponent;
import Server.Components.WordComponent;
import Server.Utils.PlayerState;

public class ServerPlayer extends Entity{
	
	private StateComponent stateComponent;
	private IdComponent idComponent;
	private HealthComponent healthComponent;
	private EnergyComponent energyComponent;
	private WordComponent wordComponent;
	
	private Timer timer;
	
	private long TIMEOUT = 1000;
	
	private boolean ready;
	
	public ServerPlayer(String name, Connection connection) {
		
		timer = new Timer();
		
		this.ready = false;
		
		//Components
		idComponent = new IdComponent(name, connection.getID());
		healthComponent = new HealthComponent();
		energyComponent = new EnergyComponent();
		stateComponent = new StateComponent();
		wordComponent = new WordComponent("First");
		
		add(idComponent);
		add(healthComponent);
		add(energyComponent);
		add(stateComponent);
		add(wordComponent);
		
	}

	public void setStateTimer() {
		timer.cancel();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				stateComponent.state = PlayerState.OPEN;
			}
			
		}, TIMEOUT);
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isReady() {
		return ready;
	}

	public String getName() {
		return idComponent.name;
	}
	
	public int getID(){
		return idComponent.id;
	}
	
}
