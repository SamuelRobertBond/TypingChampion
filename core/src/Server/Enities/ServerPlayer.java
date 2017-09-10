package Server.Enities;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;

import Server.Components.EnergyComponent;
import Server.Components.HealthComponent;
import Server.Components.IdComponent;

public class ServerPlayer extends Entity{
	
	public ServerPlayer(String name, Connection connection) {
		add(new IdComponent(name, connection.getID()));
		add(new HealthComponent());
		add(new EnergyComponent());
	}
	
}
