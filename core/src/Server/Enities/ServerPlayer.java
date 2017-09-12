package Server.Enities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.esotericsoftware.kryonet.Connection;

import Server.Components.EnergyComponent;
import Server.Components.HealthComponent;
import Server.Components.IdComponent;
import Server.Components.StateComponent;
import Server.Utils.PlayerState;

public class ServerPlayer extends Entity{
	
	private StateComponent stateComponent;
	private Timer timer;
	
	private boolean ready;
	
	public ServerPlayer(String name, Connection connection) {
		
		timer = new Timer();
		
		add(new IdComponent(name, connection.getID()));
		add(new HealthComponent());
		add(new EnergyComponent());
		
		ready = false;
	}

	public void setStateTimer(int timeout) {
		timer.clear();
		timer.scheduleTask(new Task(){
			@Override
			public void run() {
				stateComponent.state = PlayerState.OPEN;
			}
			
		}, timeout);
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isReady() {
		return ready;
	}
	
}
