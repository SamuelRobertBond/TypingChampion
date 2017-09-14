package Client.Screens;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.tdg.gdx.TypingGame;

import Client.Requests.JoinRequest;
import Client.Utils.ClientManager;
import Client.Utils.Constants;
import Client.Utils.MenuManager;
import Server.Responses.JoinResponse;

public class ConnectionScreen implements Screen{

	private TypingGame game;
	private StretchViewport view;
	
	private MenuManager menu;
	private Table popupTable;
	private TextField field;
	
	private ClientManager clientManager;
	
	private String name;
	
	private Stack<Listener> listeners;
	
	private boolean changeScreen;

	
	public ConnectionScreen(TypingGame game, String name) {
				
		this.game = game;
		view = new StretchViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		listeners = new Stack<Listener>();
		
		clientManager = new ClientManager(name);
		
		menu = new MenuManager(view);
		
		menu.setCellSize(80, 40);
		
		menu.addLabel("Connection Type?");
		menu.row();
		
		menu.addTextButton("Lan").addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				localGame();
				
			}
			
		});
		
		menu.addTextButton("Net").addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				netGame();
			}
			
		});
		
		this.name = name;
		
		popupTable = menu.addTable();
		menu.addLabel(popupTable, "IP Address:");
		popupTable.row();
		
		field = menu.addTextField(popupTable);
		menu.setActorCellSize(popupTable, 400, 40, field);
		
		popupTable.row();
		
		menu.addTextButton(popupTable, "Back").addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				popupOff();
			}

			
		});
		
		menu.addTextButton(popupTable, "Connect").addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				if(!field.getText().equals("")){
					connect(field.getText());
				}
				
			}
			
		});
		
		menu.addTextButton("Back").addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				back();
			}
			
		});
		
		//Join Response Listener
		listeners.push(new Listener(){
			
			@Override
			public void received(Connection connection, Object object) {
				
				if(object instanceof JoinResponse){
					
					JoinResponse r = (JoinResponse)object;
					
					if(r.success){
						joinLobby();
					}
					
				}
				
			}
			
		});
		clientManager.getClient().addListener(listeners.peek());
		
		changeScreen = false;
		
	}
	
	private void joinLobby(){
		changeScreen = true;
	}
	
	private void popupOff() {
		popupTable.setVisible(false);
		menu.getMainTable().setVisible(true);
	}
	
	private void back(){
		dispose();
		game.setScreen(new MenuScreen(game));
	}
	
	private void connect(String addr){
		
		if(clientManager.connectNet(name, addr, 54555, 54777)){
			clientManager.getClient().sendTCP(new JoinRequest(name));
		}
		
	}
	
	private void localGame(){
		
		if(clientManager.connectLan(name, 54555, 54777)){
			clientManager.getClient().sendTCP(new JoinRequest(name));
		}
			
	}
	
	private void netGame(){
		menu.getMainTable().setVisible(false);
		popupTable.setVisible(true);
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		menu.render(delta);
		
		if(changeScreen){
			dispose();
			game.setScreen(new LobbyScreen(game, clientManager));
		}
		
	}


	@Override
	public void resize(int width, int height) {
		view.update(width, height);
		
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dispose() {
		
		menu.dispose();
		
		while(!listeners.isEmpty()){
			clientManager.getClient().removeListener(listeners.pop());
		}
		
	}
	
}
