package com.senegas.kickoff;

import com.badlogic.gdx.Game;
import com.senegas.kickoff.screens.MainMenu;

public class KickOff extends Game {
	
	public static final String TITLE = "Open Kick Off";
    public static final String VERSION = "0.3.6";

	@Override
	public void create () {
		setScreen(new MainMenu());
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {	
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
