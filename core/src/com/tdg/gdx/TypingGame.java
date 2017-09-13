package com.tdg.gdx;

import java.util.Scanner;

import com.badlogic.gdx.Game;
import Client.Screens.MenuScreen;

public class TypingGame extends Game{

	Scanner in;
	
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
	
}
