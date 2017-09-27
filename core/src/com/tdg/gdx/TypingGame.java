package com.tdg.gdx;

import com.badlogic.gdx.Game;
import Client.Screens.MenuScreen;

public class TypingGame extends Game{
	
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
	
}
